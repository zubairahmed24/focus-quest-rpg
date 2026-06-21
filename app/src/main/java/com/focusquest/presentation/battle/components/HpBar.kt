package com.focusquest.presentation.battle.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.focusquest.presentation.theme.FocusQuestTheme

/**
 * Animated HP bar with color transitions based on percentage.
 *
 * - hpHigh (green) when HP > 50%
 * - hpMid (yellow) when HP 25-50%
 * - hpLow (red) when HP < 25%
 *
 * Colors come from the design system via [FocusQuestTheme.colors] so the bar stays
 * consistent with every other status surface in the app.
 *
 * @param currentHp Current HP value
 * @param maxHp Maximum HP value
 * @param modifier Standard Compose modifier
 */
@Composable
fun HpBar(
    currentHp: Int,
    maxHp: Int,
    modifier: Modifier = Modifier
) {
    val colors = FocusQuestTheme.colors
    val percentage = if (maxHp > 0) currentHp.toFloat() / maxHp.toFloat() else 0f
    val barColor by animateColorAsState(
        targetValue = when {
            percentage > 0.5f -> colors.hpHigh
            percentage > 0.25f -> colors.hpMid
            else -> colors.hpLow
        },
        animationSpec = tween(durationMillis = 300),
        label = "hpBarColor"
    )

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "HP",
                style = MaterialTheme.typography.labelMedium,
                color = colors.textSecondary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$currentHp / $maxHp",
                style = MaterialTheme.typography.labelMedium,
                color = colors.textSecondary,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(colors.surfaceElevated)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage.coerceIn(0f, 1f))
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(barColor)
            )
        }
    }
}
