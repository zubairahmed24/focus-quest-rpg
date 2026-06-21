package com.focusquest.domain.service

import com.focusquest.domain.model.FocusTimerState
import com.focusquest.domain.repository.PlayerRepository
import com.focusquest.util.Constants
import com.focusquest.util.DateTimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Timestamp-based focus timer engine.
 *
 * ARCHITECTURE: See ADR-001-timer-architecture.md
 *
 * The source of truth for timer state is a persisted timestamp
 * (activeSessionStartTime in player_state), NOT a decrementing counter.
 * The coroutine ticker is for UI display only — it recalculates remaining
 * time from the timestamp on every tick, ensuring zero drift.
 *
 * Key properties:
 * - Survives app backgrounding (timestamp persists in Room)
 * - Survives process death (timestamp persists in Room)
 * - Survives screen rotation (service is @Singleton)
 * - No countdown drift (always calculates from real time)
 * - No memory leaks (coroutine is cancelled on stop/abandon)
 *
 * Pause/Resume:
 * - Pause saves remaining seconds in memory (not persisted)
 * - Resume adjusts sessionStartTime to account for pause duration
 * - If app dies during pause, session auto-completes or abandons on recovery
 *
 * Break timer:
 * - In-memory only (not persisted to Room)
 * - If app dies during break, break is lost (acceptable for MVP)
 *
 * @Singleton ensures the timer survives configuration changes (screen rotation).
 */
