package com.focusquest.presentation.stats

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.focusquest.presentation.components.FQCard
import com.focusquest.presentation.stats.components.BossProgressList
import com.focusquest.presentation.stats.components.WeeklyFocusChart
import com.focusquest.presentation.theme.FocusQuestTheme

@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    StatsScreenContent(state = state)
}

@Composable
fun StatsScreenContent(
    state: StatsUiState,
    modifier: Modifier = Modifier
) {
    val c = FocusQuestTheme.colors

    if (state.isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(c.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = c.brand)
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(c.background)
                .verticalScroll(rememberScrollState())
                .padding(FocusQuestTheme.spacing.sm),
            verticalArrangement = Arrangement.spacedBy(FocusQuestTheme.spacing.md)
        ) {
            // Header
            Text(
                text = "Stats Dashboard",
                style = FocusQuestTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = c.textPrimary,
                modifier = Modifier.padding(top = FocusQuestTheme.spacing.xs)
            )

            // Stat Cards Grid (2x2)
            Column(
                verticalArrangement = Arrangement.spacedBy(FocusQuestTheme.spacing.sm)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(FocusQuestTheme.spacing.sm)
                ) {
                    StatCard(
                        title = "Level",
                        value = state.playerLevel.toString(),
                        icon = Icons.Default.Star,
                        iconColor = c.cta,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Focus Time",
                        value = state.totalFocusTimeFormatted,
                        icon = Icons.Default.PlayArrow,
                        iconColor = c.brand,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(FocusQuestTheme.spacing.sm)
                ) {
                    StatCard(
                        title = "Bosses Defeated",
                        value = "${state.bossesDefeatedCount}/${state.totalBossesCount}",
                        icon = Icons.Default.Check,
                        iconColor = c.hpHigh,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Daily Streak",
                        value = "${state.currentStreak} days",
                        icon = Icons.Default.Favorite,
                        iconColor = c.streak,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Weekly Focus Chart
            WeeklyFocusChart(bars = state.weeklyChartBars)

            // Boss Progress List
            BossProgressList(bosses = state.bossProgressItems)
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    val c = FocusQuestTheme.colors
    FQCard(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(iconColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(FocusQuestTheme.spacing.xs))
            Column {
                Text(
                    text = title,
                    style = FocusQuestTheme.typography.bodySmall,
                    color = c.textSecondary,
                    maxLines = 1
                )
                Text(
                    text = value,
                    style = FocusQuestTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = c.textPrimary,
                    maxLines = 1
                )
            }
        }
    }
}
