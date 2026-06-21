package com.focusquest.presentation.battle

import com.focusquest.domain.model.BattleResult
import com.focusquest.domain.model.Boss
import com.focusquest.domain.model.FocusTimerState
import com.focusquest.domain.model.PlayerState

/**
 * Immutable UI state for the Battle screen.
 *
 * This is the ONLY state the Compose layer sees. The ViewModel
 * constructs this from domain models and timer state.
 *
 * All fields are val — no mutable state is exposed to UI.
 */
data class BattleUiState(
    val isLoading: Boolean = true,
    val playerLevel: Int = 1,
    val playerXp: Int = 0,
    val xpToNextLevel: Int = 300,
    val streak: Int = 0,
    val currentBoss: Boss? = null,
    val bossHpRemaining: Int = 0,
    val bossMaxHp: Int = 100,
    val timerState: FocusTimerState = FocusTimerState.Idle,
    val showGiveUpDialog: Boolean = false,
    val showVictory: Boolean = false,
    val lastBattleResult: BattleResult? = null,
    val campaignComplete: Boolean = false
) {
    /**
     * XP progress as a 0..1 fraction for the progress bar.
     */
    val xpProgress: Float
        get() = if (xpToNextLevel > 0) playerXp.toFloat() / xpToNextLevel.toFloat() else 0f

    /**
     * True when the timer is actively running (focusing or break).
     */
    val isTimerRunning: Boolean
        get() = timerState is FocusTimerState.Focusing || timerState is FocusTimerState.Break

    /**
     * True when the timer is paused.
     */
    val isPaused: Boolean
        get() = timerState is FocusTimerState.Paused

    /**
     * Remaining seconds from the timer state, or 0 if idle.
     */
    val remainingSeconds: Int
        get() = when (val s = timerState) {
            is FocusTimerState.Focusing -> s.remainingSeconds
            is FocusTimerState.Break -> s.remainingSeconds
            is FocusTimerState.Paused -> s.remainingSeconds
            else -> 0
        }

    /**
     * Total seconds for the current timer phase (focus or break).
     */
    val totalSeconds: Int
        get() = when (val s = timerState) {
            is FocusTimerState.Focusing -> com.focusquest.util.Constants.FOCUS_DURATION_MINUTES * 60
            is FocusTimerState.Break -> com.focusquest.util.Constants.BREAK_DURATION_MINUTES * 60
            is FocusTimerState.Paused -> if (s.wasFocusing) com.focusquest.util.Constants.FOCUS_DURATION_MINUTES * 60 else com.focusquest.util.Constants.BREAK_DURATION_MINUTES * 60
            else -> 0
        }

    /**
     * Whether the current timer phase is a focus session (not break).
     */
    val isFocusing: Boolean
        get() = when (val s = timerState) {
            is FocusTimerState.Focusing -> true
            is FocusTimerState.Paused -> s.wasFocusing
            else -> false
        }
}

/**
 * One-shot UI events emitted by the ViewModel.
 * These are consumed via SharedFlow and trigger UI effects (snackbars, navigation).
 */
sealed class BattleUiEvent {
    /** Boss was defeated — navigate to victory or show inline victory. */
    data class BossDefeated(val bossName: String, val xpGained: Int) : BattleUiEvent()
    /** Focus session completed but boss survived. */
    data class DamageDealt(val damage: Int, val bossHpRemaining: Int) : BattleUiEvent()
    /** Campaign fully completed — all 5 bosses defeated. */
    object CampaignComplete : BattleUiEvent()
    /** An error occurred during session completion. */
    data class Error(val message: String) : BattleUiEvent()
    /** Session was recovered after process death. */
    object SessionRecovered : BattleUiEvent()
}

/**
 * User actions that the UI can dispatch to the ViewModel.
 * These are plain objects — the ViewModel handles them via functions.
 */
sealed class BattleUiAction {
    object StartFocus : BattleUiAction()
    object Pause : BattleUiAction()
    object Resume : BattleUiAction()
    object GiveUp : BattleUiAction()
    object ConfirmGiveUp : BattleUiAction()
    object DismissGiveUpDialog : BattleUiAction()
    object DismissVictory : BattleUiAction()
    object StartBreak : BattleUiAction()
    object SkipBreak : BattleUiAction()
}
