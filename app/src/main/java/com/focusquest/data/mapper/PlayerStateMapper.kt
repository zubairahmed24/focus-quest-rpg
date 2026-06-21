package com.focusquest.data.mapper

import com.focusquest.data.local.entity.PlayerStateEntity
import com.focusquest.domain.model.PlayerState
import com.focusquest.util.DateTimeUtils
import javax.inject.Inject

/**
 * Converts between PlayerStateEntity (Room, ISO string dates)
 * and PlayerState (domain, LocalDate).
 *
 * This mapper lives in the data layer because it depends on the entity
 * and knows about the storage format. The domain layer never sees entities.
 */
class PlayerStateMapper @Inject constructor() {

    fun entityToDomain(entity: PlayerStateEntity): PlayerState {
        return PlayerState(
            id = entity.id,
            level = entity.level,
            xp = entity.xp,
            currentBossId = entity.currentBossId,
            currentBossHpRemaining = entity.currentBossHpRemaining,
            streak = entity.streak,
            lastFocusDate = DateTimeUtils.parseDate(entity.lastFocusDate),
            totalFocusMinutes = entity.totalFocusMinutes,
            totalSessionsCompleted = entity.totalSessionsCompleted,
            totalBossesDefeated = entity.totalBossesDefeated
        )
    }

    fun domainToEntity(domain: PlayerState): PlayerStateEntity {
        return PlayerStateEntity(
            id = domain.id,
            level = domain.level,
            xp = domain.xp,
            currentBossId = domain.currentBossId,
            currentBossHpRemaining = domain.currentBossHpRemaining,
            streak = domain.streak,
            lastFocusDate = DateTimeUtils.formatDate(domain.lastFocusDate),
            totalFocusMinutes = domain.totalFocusMinutes,
            totalSessionsCompleted = domain.totalSessionsCompleted,
            totalBossesDefeated = domain.totalBossesDefeated
        )
    }
}
