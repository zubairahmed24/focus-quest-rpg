package com.focusquest.data.repository

import com.focusquest.data.local.dao.PlayerStateDao
import com.focusquest.data.mapper.PlayerStateMapper
import com.focusquest.domain.model.PlayerState
import com.focusquest.domain.repository.PlayerRepository
import com.focusquest.util.DateTimeUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

/**
 * Concrete implementation of PlayerRepository.
 *
 * Wraps PlayerStateDao and converts between Room entities and domain models
 * using PlayerStateMapper. The domain layer never sees entities.
 *
 * @Inject constructor makes this Hilt-providable when bound via RepositoryModule.
 */
class PlayerRepositoryImpl @Inject constructor(
    private val dao: PlayerStateDao,
    private val mapper: PlayerStateMapper
) : PlayerRepository {

    override fun getPlayerState(): Flow<PlayerState?> {
        return dao.getPlayerState().map { entity ->
            entity?.let { mapper.entityToDomain(it) }
        }
    }

    override suspend fun getPlayerStateOnce(): PlayerState? {
        return dao.getPlayerStateOnce()?.let { mapper.entityToDomain(it) }
    }

    override suspend fun upsert(state: PlayerState) {
        dao.upsert(mapper.domainToEntity(state))
    }

    override suspend fun updateXpAndLevel(xp: Int, level: Int) {
        dao.updateXpAndLevel(xp, level)
    }

    override suspend fun updateCurrentBoss(bossId: Int, hp: Int) {
        dao.updateCurrentBoss(bossId, hp)
    }

    override suspend fun dealDamageToBoss(damage: Int) {
        dao.dealDamageToBoss(damage)
    }

    override suspend fun updateStreak(streak: Int, date: LocalDate) {
        dao.updateStreak(streak, DateTimeUtils.formatDate(date))
    }

    override suspend fun addFocusSession(minutes: Int) {
        dao.addFocusSession(minutes)
    }

    override suspend fun incrementBossesDefeated() {
        dao.incrementBossesDefeated()
    }
}
