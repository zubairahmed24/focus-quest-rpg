package com.focusquest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A single focus session record — one completed (or abandoned) pomodoro session.
 *
 * startTime is stored as an ISO datetime string for Room compatibility.
 */
@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startTime: String,
    val durationMinutes: Int,
    val completed: Boolean,
    val bossId: Int,
    val damageDealt: Int
)
