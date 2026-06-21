package com.focusquest.domain.usecase

import com.focusquest.domain.model.Boss
import com.focusquest.domain.repository.BossRepository
import javax.inject.Inject

/**
 * Returns the current active boss — the first boss that is unlocked
 * but not yet defeated.
 *
 * Returns null if the campaign is complete (all 5 bosses defeated).
 * Used by the Battle screen to display the current enemy.
 */
class GetCurrentBossUseCase @Inject constructor(
    private val bossRepository: BossRepository
) {
    suspend operator fun invoke(): Boss? {
        return bossRepository.getCurrentBoss()
    }
}
