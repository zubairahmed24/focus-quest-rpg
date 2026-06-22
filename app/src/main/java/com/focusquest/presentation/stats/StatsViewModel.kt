package com.focusquest.presentation.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.focusquest.domain.repository.BossRepository
import com.focusquest.domain.usecase.GetPlayerStateUseCase
import com.focusquest.domain.usecase.GetWeeklyFocusStatsUseCase
import com.focusquest.util.DateTimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val getPlayerStateUseCase: GetPlayerStateUseCase,
    private val getWeeklyFocusStatsUseCase: GetWeeklyFocusStatsUseCase,
    private val bossRepository: BossRepository
) : ViewModel() {

    val uiState: StateFlow<StatsUiState> = combine(
        getPlayerStateUseCase(),
        getWeeklyFocusStatsUseCase(),
        bossRepository.getAllBosses()
    ) { playerState, weeklyStats, bosses ->
        if (playerState == null) {
            StatsUiState(isLoading = true)
        } else {
            val today = LocalDate.now()

            val chartBars = weeklyStats.map { stats ->
                val isToday = stats.date == today
                val dayOfWeekLabel = stats.date.dayOfWeek.getDisplayName(
                    TextStyle.SHORT,
                    Locale.getDefault()
                )
                DailyBarState(
                    date = stats.date,
                    dayLabel = dayOfWeekLabel,
                    minutes = stats.totalMinutes,
                    valueLabel = if (stats.totalMinutes > 0) "${stats.totalMinutes}m" else "0",
                    isToday = isToday
                )
            }

            val bossItems = bosses.map { boss ->
                val status = when {
                    boss.isDefeated -> BossStatus.DEFEATED
                    boss.isUnlocked -> BossStatus.CURRENT
                    else -> BossStatus.LOCKED
                }
                BossProgressItem(
                    id = boss.id,
                    name = boss.name,
                    maxHp = boss.maxHp,
                    status = status
                )
            }

            StatsUiState(
                isLoading = false,
                playerLevel = playerState.level,
                totalFocusTimeFormatted = DateTimeUtils.formatFocusTime(playerState.totalFocusMinutes),
                bossesDefeatedCount = playerState.totalBossesDefeated,
                totalBossesCount = bosses.size,
                currentStreak = playerState.streak,
                weeklyChartBars = chartBars,
                bossProgressItems = bossItems
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StatsUiState(isLoading = true)
    )
}
