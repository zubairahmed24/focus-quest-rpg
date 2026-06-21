package com.focusquest.domain.model

/**
 * Represents the current state of the focus timer state machine.
 *
 * State transitions:
 *   Idle → Focusing (player starts session)
 *   Focusing → Break (session completes)
 *   Focusing → Paused (player pauses)
 *   Paused → Focusing (player resumes)
 *   Focusing → Idle (player abandons)
 *   Break → Idle (break completes or is skipped)
 *   Break → Paused (player pauses during break)
 *   Paused → Break (player resumes during break)
 *
 * Pure Kotlin — no Android dependencies.
 */
sealed class FocusTimerState {

    /** Timer is not running. Player can start a new focus session. */
    object Idle : FocusTimerState()

    /** Focus session is active. [remainingSeconds] counts down to 0. */
    data class Focusing(val remainingSeconds: Int) : FocusTimerState()

    /** Break timer is active. [remainingSeconds] counts down to 0. */
    data class Break(val remainingSeconds: Int) : FocusTimerState()

    /** Timer is paused. [wasFocusing] tracks whether the pause occurred during
     * a focus session or a break, so resume returns to the correct state. */
    data class Paused(
        val remainingSeconds: Int,
        val wasFocusing: Boolean
    ) : FocusTimerState()
}
