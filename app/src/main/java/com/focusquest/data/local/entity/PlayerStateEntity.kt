package com.focusquest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Singleton row storing the player's overall game state.
 * There is only ever one row with id = 1.
 *
 * Dates are stored as ISO strings (yyyy-MM-dd) because Room does not
 * natively support java.time types. Conversion happens in the mapper layer.
 */
@Entity(tableName = "player_state")
data class PlayerStateEntity(
    @PrimaryKey val id: Long = 1,
    val level: Int = 1,
    val xp: Int = 0,
    val currentBossId: Int = 1,
    val currentBossHpRemaining: Int = 100,
    val streak: Int = 0,
    val lastFocusDate: String,
    val totalFocusMinutes: Int = 0,
    val totalSessionsCompleted: Int = 0,
    val totalBossesDefeated: Int = 0
)
