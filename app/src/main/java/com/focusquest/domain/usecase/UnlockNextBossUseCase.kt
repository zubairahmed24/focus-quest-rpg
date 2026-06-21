package com.focusquest.domain.usecase

import com.focusquest.domain.repository.BossRepository
import com.focusquest.domain.repository.PlayerRepository
import com.focusquest.util.Constants
import javax.inject.Inject

/**
 * Unlocks the next boss in the progression sequence.
 *
 * Called after a boss is defeated. Finds the next boss by order and:
 * 1. Unlocks it in the bosses table.
 * 2. Updates the player's currentBossId to the new boss.
 * 3. Sets the player's currentBossHpRemaining to the new boss's max HP.
 *
 * If there is no next boss (campaign complete), does nothing —
 * the caller should handle the campaign-complete state separately.
 */
class UnlockNextBossUseCase @Inject constructor(
    private val bossRepository: BossRepository,
    private val playerRepository: PlayerRepository
) {
    suspend operator fun invoke(defeatedBossId: Int) {
        val allBosses = bossRepository.getAllBossesOnce()
        val defeatedBoss = allBosses.find { it.id == defeatedBossId }

        if (defeatedBoss == null) return

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
}
