package com.focusquest.domain.model

import java.time.LocalDate

/**
 * Domain representation of the player's game state.
 *
 * This is a pure Kotlin model — no Android framework dependencies.
 * The data layer converts between this and PlayerStateEntity via mappers.
 */
data class PlayerState(
    val id: Long = 1,
    val level: Int = 1,
    val xp: Int = 0,
    val currentBossId: Int = 1,
    val currentBossHpRemaining: Int = 100,
    val streak: Int = 0,
    val lastFocusDate: LocalDate = LocalDate.now(),
    val totalFocusMinutes: Int = 0,
    val totalSessionsCompleted: Int = 0,
    val totalBossesDefeated: Int = 0
)
