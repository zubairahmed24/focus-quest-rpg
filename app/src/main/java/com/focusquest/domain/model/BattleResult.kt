package com.focusquest.domain.model

/**
 * Represents the outcome of completing a focus session.
 *
 * - DamageDealt: boss took damage but is still alive.
 * - BossDefeated: boss HP reached 0, boss is defeated, next boss may be unlocked.
 *
 * Pure Kotlin — no Android dependencies.
 */
sealed class BattleResult {

    data class DamageDealt(
        val damage: Int,
        val bossHpRemaining: Int
    ) : BattleResult()

    data class BossDefeated(
        val boss: Boss,
        val xpGained: Int,
        val nextBoss: Boss?
    ) : BattleResult()
}
