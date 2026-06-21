package com.focusquest.data.mapper

import com.focusquest.data.local.entity.DailyFocusLogEntity
import com.focusquest.domain.model.DailyFocusStats
import com.focusquest.util.DateTimeUtils
import javax.inject.Inject

/**
 * Converts between DailyFocusLogEntity (Room, ISO date strings)
 * and DailyFocusStats (domain, LocalDate).
 *
 * Lives in the data layer because it depends on the entity.
 */
class DailyFocusLogMapper @Inject constructor() {

    fun entityToDomain(entity: DailyFocusLogEntity): DailyFocusStats {
        return DailyFocusStats(
            date = DateTimeUtils.parseDate(entity.date),
            totalMinutes = entity.totalMinutes,
            sessionsCompleted = entity.sessionsCompleted
        )
    }

    fun entityListToDomainList(entities: List<DailyFocusLogEntity>): List<DailyFocusStats> {
        return entities.map { entityToDomain(it) }
    }
}
