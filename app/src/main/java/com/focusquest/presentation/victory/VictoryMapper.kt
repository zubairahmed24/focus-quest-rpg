package com.focusquest.presentation.victory

import com.focusquest.domain.model.BattleResult

/**
 * Maps a [BattleResult.BossDefeated] domain result + current player level into the
 * [VictoryUiState] the screen renders.
 *
 * @param playerLevel Player level after the win.
 * @param isCampaignComplete True when this win cleared the final boss.
 */
fun BattleResult.BossDefeated.toVictoryUiState(
    playerLevel: Int,
    isCampaignComplete: Boolean,
): VictoryUiState = VictoryUiState(
    defeatedBossName = boss.name,
    xpGained = xpGained,
    playerLevel = playerLevel,
    nextBossName = nextBoss?.name,
    isCampaignComplete = isCampaignComplete,
)
