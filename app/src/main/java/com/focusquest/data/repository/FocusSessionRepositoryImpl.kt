package com.focusquest.data.repository

import com.focusquest.data.local.dao.FocusSessionDao
import com.focusquest.data.mapper.FocusSessionMapper
import com.focusquest.domain.model.FocusSession
import com.focusquest.domain.repository.FocusSessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Concrete implementation of FocusSessionRepository.
 *
 * Wraps FocusSessionDao and converts between Room entities and domain models
 * using FocusSessionMapper.
 */
class FocusSessionRepositoryImpl @Inject constructor(
    private val dao: FocusSessionDao,
    private val mapper: FocusSessionMapper
) : FocusSessionRepository {

    override fun getRecentSessions(): Flow<List<FocusSession>> {
        return dao.getRecentSessions().map { entities ->
            mapper.entityListToDomainList(entities)
        }
    }

    override suspend fun insert(session: FocusSession): Long {
        return dao.insert(mapper.domainToEntity(session))
    }
}
