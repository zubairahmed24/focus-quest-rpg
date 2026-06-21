package com.focusquest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents one of the 5 procrastination bosses in the campaign.
 * Bosses are pre-populated on first database creation and updated
 * as the player defeats them.
 *
 * @param order Sequential progression order (1 = first boss, 5 = final boss).
 * @param isUnlocked Whether the player can currently battle this boss.
 * @param isDefeated Whether the player has defeated this boss.
 */
@Entity(tableName = "bosses")
data class BossEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val maxHp: Int,
    val taunt: String,
    val isUnlocked: Boolean,
    val isDefeated: Boolean,
    val order: Int
)
