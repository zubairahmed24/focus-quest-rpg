package com.focusquest.presentation.stats

import java.time.LocalDate

data class StatsUiState(
    val isLoading: Boolean = true,
    val playerLevel: Int = 1,
    val totalFocusTimeFormatted: String = "0m",
    val bossesDefeatedCount: Int = 0,
    val totalBossesCount: Int = 5,
    val currentStreak: Int = 0,
    val weeklyChartBars: List<DailyBarState> = emptyList(),
    val bossProgressItems: List<BossProgressItem> = emptyList()
)

data class DailyBarState(
    val date: LocalDate,
    val dayLabel: String,
    val minutes: Int,
    val valueLabel: String,
    val isToday: Boolean
)

enum class BossStatus {
    DEFEATED,
    CURRENT,
    LOCKED
}

data class BossProgressItem(
    val id: Int,
    val name: String,
    val maxHp: Int,
    val status: BossStatus
)
