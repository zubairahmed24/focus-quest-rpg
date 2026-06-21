package com.focusquest.presentation.battle.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.focusquest.presentation.theme.HpGreen
import com.focusquest.presentation.theme.HpRed
import com.focusquest.presentation.theme.HpYellow

/**
 * Animated HP bar with color transitions based on percentage.
 *
 * - Green when HP > 50%
 * - Yellow when HP 25-50%
 * - Red when HP < 25%
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
    val percentage = if (maxHp > 0) currentHp.toFloat() / maxHp.toFloat() else 0f
    val barColor by animateColorAsState(
        targetValue = when {
            percentage > 0.5f -> HpGreen
            percentage > 0.25f -> HpYellow
            else -> HpRed
        },
        animationSpec = tween(durationMillis = 300),
        label = "hpBarColor"
    )

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "HP",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$currentHp / $maxHp",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
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
