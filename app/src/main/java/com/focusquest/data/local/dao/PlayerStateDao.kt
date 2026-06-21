package com.focusquest.data.local.dao

import com.focusquest.data.local.entity.PlayerStateEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data access for the singleton player_state row.
 *
 * Observable queries return Flow for reactive UI updates.
 * One-shot mutations use suspend functions.
 */
@androidx.room.Dao
interface PlayerStateDao {

    @androidx.room.Query("SELECT * FROM player_state WHERE id = 1")
    fun getPlayerState(): Flow<PlayerStateEntity?>

    @androidx.room.Query("SELECT * FROM player_state WHERE id = 1")
    suspend fun getPlayerStateOnce(): PlayerStateEntity?

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun upsert(state: PlayerStateEntity)

    @androidx.room.Query("UPDATE player_state SET xp = :xp, level = :level WHERE id = 1")
    suspend fun updateXpAndLevel(xp: Int, level: Int)

    @androidx.room.Query("UPDATE player_state SET currentBossId = :bossId, currentBossHpRemaining = :hp WHERE id = 1")
    suspend fun updateCurrentBoss(bossId: Int, hp: Int)

    @androidx.room.Query("UPDATE player_state SET currentBossHpRemaining = currentBossHpRemaining - :damage WHERE id = 1")
    suspend fun dealDamageToBoss(damage: Int)

    @androidx.room.Query("UPDATE player_state SET streak = :streak, lastFocusDate = :date WHERE id = 1")
    suspend fun updateStreak(streak: Int, date: String)

    @androidx.room.Query("UPDATE player_state SET totalFocusMinutes = totalFocusMinutes + :minutes, totalSessionsCompleted = totalSessionsCompleted + 1 WHERE id = 1")
    suspend fun addFocusSession(minutes: Int)

    @androidx.room.Query("UPDATE player_state SET totalBossesDefeated = totalBossesDefeated + 1 WHERE id = 1")
    suspend fun incrementBossesDefeated()

    /**
     * Sets or clears the active focus session start time.
     * Pass null to clear (session completed or abandoned).
     * Used by FocusTimerService for timestamp-based recovery. See ADR-001.
     */
    @androidx.room.Query("UPDATE player_state SET activeSessionStartTime = :time WHERE id = 1")
    suspend fun updateSessionStartTime(time: String?)
}
