package com.focusquest.domain.usecase

import com.focusquest.domain.model.BattleResult
import com.focusquest.domain.model.Boss
import com.focusquest.domain.repository.BossRepository
import com.focusquest.domain.repository.FocusSessionRepository
import com.focusquest.domain.repository.PlayerRepository
import com.focusquest.util.Constants
import com.focusquest.util.DateTimeUtils
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * The core business logic use case — handles everything that happens
 * when a focus session completes successfully.
 *
 * Orchestrates the following operations in sequence:
 *
 * 1. STREAK UPDATE
 *    - If lastFocusDate is yesterday → streak += 1
 *    - If lastFocusDate is today → streak unchanged (same-day session)
 *    - If lastFocusDate is >1 day ago → streak = 1 (reset)
 *    - If this is the first session ever (streak == 0) → streak = 1
 *
 * 2. BOSS DAMAGE
 *    - Deal DAMAGE_PER_SESSION (100) damage to the current boss.
    *    - If boss HP drops to 0 or below → boss is defeated.
 *
 * 3. BOSS DEFEAT (if applicable)
    *    - Mark boss as defeated in the bosses table.
 *    - Increment totalBossesDefeated counter.
 *    - Unlock the next boss (if one exists).
 *    - If no next boss → campaign is complete.
 *
 * 4. XP AWARD
 *    - Add XP_PER_SESSION (100) XP.
 *    - If XP >= XP_PER_LEVEL (300) → level up, XP -= 300.
 *    - Handle multiple level-ups.
 *
 * 5. SESSION RECORD
 *    - Insert a FocusSession record into focus_sessions table.
 *
 * 6. DAILY LOG UPDATE
 *    - Add focus minutes to today's daily_focus_log entry.
 *
 * 7. PLAYER STATE UPDATE
 *    - Add focus minutes to totalFocusMinutes.
 *    - Increment totalSessionsCompleted.
 *
 * 8. BATTLE RESULT
 *    - Return BattleResult.DamageDealt if boss survived.
 *    - Return BattleResult.BossDefeated if boss was defeated.
 *
 * All database mutations are performed through repositories.
 * The use case contains NO Android framework imports.
 */
class CompleteFocusSessionUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val bossRepository: BossRepository,
    private val focusSessionRepository: FocusSessionRepository
) {

    suspend operator fun invoke(): BattleResult {
        val player = playerRepository.getPlayerStateOnce()
            ?: throw IllegalStateException("Player state not found — database may not be seeded")

        val currentBoss = bossRepository.getBossById(player.currentBossId)
            ?: throw IllegalStateException("Current boss not found — invalid bossId: ${player.currentBossId}")

        val today = LocalDate.now()
        val sessionStart = LocalDateTime.now().minusMinutes(Constants.FOCUS_DURATION_MINUTES.toLong())

        // ── 1. STREAK UPDATE ──────────────────────────────────────────
        val newStreak = calculateNewStreak(
            currentStreak = player.streak,
            lastFocusDate = player.lastFocusDate,
            today = today
        )
        playerRepository.updateStreak(streak = newStreak, date = today)

        // ── 2. BOSS DAMAGE ────────────────────────────────────────────
        val damage = Constants.DAMAGE_PER_SESSION
        val bossHpAfterDamage = (player.currentBossHpRemaining - damage).coerceAtLeast(0)

        // ── 3. BOSS DEFEAT CHECK ─────────────────────────────────────
        val isBossDefeated = bossHpAfterDamage == 0

        if (isBossDefeated) {
            bossRepository.markDefeated(currentBoss.id)
            playerRepository.incrementBossesDefeated()
        }

        // ── 4. XP AWARD + LEVEL PROGRESSION ──────────────────────────
        val xpAwarded = Constants.XP_PER_SESSION
        var newXp = player.xp + xpAwarded
        var newLevel = player.level

        while (newXp >= Constants.XP_PER_LEVEL) {
            newXp -= Constants.XP_PER_LEVEL
            newLevel += 1
        }
        playerRepository.updateXpAndLevel(xp = newXp, level = newLevel)

        // ── 5. SESSION RECORD ────────────────────────────────────────
        focusSessionRepository.insert(
            com.focusquest.domain.model.FocusSession(
                startTime = sessionStart,
                durationMinutes = Constants.FOCUS_DURATION_MINUTES,
                completed = true,
                bossId = currentBoss.id,
                damageDealt = damage
            )
        )

        // ── 6. PLAYER STATE: FOCUS MINUTES + SESSION COUNT ───────────
        playerRepository.addFocusSession(Constants.FOCUS_DURATION_MINUTES)

        // ── 7. BOSS UNLOCK (if defeated) ─────────────────────────────
        if (isBossDefeated) {
            unlockNextBoss(currentBoss)
        } else {
            // Boss survived — update remaining HP
            playerRepository.updateCurrentBoss(
                bossId = currentBoss.id,
                hp = bossHpAfterDamage
            )
        }

        // ── 8. BUILD BATTLE RESULT ───────────────────────────────────
        return if (isBossDefeated) {
            val nextBoss = findNextBoss(currentBoss)
            BattleResult.BossDefeated(
                boss = currentBoss,
                xpGained = xpAwarded,
                nextBoss = nextBoss
            )
        } else {
            BattleResult.DamageDealt(
                damage = damage,
                bossHpRemaining = bossHpAfterDamage
            )
        }
    }

    /**
     * Calculates the new streak based on the last focus date.
     *
     * - First session ever (streak == 0): streak = 1
     * - Last focus was yesterday: streak += 1
     * - Last focus is today (same-day): streak unchanged
     * - Gap of >1 day: streak = 1 (reset)
     */
    private fun calculateNewStreak(
        currentStreak: Int,
        lastFocusDate: LocalDate,
        today: LocalDate
    ): Int {
        return when {
            // First session ever
            currentStreak == 0 -> 1

            // Already focused today — no change
            lastFocusDate == today -> currentStreak

            // Focused yesterday — increment streak
            DateTimeUtils.isYesterday(lastFocusDate, today) -> currentStreak + 1

            // Gap > 1 day — reset streak to 1
            else -> 1
        }
    }

    /**
     * Finds and unlocks the next boss in the progression sequence.
     * Updates player's currentBossId and currentBossHpRemaining.
     */
    private suspend fun unlockNextBoss(defeatedBoss: Boss) {
        val allBosses = bossRepository.getAllBossesOnce()
        val nextBoss = allBosses.find { it.order == defeatedBoss.order + 1 }

        if (nextBoss != null) {
            bossRepository.unlockBoss(nextBoss.id)
            playerRepository.updateCurrentBoss(
                bossId = nextBoss.id,
                hp = nextBoss.maxHp
            )
        }
        // If nextBoss is null, campaign is complete. Caller handles this.
    }

    /**
     * Returns the next boss in sequence without unlocking it.
     * Used to build the BattleResult for the Victory screen.
     */
    private suspend fun findNextBoss(currentBoss: Boss): Boss? {
        val allBosses = bossRepository.getAllBossesOnce()
        return allBosses.find { it.order == currentBoss.order + 1 }
    }
}
