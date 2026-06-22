package com.focusquest.presentation.battle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.focusquest.domain.model.BattleResult
import com.focusquest.domain.model.Boss
import com.focusquest.domain.model.FocusTimerState
import com.focusquest.domain.model.PlayerState
import com.focusquest.domain.repository.BossRepository
import com.focusquest.domain.service.FocusTimerService
import com.focusquest.domain.usecase.CompleteFocusSessionUseCase
import com.focusquest.domain.usecase.GetCurrentBossUseCase
import com.focusquest.domain.usecase.GetPlayerStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Battle screen.
 *
 * Responsibilities:
 * - Observe player state (level, XP, streak, boss HP) via GetPlayerStateUseCase
 * - Observe current boss via GetCurrentBossUseCase
 * - Observe timer state via FocusTimerService
 * - Execute use cases (start, complete, abandon focus session)
 * - Expose immutable BattleUiState to the UI
 * - Emit one-shot BattleUiEvent via SharedFlow
 *
 * Architecture rules:
 * - No mutable state exposed to UI (only StateFlow<BattleUiState>)
 * - No business logic inside Compose (all logic is here or in use cases)
 * - StateFlow only (no LiveData, no StateFlow<MutableState>)
 */
@HiltViewModel
class BattleViewModel @Inject constructor(
    private val getPlayerStateUseCase: GetPlayerStateUseCase,
    private val getCurrentBossUseCase: GetCurrentBossUseCase,
    private val completeFocusSessionUseCase: CompleteFocusSessionUseCase,
    private val focusTimerService: FocusTimerService,
    private val bossRepository: BossRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BattleUiState())
    val uiState: StateFlow<BattleUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<BattleUiEvent>(extraBufferCapacity = 10)
    val events: SharedFlow<BattleUiEvent> = _events.asSharedFlow()

    init {
        observeState()
        observeTimerEvents()
        recoverSessionIfNeeded()
    }

    /**
     * Combines player state, current boss, and timer state into BattleUiState.
     * This is the single source of truth for the UI.
     */
    private fun observeState() {
        viewModelScope.launch {
            combine(
                getPlayerStateUseCase(),
                focusTimerService.timerState
            ) { player: PlayerState?, timerState: FocusTimerState ->
                Pair(player, timerState)
            }.collect { (player, timerState) ->
                if (player == null) {
                    _uiState.value = BattleUiState(isLoading = true)
                    return@collect
                }

                // Fetch the current boss synchronously (it's a one-shot query)
                val boss = getCurrentBossUseCase()

                _uiState.value = BattleUiState(
                    isLoading = false,
                    playerLevel = player.level,
                    playerXp = player.xp,
                    xpToNextLevel = com.focusquest.util.Constants.XP_PER_LEVEL,
                    streak = player.streak,
                    currentBoss = boss,
                    bossHpRemaining = player.currentBossHpRemaining,
                    bossMaxHp = boss?.maxHp ?: 100,
                    timerState = timerState,
                    showGiveUpDialog = _uiState.value.showGiveUpDialog,
                    showVictory = _uiState.value.showVictory,
                    lastBattleResult = _uiState.value.lastBattleResult,
                    campaignComplete = boss == null && player.totalBossesDefeated >= 5,
                    totalFocusMinutes = player.totalFocusMinutes,
                    totalBossesDefeated = player.totalBossesDefeated
                )
            }
        }
    }

    /**
     * Collects timer completion events and triggers session completion.
     */
    private fun observeTimerEvents() {
        viewModelScope.launch {
            focusTimerService.events.collect { event ->
                when (event) {
                    is FocusTimerService.TimerEvent.FocusSessionCompleted -> {
                        completeSession()
                    }
                    is FocusTimerService.TimerEvent.BreakCompleted -> {
                        // Break is over — timer service already set state to Idle
                    }
                }
            }
        }
    }

    /**
     * On ViewModel init, check if there's an interrupted session to recover.
     * If the session completed while the app was dead, auto-complete it.
     */
    private fun recoverSessionIfNeeded() {
        viewModelScope.launch {
            try {
                val wasCompleted = focusTimerService.recoverSession()
                if (wasCompleted) {
                    completeSession()
                    _events.emit(BattleUiEvent.SessionRecovered)
                }
            } catch (e: Exception) {
                // Recovery failed — log but don't crash
                _events.emit(BattleUiEvent.Error("Failed to recover session: ${e.message}"))
            }
        }
    }

    /**
     * Handles all UI actions dispatched from the Battle screen.
     */
    fun onAction(action: BattleUiAction) {
        when (action) {
            BattleUiAction.StartFocus -> startFocus()
            BattleUiAction.Pause -> focusTimerService.pause()
            BattleUiAction.Resume -> focusTimerService.resume()
            BattleUiAction.GiveUp -> {
                _uiState.value = _uiState.value.copy(showGiveUpDialog = true)
            }
            BattleUiAction.ConfirmGiveUp -> {
                _uiState.value = _uiState.value.copy(showGiveUpDialog = false)
                abandonSession()
            }
            BattleUiAction.DismissGiveUpDialog -> {
                _uiState.value = _uiState.value.copy(showGiveUpDialog = false)
            }
            BattleUiAction.DismissVictory -> {
                _uiState.value = _uiState.value.copy(showVictory = false, lastBattleResult = null)
            }
            BattleUiAction.StartBreak -> focusTimerService.startBreak()
            BattleUiAction.SkipBreak -> focusTimerService.stop()
        }
    }

    /**
     * Starts a focus session via the timer service.
     * The service persists the start timestamp to Room for recovery.
     */
    private fun startFocus() {
        viewModelScope.launch {
            try {
                focusTimerService.startFocus()
            } catch (e: Exception) {
                _events.emit(BattleUiEvent.Error("Failed to start session: ${e.message}"))
            }
        }
    }

    /**
     * Completes a focus session via CompleteFocusSessionUseCase.
     * This is the core game loop — damage, XP, streak, boss defeat, unlock.
     */
    private fun completeSession() {
        viewModelScope.launch {
            try {
                val result = completeFocusSessionUseCase()

                when (result) {
                    is BattleResult.DamageDealt -> {
                        _events.emit(BattleUiEvent.DamageDealt(result.damage, result.bossHpRemaining))
                    }
                    is BattleResult.BossDefeated -> {
                        if (result.nextBoss == null) {
                            // Campaign complete
                            _uiState.value = _uiState.value.copy(
                                showVictory = true,
                                lastBattleResult = result,
                                campaignComplete = true
                            )
                            _events.emit(BattleUiEvent.CampaignComplete)
                        } else {
                            _uiState.value = _uiState.value.copy(
                                showVictory = true,
                                lastBattleResult = result
                            )
                            _events.emit(BattleUiEvent.BossDefeated(result.boss.name, result.xpGained))
                        }
                    }
                }
            } catch (e: Exception) {
                _events.emit(BattleUiEvent.Error("Failed to complete session: ${e.message}"))
            }
        }
    }

    /**
     * Abandons the current focus session.
     * No damage is dealt, no XP is awarded. Timer is reset to Idle.
     */
    private fun abandonSession() {
        viewModelScope.launch {
            try {
                focusTimerService.abandon()
            } catch (e: Exception) {
                _events.emit(BattleUiEvent.Error("Failed to abandon session: ${e.message}"))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Stop the UI ticker but do NOT abandon the session.
        // The persisted timestamp allows recovery on next launch.
        focusTimerService.stop()
    }
}
