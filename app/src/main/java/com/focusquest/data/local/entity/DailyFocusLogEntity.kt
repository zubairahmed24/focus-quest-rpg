package com.focusquest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Aggregated focus minutes for a single calendar day.
 * Used to render the weekly bar chart on the Stats screen.
 *
 * date is the primary key, stored as ISO string (yyyy-MM-dd).
 */
@Entity(tableName = "daily_focus_log")
data class DailyFocusLogEntity(
    @PrimaryKey val date: String,
    val totalMinutes: Int,
    val sessionsCompleted: Int
)
