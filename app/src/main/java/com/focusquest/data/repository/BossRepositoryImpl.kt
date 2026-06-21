package com.focusquest.data.repository

import com.focusquest.data.local.dao.BossDao
import com.focusquest.data.mapper.BossMapper
import com.focusquest.domain.model.Boss
import com.focusquest.domain.repository.BossRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Concrete implementation of BossRepository.
 *
 * Wraps BossDao and converts between Room entities and domain models
 * using BossMapper. The domain layer never sees entities.
 */
class BossRepositoryImpl @Inject constructor(
    private val dao: BossDao,
    private val mapper: BossMapper
) : BossRepository {

    override fun getAllBosses(): Flow<List<Boss>> {
        return dao.getAllBosses().map { entities ->
            mapper.entityListToDomainList(entities)
        }
    }

    override suspend fun getAllBossesOnce(): List<Boss> {
        return mapper.entityListToDomainList(dao.getAllBossesOnce())
    }

    override suspend fun getBossById(id: Int): Boss? {
        return dao.getBossById(id)?.let { mapper.entityToDomain(it) }
    }

    override suspend fun getCurrentBoss(): Boss? {
        return dao.getCurrentBoss()?.let { mapper.entityToDomain(it) }
    }

    override suspend fun markDefeated(id: Int) {
        dao.markDefeated(id)
    }

    override suspend fun unlockBoss(id: Int) {
        dao.unlockBoss(id)
    }
}
