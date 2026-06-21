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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.focusquest.domain.model.FocusTimerState
import com.focusquest.presentation.battle.components.BossSprite
import com.focusquest.presentation.battle.components.HpBar
import com.focusquest.presentation.battle.components.TauntBubble
import com.focusquest.presentation.battle.components.TimerDisplay
import com.focusquest.presentation.components.ConfirmDialog
import com.focusquest.presentation.theme.Gold
import com.focusquest.presentation.theme.StreakOrange

/**
 * Battle Screen — the main game screen.
 *
 * Layout (top to bottom):
 * 1. Header: Player level, XP bar, streak
 * 2. Boss section: Boss sprite, name, taunt, HP bar
 * 3. Timer section: Circular timer display
 * 4. Action buttons: Start/Pause/Resume/Give Up
 *
 * All state is observed from BattleViewModel via StateFlow.
 * No business logic exists in this composable — it only renders state
 * and dispatches actions.
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
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        return
    }

    // Show victory overlay if boss was defeated
    if (state.showVictory && state.lastBattleResult != null) {
        VictoryOverlay(
            result = state.lastBattleResult,
            campaignComplete = state.campaignComplete,
            onDismiss = { onAction(BattleUiAction.DismissVictory) },
            onStartBreak = { onAction(BattleUiAction.StartBreak) }
        )
        return
    }

    // Show campaign complete overlay
    if (state.campaignComplete && !state.showVictory) {
        CampaignCompleteOverlay(
            onDismiss = { onAction(BattleUiAction.DismissVictory) }
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── 1. HEADER: Level, XP, Streak ──────────────────────────
        PlayerHeader(
            level = state.playerLevel,
            xp = state.playerXp,
            xpToNextLevel = state.xpToNextLevel,
            xpProgress = state.xpProgress,
            streak = state.streak
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ── 2. BOSS SECTION ────────────────────────────────────────
        val boss = state.currentBoss
        if (boss != null) {
            BossSection(
                bossName = boss.name,
                bossTaunt = boss.taunt,
                bossHp = state.bossHpRemaining,
                bossMaxHp = state.bossMaxHp
            )
        } else {
            // No current boss — campaign should be complete
            Text(
                text = "All bosses defeated!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── 3. TIMER SECTION ───────────────────────────────────────
        TimerSection(
            timerState = state.timerState,
            remainingSeconds = state.remainingSeconds,
            totalSeconds = state.totalSeconds,
            isFocusing = state.isFocusing
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ── 4. ACTION BUTTONS ──────────────────────────────────────
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Level $level",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Gold
                )
                if (streak > 0) {
                    Text(
                        text = "$streak 🔥",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = StreakOrange
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { xpProgress },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.tertiary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "XP: $xp / $xpToNextLevel",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
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
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TauntBubble(
            taunt = bossTaunt,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        BossSprite(
            bossName = bossName,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = bossName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
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
            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
    when (timerState) {
        is FocusTimerState.Idle -> {
            Button(
                onClick = { onAction(BattleUiAction.StartFocus) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Gold,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "⚔️ START FOCUSING",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        is FocusTimerState.Focusing -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onAction(BattleUiAction.Pause) },
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Text("Pause", fontWeight = FontWeight.Medium)
                }
                OutlinedButton(
                    onClick = { onAction(BattleUiAction.GiveUp) },
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Give Up", fontWeight = FontWeight.Medium)
                }
            }
        }

        is FocusTimerState.Break -> {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Break time! 🧘",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedButton(
                    onClick = { onAction(BattleUiAction.SkipBreak) },
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Skip Break")
                }
            }
        }

        is FocusTimerState.Paused -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { onAction(BattleUiAction.Resume) },
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Gold,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Resume", fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = { onAction(BattleUiAction.GiveUp) },
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Give Up", fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

/**
 * Inline victory overlay shown when a boss is defeated.
 * In M4, this will be replaced with a full VictoryScreen navigation.
 */
@Composable
private fun VictoryOverlay(
    result: com.focusquest.domain.model.BattleResult.BossDefeated,
    campaignComplete: Boolean,
    onDismiss: () -> Unit,
    onStartBreak: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "🎉 BOSS DEFEATED!",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = Gold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = result.boss.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "+${result.xpGained} XP",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.tertiary
                )
                if (campaignComplete) {
                    Text(
                        text = "🏆 Campaign Complete!\nAll bosses defeated!",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Gold,
                        textAlign = TextAlign.Center
                    )
                } else if (result.nextBoss != null) {
                    Text(
                        text = "Next boss: ${result.nextBoss.name}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Gold,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Continue")
                }
                OutlinedButton(
                    onClick = onStartBreak,
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Take a Break")
                }
            }
        }
    }
}

/**
 * Campaign complete overlay shown when all 5 bosses are defeated.
 */
@Composable
private fun CampaignCompleteOverlay(
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "🏆",
                    fontSize = 64.sp
                )
                Text(
                    text = "Campaign Complete!",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = Gold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "You defeated all 5 procrastination bosses!\nMore bosses coming soon.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Gold,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Continue")
                }
            }
        }
    }
}
