package com.focusquest.data.mapper

import com.focusquest.data.local.entity.FocusSessionEntity
import com.focusquest.domain.model.FocusSession
import com.focusquest.util.DateTimeUtils
import javax.inject.Inject

/**
 * Converts between FocusSessionEntity (Room, ISO datetime strings)
 * and FocusSession (domain, LocalDateTime).
 *
 * Lives in the data layer because it depends on the entity
 * and knows about the storage format.
 */
class FocusSessionMapper @Inject constructor() {

    fun entityToDomain(entity: FocusSessionEntity): FocusSession {
        return FocusSession(
            id = entity.id,
            startTime = DateTimeUtils.parseDateTime(entity.startTime),
            durationMinutes = entity.durationMinutes,
            completed = entity.completed,
            bossId = entity.bossId,
            damageDealt = entity.damageDealt
        )
    }

    fun domainToEntity(domain: FocusSession): FocusSessionEntity {
        return FocusSessionEntity(
            id = domain.id,
            startTime = DateTimeUtils.formatDateTime(domain.startTime),
            durationMinutes = domain.durationMinutes,
            completed = domain.completed,
            bossId = domain.bossId,
            damageDealt = domain.damageDealt
        )
    }

    fun entityListToDomainList(entities: List<FocusSessionEntity>): List<FocusSession> {
        return entities.map { entityToDomain(it) }
    }
}
