package com.focusquest.presentation.battle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.focusquest.domain.model.BattleResult
import com.focusquest.domain.model.FocusTimerState
import com.focusquest.presentation.battle.components.BossSprite
import com.focusquest.presentation.battle.components.HpBar
import com.focusquest.presentation.battle.components.TauntBubble
import com.focusquest.presentation.battle.components.TimerDisplay
import com.focusquest.presentation.components.ConfirmDialog
import com.focusquest.presentation.components.FQButton
import com.focusquest.presentation.components.FQButtonSize
import com.focusquest.presentation.components.FQButtonVariant
import com.focusquest.presentation.components.FQCard
import com.focusquest.presentation.theme.FocusQuestTheme
import com.focusquest.presentation.victory.VictoryScreen
import com.focusquest.presentation.victory.VictoryUiState
import com.focusquest.presentation.victory.toVictoryUiState

/**
 * Battle Screen — the main game screen.
 *
 * Layout (top to bottom):
 * 1. Header: Player level, XP bar, streak
 * 2. Boss section: Boss sprite, name, taunt, HP bar
 * 3. Timer section: Circular timer display
 * 4. Action buttons: Start/Pause/Resume/Give Up
 *
 * On a boss defeat (or a returning campaign-complete state) the screen delegates to
 * [VictoryScreen] — the victory UI is no longer inlined here (#12).
 *
 * Styling is fully design-system driven: colors via [FocusQuestTheme.colors],
 * spacing via [FocusQuestTheme.spacing], buttons/cards via the FQ* components.
 *
 * @param viewModel Injected Hilt ViewModel
 */
@Composable
fun BattleScreen(
    viewModel: BattleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BattleScreenContent(
        state = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
private fun BattleScreenContent(
    state: BattleUiState,
    onAction: (BattleUiAction) -> Unit
) {
    val spacing = FocusQuestTheme.spacing

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.headlineMedium,
                color = FocusQuestTheme.colors.textPrimary
            )
        }
        return
    }

    var showShareDialog by remember { mutableStateOf(false) }

    if (showShareDialog) {
        com.focusquest.presentation.components.ShareDialog(
            playerLevel = state.playerLevel,
            streak = state.streak,
            totalFocusTimeFormatted = com.focusquest.util.DateTimeUtils.formatFocusTime(state.totalFocusMinutes),
            bossesDefeatedCount = state.totalBossesDefeated,
            totalBossesCount = 5,
            onDismiss = { showShareDialog = false }
        )
    }

    // Victory — boss defeated. Delegates to the dedicated VictoryScreen (#12).
    val result = state.lastBattleResult
    if (state.showVictory && result is BattleResult.BossDefeated) {
        VictoryScreen(
            state = result.toVictoryUiState(
                playerLevel = state.playerLevel,
                isCampaignComplete = state.campaignComplete
            ),
            onContinue = { onAction(BattleUiAction.DismissVictory) },
            onTakeBreak = { onAction(BattleUiAction.StartBreak) },
            onShare = { showShareDialog = true }
        )
        return
    }

    // Returning to a campaign that is already fully complete (no fresh defeat).
    if (state.campaignComplete && !state.showVictory) {
        VictoryScreen(
            state = VictoryUiState(
                defeatedBossName = "",
                xpGained = 0,
                playerLevel = state.playerLevel,
                nextBossName = null,
                isCampaignComplete = true,
                isCampaignSummary = true
            ),
            onContinue = { onAction(BattleUiAction.DismissVictory) },
            onShare = { showShareDialog = true }
        )
        return
    }

    // Give Up confirmation dialog
    if (state.showGiveUpDialog) {
        ConfirmDialog(
            title = "Give Up?",
            message = "Your progress in this focus session will be lost. The boss will not take damage.",
            confirmText = "Give Up",
            dismissText = "Keep Focusing",
            onConfirm = { onAction(BattleUiAction.ConfirmGiveUp) },
            onDismiss = { onAction(BattleUiAction.DismissGiveUpDialog) }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.sm)
    ) {
        // ── 1. HEADER: Level, XP, Streak ──────────────────────────
        PlayerHeader(
            level = state.playerLevel,
            xp = state.playerXp,
            xpToNextLevel = state.xpToNextLevel,
            xpProgress = state.xpProgress,
            streak = state.streak
        )

        Spacer(modifier = Modifier.height(spacing.xxs))

        // ── 2. BOSS SECTION ───────────────────────────────────────
        val boss = state.currentBoss
        if (boss != null) {
            BossSection(
                bossName = boss.name,
                bossTaunt = boss.taunt,
                bossHp = state.bossHpRemaining,
                bossMaxHp = state.bossMaxHp
            )
        } else {
            Text(
                text = "All bosses defeated!",
                style = MaterialTheme.typography.headlineMedium,
                color = FocusQuestTheme.colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(spacing.sm))

        // ── 3. TIMER SECTION ──────────────────────────────────────
        TimerSection(
            timerState = state.timerState,
            remainingSeconds = state.remainingSeconds,
            totalSeconds = state.totalSeconds,
            isFocusing = state.isFocusing
        )

        Spacer(modifier = Modifier.height(spacing.sm))

        // ── 4. ACTION BUTTONS ─────────────────────────────────────
        ActionButtons(
            timerState = state.timerState,
            onAction = onAction
        )
    }
}

