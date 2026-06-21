package com.focusquest.domain.repository

import com.focusquest.domain.model.Boss
import kotlinx.coroutines.flow.Flow

/**
 * Repository contract for boss operations.
 *
 * Lives in the domain layer. The data layer provides BossRepositoryImpl
 * which is bound via Hilt.
 */
interface BossRepository {

    /** Reactive stream of all bosses ordered by progression sequence. */
    fun getAllBosses(): Flow<List<Boss>>

    /** One-shot read of all bosses. Used for initialization and stats. */
    suspend fun getAllBossesOnce(): List<Boss>

    /** Get a specific boss by ID. Returns null if not found. */
    suspend fun getBossById(id: Int): Boss?

    /** Get the current active boss (first unlocked + not defeated). Returns null if campaign complete. */
    suspend fun getCurrentBoss(): Boss?

    /** Mark a boss as defeated by ID. */
    suspend fun markDefeated(id: Int)

    /** Unlock a boss by ID. Called when the previous boss is defeated. */
    suspend fun unlockBoss(id: Int)
}
