package com.focusquest.domain.usecase

import com.focusquest.domain.repository.PlayerRepository
import com.focusquest.util.Constants
import javax.inject.Inject

/**
 * Awards XP to the player and handles level progression.
 *
 * Business rules:
 * - Each completed focus session awards XP_PER_SESSION (100) XP.
 * - When accumulated XP reaches XP_PER_LEVEL (300), the player levels up:
 *   level += 1, xp -= XP_PER_LEVEL.
 * - Multiple level-ups can occur in a single session if XP > 2 * XP_PER_LEVEL.
 * - The new XP and level are persisted to the player_state table.
 *
 * Returns the new XP value and level after applying the award.
 */
class AwardXpUseCase @Inject constructor(
    private val playerRepository: PlayerRepository
) {
    /**
     * @return Pair(newXp, newLevel) after awarding XP.
     */
    suspend operator fun invoke(): Pair<Int, Int> {
        val player = playerRepository.getPlayerStateOnce()
            ?: return Pair(0, 1)

        var xp = player.xp + Constants.XP_PER_SESSION
        var level = player.level

        // Handle multiple level-ups in a single award
        while (xp >= Constants.XP_PER_LEVEL) {
            xp -= Constants.XP_PER_LEVEL
            level += 1
        }

        playerRepository.updateXpAndLevel(xp = xp, level = level)

        return Pair(xp, level)
    }
}
