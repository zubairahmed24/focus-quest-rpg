package com.focusquest.data.local.dao

import com.focusquest.data.local.entity.FocusSessionEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data access for the focus_sessions table.
 * Stores individual session records for history and analytics.
 */
@androidx.room.Dao
interface FocusSessionDao {

    @androidx.room.Query("SELECT * FROM focus_sessions ORDER BY startTime DESC LIMIT 50")
    fun getRecentSessions(): Flow<List<FocusSessionEntity>>

    @androidx.room.Insert
    suspend fun insert(session: FocusSessionEntity): Long
}
