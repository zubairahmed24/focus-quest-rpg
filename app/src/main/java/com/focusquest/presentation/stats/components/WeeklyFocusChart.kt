package com.focusquest.presentation.stats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.focusquest.presentation.components.FQCard
import com.focusquest.presentation.stats.DailyBarState
import com.focusquest.presentation.theme.FocusQuestTheme

private val BarZoneHeight = 100.dp

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

            // Wrap-content height: each column is identical (label + fixed bar zone +
            // label), so nothing overflows or clips regardless of bar height.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(FocusQuestTheme.spacing.xxs)
            ) {
                bars.forEach { bar ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Value label on top of the bar
                        Text(
                            text = bar.valueLabel,
                            style = FocusQuestTheme.typography.bodySmall.copy(fontSize = 10.sp),
                            fontWeight = if (bar.isToday) FontWeight.Bold else FontWeight.Normal,
                            color = if (bar.isToday) FocusQuestTheme.colors.cta else FocusQuestTheme.colors.textSecondary,
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(FocusQuestTheme.spacing.xxs))

                        // Fixed-height bar zone: the bar grows from the bottom up,
                        // scaled against the busiest day (min 8% so a non-zero day
                        // is always visible).
                        Box(
                            modifier = Modifier.height(BarZoneHeight),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            val ratio = (bar.minutes.toFloat() / maxMinutes).coerceIn(0f, 1f)
                            val barHeight = if (bar.minutes > 0) {
                                (BarZoneHeight.value * ratio.coerceAtLeast(0.08f)).dp
                            } else {
                                4.dp
                            }
                            Box(
                                modifier = Modifier
                                    .height(barHeight)
                                    .width(14.dp)
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                    .background(
                                        if (bar.isToday) FocusQuestTheme.colors.cta else FocusQuestTheme.colors.brand
                                    )
                            )
                        }
                        Spacer(modifier = Modifier.height(FocusQuestTheme.spacing.xxs))

                        // Day label
                        Text(
                            text = bar.dayLabel,
                            style = FocusQuestTheme.typography.bodySmall,
                            fontWeight = if (bar.isToday) FontWeight.Bold else FontWeight.Normal,
                            color = if (bar.isToday) FocusQuestTheme.colors.textPrimary else FocusQuestTheme.colors.textSecondary,
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
