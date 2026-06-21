package com.focusquest.domain.usecase

import com.focusquest.domain.model.DailyFocusStats
import com.focusquest.domain.repository.FocusSessionRepository
import com.focusquest.util.DateTimeUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

/**
 * Returns focus statistics for the last 7 days (including today).
 *
 * Produces a list of 7 DailyFocusStats entries, one per day from
 * 6 days ago through today. Days with no focus activity have zero values.
 *
 * Used by the Stats screen to render the weekly bar chart.
 */
class GetWeeklyFocusStatsUseCase @Inject constructor(
    private val focusSessionRepository: FocusSessionRepository
) {
    operator fun invoke(): Flow<List<DailyFocusStats>> {
        val today = LocalDate.now()
        val startDate = today.minusDays(6)
        val startDateString = DateTimeUtils.formatDate(startDate)

        return focusSessionRepository.getRecentSessions().map { sessions ->
            // Build a map of date → total minutes from sessions
            val dailyMap = sessions
                .filter { !it.startTime.toLocalDate().isBefore(startDate) }
                .groupBy { it.startTime.toLocalDate() }
                .mapValues { (_, sessionsForDay) ->
                    sessionsForDay.sumOf { it.durationMinutes } to sessionsForDay.size
                }

            // Produce 7 entries, one per day, filling gaps with zeros
            (0..6).map { offset ->
                val date = startDate.plusDays(offset.toLong())
                val (minutes, count) = dailyMap[date] ?: (0 to 0)
                DailyFocusStats(
                    date = date,
                    totalMinutes = minutes,
                    sessionsCompleted = count
                )
            }
        }
    }
}
