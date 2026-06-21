package com.focusquest.domain.repository

import com.focusquest.domain.model.PlayerState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Repository contract for player state operations.
 *
 * Lives in the domain layer so use cases depend on this interface,
 * not the concrete implementation. The data layer provides
 * PlayerRepositoryImpl which is bound via Hilt.
 *
 * Observable queries return Flow for reactive UI updates.
 * One-shot operations use suspend functions.
 */
interface PlayerRepository {

    /** Reactive stream of the singleton player state. Emits null until seeded. */
    fun getPlayerState(): Flow<PlayerState?>

    /** One-shot read of the current player state. Returns null if not yet seeded. */
    suspend fun getPlayerStateOnce(): PlayerState?

    /** Persist the entire player state. Used when multiple fields change atomically. */
    suspend fun upsert(state: PlayerState)

    /** Update only XP and level. Used by AwardXpUseCase. */
    suspend fun updateXpAndLevel(xp: Int, level: Int)

    /** Update the current boss ID and remaining HP. Used after boss defeat/unlock. */
    suspend fun updateCurrentBoss(bossId: Int, hp: Int)

    /** Subtract damage from the current boss HP. Clamped to 0 by the use case. */
    suspend fun dealDamageToBoss(damage: Int)

    /** Update the streak counter and last focus date. */
    suspend fun updateStreak(streak: Int, date: LocalDate)

    /** Add focus minutes and increment session count atomically. */
    suspend fun addFocusSession(minutes: Int)

    /** Increment the total bosses defeated counter by 1. */
    suspend fun incrementBossesDefeated()
}
