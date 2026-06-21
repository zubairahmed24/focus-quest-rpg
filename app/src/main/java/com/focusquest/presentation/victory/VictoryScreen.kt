package com.focusquest.presentation.victory

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.focusquest.presentation.components.FQButton
import com.focusquest.presentation.components.FQButtonSize
import com.focusquest.presentation.components.FQButtonVariant
import com.focusquest.presentation.components.FQCard
import com.focusquest.presentation.theme.FocusQuestTheme

/**
 * Victory screen \u2014 shown when a boss is defeated (and the campaign-complete summary).
 *
 * Fully design-system driven: surface from [FocusQuestTheme.colors], spacing from
 * [FocusQuestTheme.spacing], buttons via [FQButton], card via [FQCard]. The reward
 * emblem and content animate in with a bouncy scale + fade for game-feel.
 *
 * Stateless: the host supplies [state] and the action callbacks. [onTakeBreak] and
 * [onShare] are optional \u2014 buttons render only when provided (Share lands with #14).
 *
 * @param state What to celebrate.
 * @param onContinue Primary action \u2014 dismiss / proceed.
 * @param onTakeBreak Optional \u2014 start the break timer. Hidden on campaign-complete.
 * @param onShare Optional \u2014 share the win (#14). Hidden until wired.
 */
@Composable
fun VictoryScreen(
    state: VictoryUiState,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
    onTakeBreak: (() -> Unit)? = null,
    onShare: (() -> Unit)? = null,
) {
    val colors = FocusQuestTheme.colors
    val spacing = FocusQuestTheme.spacing
    val isFinale = state.isCampaignComplete || state.isCampaignSummary

    var revealed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { revealed = true }
    val emblemScale by animateFloatAsState(
        targetValue = if (revealed) 1f else 0.6f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "victoryEmblemScale"
    )
    val contentAlpha by animateFloatAsState(
        targetValue = if (revealed) 1f else 0f,
        animationSpec = tween(durationMillis = 450),
        label = "victoryContentAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(spacing.sm),
        contentAlignment = Alignment.Center
    ) {
        FQCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(contentAlpha),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.sm)
            ) {
                Text(
                    text = if (isFinale) "\uD83C\uDFC6" else "\uD83C\uDF89",
                    fontSize = 64.sp,
                    modifier = Modifier.scale(emblemScale)
                )

                if (state.isCampaignSummary) {
                    Text(
                        text = "Campaign Complete!",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.cta,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "You defeated all 5 procrastination bosses!\nMore bosses coming soon.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.textSecondary,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        text = "BOSS DEFEATED!",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.cta,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = state.defeatedBossName,
                        style = MaterialTheme.typography.headlineMedium,
                        color = colors.textPrimary,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "+${state.xpGained} XP",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.xp
                    )
                    Text(
                        text = "Level ${state.playerLevel}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.levelUp
                    )
                    if (state.isCampaignComplete) {
                        Text(
                            text = "\uD83C\uDFC6 Campaign Complete!\nAll bosses defeated!",
                            style = MaterialTheme.typography.headlineSmall,
                            color = colors.cta,
                            textAlign = TextAlign.Center
                        )
                    } else if (state.nextBossName != null) {
                        Text(
                            text = "Next boss: ${state.nextBossName}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = colors.textSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(spacing.xxs))

                FQButton(
                    text = "Continue",
                    onClick = onContinue,
                    modifier = Modifier.fillMaxWidth(),
                    variant = FQButtonVariant.Cta,
                    size = FQButtonSize.Cta
                )
                if (onTakeBreak != null && !isFinale) {
                    FQButton(
                        text = "Take a Break",
                        onClick = onTakeBreak,
                        modifier = Modifier.fillMaxWidth(),
                        variant = FQButtonVariant.Outline
                    )
                }
                if (onShare != null) {
                    FQButton(
                        text = "Share",
                        onClick = onShare,
                        modifier = Modifier.fillMaxWidth(),
                        variant = FQButtonVariant.Secondary
                    )
                }
            }
        }
    }
}
