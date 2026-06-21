package com.focusquest.domain.repository

import com.focusquest.domain.model.FocusSession
import kotlinx.coroutines.flow.Flow

/**
 * Repository contract for focus session operations.
 *
 * Lives in the domain layer. The data layer provides FocusSessionRepositoryImpl
 * which is bound via Hilt.
 */
interface FocusSessionRepository {

    /** Reactive stream of the 50 most recent focus sessions, newest first. */
    fun getRecentSessions(): Flow<List<FocusSession>>

    /** Insert a new focus session record. Returns the generated row ID. */
    suspend fun insert(session: FocusSession): Long
}
