package com.focusquest.presentation.victory

/**
 * Immutable UI state for the Victory screen.
 *
 * Built by the caller from a [com.focusquest.domain.model.BattleResult.BossDefeated]
 * (see [com.focusquest.presentation.victory.toVictoryUiState]) plus the current
 * player level. Pure data \u2014 the screen renders this and nothing else.
 *
 * @param defeatedBossName Name of the boss just defeated (blank for a pure campaign summary).
 * @param xpGained XP awarded for the completed session.
 * @param playerLevel Player level after the win.
 * @param nextBossName Name of the next boss, or null if there is none.
 * @param isCampaignComplete True when this win cleared the final boss.
 * @param isCampaignSummary True for the standalone "campaign already complete" screen
 *        (no fresh defeat to celebrate).
 */
data class VictoryUiState(
    val defeatedBossName: String,
    val xpGained: Int,
    val playerLevel: Int,
    val nextBossName: String?,
    val isCampaignComplete: Boolean,
    val isCampaignSummary: Boolean = false,
)