/**
 * Player header with level, XP progress bar, and streak.
 */
@Composable
private fun PlayerHeader(
    level: Int,
    xp: Int,
    xpToNextLevel: Int,
    xpProgress: Float,
    streak: Int
) {
    val colors = FocusQuestTheme.colors
    FQCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Level $level",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = colors.cta
            )
            if (streak > 0) {
                Text(
                    text = "$streak 🔥",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.streak
                )
            }
        }
        Spacer(modifier = Modifier.height(FocusQuestTheme.spacing.xxs))
        LinearProgressIndicator(
            progress = { xpProgress },
            modifier = Modifier.fillMaxWidth(),
            color = colors.xp,
            trackColor = colors.surfaceElevated
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "XP: $xp / $xpToNextLevel",
            style = MaterialTheme.typography.labelMedium,
            color = colors.textSecondary
        )
    }
}

/**
 * Boss section with sprite, name, taunt, and HP bar.
 */
@Composable
private fun BossSection(
    bossName: String,
    bossTaunt: String,
    bossHp: Int,
    bossMaxHp: Int
) {
    val spacing = FocusQuestTheme.spacing
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TauntBubble(
            taunt = bossTaunt,
            modifier = Modifier.padding(bottom = spacing.xxs)
        )
        BossSprite(
            bossName = bossName,
            modifier = Modifier.padding(vertical = spacing.xxs)
        )
        Text(
            text = bossName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = FocusQuestTheme.colors.textPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(spacing.xs))
        HpBar(
            currentHp = bossHp,
            maxHp = bossMaxHp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Timer section with circular display.
 */
@Composable
private fun TimerSection(
    timerState: FocusTimerState,
    remainingSeconds: Int,
    totalSeconds: Int,
    isFocusing: Boolean
) {
    if (timerState is FocusTimerState.Idle) {
        Text(
            text = "Ready to focus?",
            style = MaterialTheme.typography.headlineSmall,
            color = FocusQuestTheme.colors.textSecondary,
            textAlign = TextAlign.Center
        )
    } else {
        TimerDisplay(
            remainingSeconds = remainingSeconds,
            totalSeconds = totalSeconds,
            isFocusing = isFocusing
        )
    }
}

/**
 * Action buttons that change based on timer state.
 */
@Composable
private fun ActionButtons(
    timerState: FocusTimerState,
    onAction: (BattleUiAction) -> Unit
) {
    val spacing = FocusQuestTheme.spacing
    when (timerState) {
        is FocusTimerState.Idle -> {
            FQButton(
                text = "⚔️ START FOCUSING",
                onClick = { onAction(BattleUiAction.StartFocus) },
                modifier = Modifier.fillMaxWidth(),
                variant = FQButtonVariant.Cta,
                size = FQButtonSize.Cta
            )
        }

        is FocusTimerState.Focusing -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.xs)
            ) {
                FQButton(
                    text = "Pause",
                    onClick = { onAction(BattleUiAction.Pause) },
                    modifier = Modifier.weight(1f),
                    variant = FQButtonVariant.Outline
                )
                FQButton(
                    text = "Give Up",
                    onClick = { onAction(BattleUiAction.GiveUp) },
                    modifier = Modifier.weight(1f),
                    variant = FQButtonVariant.Danger
                )
            }
        }

        is FocusTimerState.Break -> {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacing.xxs)
            ) {
                Text(
                    text = "Break time! 🧘",
                    style = MaterialTheme.typography.titleMedium,
                    color = FocusQuestTheme.colors.textSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                FQButton(
                    text = "Skip Break",
                    onClick = { onAction(BattleUiAction.SkipBreak) },
                    modifier = Modifier.fillMaxWidth(),
                    variant = FQButtonVariant.Outline
                )
            }
        }

        is FocusTimerState.Paused -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.xs)
            ) {
                FQButton(
                    text = "Resume",
                    onClick = { onAction(BattleUiAction.Resume) },
                    modifier = Modifier.weight(1f),
                    variant = FQButtonVariant.Cta
                )
                FQButton(
                    text = "Give Up",
                    onClick = { onAction(BattleUiAction.GiveUp) },
                    modifier = Modifier.weight(1f),
                    variant = FQButtonVariant.Danger
                )
            }
        }
    }
}
