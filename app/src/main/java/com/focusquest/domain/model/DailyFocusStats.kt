package com.focusquest.domain.model

import java.time.LocalDate

/**
 * Domain representation of a single day's aggregated focus stats.
 * Used by GetWeeklyFocusStatsUseCase to feed the weekly bar chart.
 *
 * Pure Kotlin — no Android dependencies.
 */
data class DailyFocusStats(
    val date: LocalDate,
    val totalMinutes: Int,
    val sessionsCompleted: Int
)
