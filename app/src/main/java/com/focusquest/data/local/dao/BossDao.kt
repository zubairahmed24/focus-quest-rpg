package com.focusquest.data.local.dao

import com.focusquest.data.local.entity.BossEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data access for the bosses table.
 * Provides observable queries for boss list and progression state.
 */
@androidx.room.Dao
interface BossDao {

    @androidx.room.Query("SELECT * FROM bosses ORDER BY `order` ASC")
    fun getAllBosses(): Flow<List<BossEntity>>

    @androidx.room.Query("SELECT * FROM bosses ORDER BY `order` ASC")
    suspend fun getAllBossesOnce(): List<BossEntity>

    @androidx.room.Query("SELECT * FROM bosses WHERE id = :id")
    suspend fun getBossById(id: Int): BossEntity?

    @androidx.room.Query("SELECT * FROM bosses WHERE isUnlocked = 1 AND isDefeated = 0 ORDER BY `order` ASC LIMIT 1")
    suspend fun getCurrentBoss(): BossEntity?

    @androidx.room.Query("UPDATE bosses SET isDefeated = 1 WHERE id = :id")
    suspend fun markDefeated(id: Int)

    @androidx.room.Query("UPDATE bosses SET isUnlocked = 1 WHERE id = :id")
    suspend fun unlockBoss(id: Int)

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertAll(bosses: List<BossEntity>)
}
