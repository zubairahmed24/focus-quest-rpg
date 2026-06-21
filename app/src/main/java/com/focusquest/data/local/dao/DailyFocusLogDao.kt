package com.focusquest.data.local.dao

import com.focusquest.data.local.entity.DailyFocusLogEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data access for the daily_focus_log table.
 * Used to render the weekly bar chart on the Stats screen.
 */
@androidx.room.Dao
interface DailyFocusLogDao {

    @androidx.room.Query("SELECT * FROM daily_focus_log WHERE date >= :startDate ORDER BY date ASC")
    fun getLogsSince(startDate: String): Flow<List<DailyFocusLogEntity>>

    @androidx.room.Query("SELECT * FROM daily_focus_log WHERE date = :date")
    suspend fun getLogForDate(date: String): DailyFocusLogEntity?

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun upsert(log: DailyFocusLogEntity)

    @androidx.room.Query("UPDATE daily_focus_log SET totalMinutes = totalMinutes + :minutes, sessionsCompleted = sessionsCompleted + 1 WHERE date = :date")
    suspend fun addToDate(date: String, minutes: Int)
}