@Singleton
class FocusTimerService @Inject constructor(
    private val playerRepository: PlayerRepository
) {

    /**
     * One-shot events emitted by the timer.
     * The ViewModel collects these to trigger use case execution.
     */
    sealed class TimerEvent {
        /** Focus session reached 0:00 — call CompleteFocusSessionUseCase. */
        object FocusSessionCompleted : TimerEvent()
        /** Break timer reached 0:00 — return to idle state. */
        object BreakCompleted : TimerEvent()
    }

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _timerState = MutableStateFlow<FocusTimerState>(FocusTimerState.Idle)
    val timerState: StateFlow<FocusTimerState> = _timerState.asStateFlow()

    private val _events = MutableSharedFlow<TimerEvent>(extraBufferCapacity = 10)
    val events: SharedFlow<TimerEvent> = _events.asSharedFlow()

    // In-memory start times (source of truth is the persisted timestamp for focus)
    private var focusSessionStartTime: LocalDateTime? = null
    private var breakStartTime: LocalDateTime? = null
    private var tickJob: Job? = null

    /**
     * Starts a focus session.
     * Saves the start timestamp to Room for recovery, sets state to Focusing,
     * and begins the UI ticker coroutine.
     */
    suspend fun startFocus() {
        focusSessionStartTime = LocalDateTime.now()
        playerRepository.updateSessionStartTime(focusSessionStartTime)
        _timerState.value = FocusTimerState.Focusing(Constants.FOCUS_DURATION_MINUTES * 60)
        startTicking()
    }

    /**
     * Starts a break timer (in-memory only, not persisted).
     * Called after a focus session completes.
     */
    fun startBreak() {
        breakStartTime = LocalDateTime.now()
        _timerState.value = FocusTimerState.Break(Constants.BREAK_DURATION_MINUTES * 60)
        startTicking()
    }

    /**
     * Pauses the timer. Saves remaining seconds in state.
     * Does NOT persist pause state — if app dies during pause,
     * the session will auto-complete or abandon on recovery.
     */
    fun pause() {
        tickJob?.cancel()
        val current = _timerState.value
        when (current) {
            is FocusTimerState.Focusing -> {
                _timerState.value = FocusTimerState.Paused(current.remainingSeconds, wasFocusing = true)
            }
            is FocusTimerState.Break -> {
                _timerState.value = FocusTimerState.Paused(current.remainingSeconds, wasFocusing = false)
            }
            else -> { /* Can't pause from Idle or Paused */ }
        }
    }

    /**
     * Resumes from pause. Adjusts the in-memory start time to account
     * for the pause duration, ensuring timestamp-based calculation remains accurate.
     */
    fun resume() {
        val current = _timerState.value
        if (current is FocusTimerState.Paused) {
            if (current.wasFocusing) {
                // Adjust focus start time: pretend we started (totalDuration - remaining) seconds ago
                val elapsedSeconds = Constants.FOCUS_DURATION_MINUTES * 60 - current.remainingSeconds
                focusSessionStartTime = LocalDateTime.now().minusSeconds(elapsedSeconds.toLong())
                _timerState.value = FocusTimerState.Focusing(current.remainingSeconds)
            } else {
                val elapsedSeconds = Constants.BREAK_DURATION_MINUTES * 60 - current.remainingSeconds
                breakStartTime = LocalDateTime.now().minusSeconds(elapsedSeconds.toLong())
                _timerState.value = FocusTimerState.Break(current.remainingSeconds)
            }
            startTicking()
        }
    }

    /**
     * Abandons the current session. Clears the persisted timestamp,
     * cancels the ticker, and resets to Idle.
     */
    suspend fun abandon() {
        tickJob?.cancel()
        focusSessionStartTime = null
        breakStartTime = null
        playerRepository.updateSessionStartTime(null)
        _timerState.value = FocusTimerState.Idle
    }

    /**
     * Recovers an interrupted focus session after process death.
     *
     * Reads the persisted session start time from Room and:
     * - If elapsed >= focus duration → returns true (session should be completed)
     * - If elapsed < focus duration → resumes the timer with correct remaining time
     * - If no persisted start time → returns false (no session to recover)
     *
     * @return true if the session completed while the app was dead,
     *         false if no recovery was needed or session was resumed.
     */
    suspend fun recoverSession(): Boolean {
        val player = playerRepository.getPlayerStateOnce() ?: return false
        val savedStartTime = player.activeSessionStartTime ?: return false

        val elapsedSeconds = ChronoUnit.SECONDS.between(savedStartTime, LocalDateTime.now())
        val focusDurationSeconds = Constants.FOCUS_DURATION_MINUTES * 60L

        return if (elapsedSeconds >= focusDurationSeconds) {
            // Session completed while app was dead.
            // Do NOT clear the persisted timestamp here — let CompleteFocusSessionUseCase do it.
            // This ensures the session is properly recorded even if recovery runs multiple times.
            focusSessionStartTime = null
            _timerState.value = FocusTimerState.Idle
            true
        } else {
            // Session is still in progress — resume with correct remaining time
            focusSessionStartTime = savedStartTime
            val remaining = (focusDurationSeconds - elapsedSeconds).toInt()
            _timerState.value = FocusTimerState.Focusing(remaining)
            startTicking()
            false
        }
    }

    /**
     * Stops the timer and resets to Idle. Does NOT clear persisted state.
     * Used when the ViewModel is cleared and doesn't want to abandon the session.
     */
    fun stop() {
        tickJob?.cancel()
        _timerState.value = FocusTimerState.Idle
    }

    // ── Private ────────────────────────────────────────────────────

    /**
     * Starts the UI ticker coroutine. This coroutine recalculates remaining
     * time from the timestamp every second, ensuring zero drift.
     * The coroutine self-cancels when the timer reaches Idle.
     */
    private fun startTicking() {
        tickJob?.cancel()
        tickJob = serviceScope.launch {
            while (isActive) {
                delay(1000L)
                tick()
            }
        }
    }

    /**
     * Recalculates remaining time from the timestamp and updates state.
     * Emits FocusSessionCompleted or BreakCompleted when the timer reaches 0.
     *
     * For focus sessions: does NOT clear the persisted timestamp here.
     * The ViewModel catches the event and calls CompleteFocusSessionUseCase,
     * which clears the timestamp. If the ViewModel is dead, the timestamp
     * persists and recovery handles it on next launch.
     */
    private suspend fun tick() {
        val current = _timerState.value
        when (current) {
            is FocusTimerState.Focusing -> {
                val start = focusSessionStartTime ?: return
                val elapsed = ChronoUnit.SECONDS.between(start, LocalDateTime.now())
                val remaining = (Constants.FOCUS_DURATION_MINUTES * 60 - elapsed.toInt()).coerceAtLeast(0)

                if (remaining <= 0) {
                    _timerState.value = FocusTimerState.Idle
                    focusSessionStartTime = null
                    tickJob?.cancel()
                    _events.emit(TimerEvent.FocusSessionCompleted)
                } else {
                    _timerState.value = FocusTimerState.Focusing(remaining)
                }
            }

            is FocusTimerState.Break -> {
                val start = breakStartTime ?: return
                val elapsed = ChronoUnit.SECONDS.between(start, LocalDateTime.now())
                val remaining = (Constants.BREAK_DURATION_MINUTES * 60 - elapsed.toInt()).coerceAtLeast(0)

                if (remaining <= 0) {
                    _timerState.value = FocusTimerState.Idle
                    breakStartTime = null
                    tickJob?.cancel()
                    _events.emit(TimerEvent.BreakCompleted)
                } else {
                    _timerState.value = FocusTimerState.Break(remaining)
                }
            }

            else -> {
                tickJob?.cancel()
            }
        }
    }
}
