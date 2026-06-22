package com.focusquest.presentation.stats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.focusquest.presentation.components.FQCard
import com.focusquest.presentation.stats.DailyBarState
import com.focusquest.presentation.theme.FocusQuestTheme

@Composable
fun WeeklyFocusChart(
    bars: List<DailyBarState>,
    modifier: Modifier = Modifier
) {
    val maxMinutes = bars.maxOfOrNull { it.minutes }?.coerceAtLeast(1) ?: 1

    FQCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            Text(
                text = "Weekly Focus",
                style = FocusQuestTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = FocusQuestTheme.colors.textPrimary
            )
            Spacer(modifier = Modifier.height(FocusQuestTheme.spacing.md))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(vertical = FocusQuestTheme.spacing.xs),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                bars.forEach { bar ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Value label on top of bar
                        Text(
                            text = bar.valueLabel,
                            style = FocusQuestTheme.typography.bodySmall.copy(fontSize = 10.sp),
                            fontWeight = if (bar.isToday) FontWeight.Bold else FontWeight.Normal,
                            color = if (bar.isToday) FocusQuestTheme.colors.cta else FocusQuestTheme.colors.textSecondary
                        )
                        Spacer(modifier = Modifier.height(FocusQuestTheme.spacing.xxs))

                        // Scaled bar of max height 100.dp
                        val barHeight = (100 * (bar.minutes.toFloat() / maxMinutes).coerceAtLeast(0.08f)).dp
                        Box(
                            modifier = Modifier
                                .height(barHeight)
                                .width(14.dp)
                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                .background(
                                    if (bar.isToday) FocusQuestTheme.colors.cta else FocusQuestTheme.colors.brand
                                )
                        )
                        Spacer(modifier = Modifier.height(FocusQuestTheme.spacing.xxs))

                        // Day label
                        Text(
                            text = bar.dayLabel,
                            style = FocusQuestTheme.typography.bodySmall,
                            fontWeight = if (bar.isToday) FontWeight.Bold else FontWeight.Normal,
                            color = if (bar.isToday) FocusQuestTheme.colors.textPrimary else FocusQuestTheme.colors.textSecondary
                        )
                    }
                }
            }
        }
    }
}
