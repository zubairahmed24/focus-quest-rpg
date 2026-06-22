package com.focusquest.presentation.stats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.focusquest.presentation.components.FQCard
import com.focusquest.presentation.stats.BossProgressItem
import com.focusquest.presentation.stats.BossStatus
import com.focusquest.presentation.theme.FocusQuestTheme

@Composable
fun BossProgressList(
    bosses: List<BossProgressItem>,
    modifier: Modifier = Modifier
) {
    FQCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            Text(
                text = "Boss Progress",
                style = FocusQuestTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = FocusQuestTheme.colors.textPrimary
            )
            Spacer(modifier = Modifier.height(FocusQuestTheme.spacing.md))

            Column(
                verticalArrangement = Arrangement.spacedBy(FocusQuestTheme.spacing.xs)
            ) {
                bosses.forEach { boss ->
                    BossProgressRow(boss = boss)
                }
            }
        }
    }
}

@Composable
fun BossProgressRow(
    boss: BossProgressItem,
    modifier: Modifier = Modifier
) {
    val c = FocusQuestTheme.colors
    val (icon, iconColor, textColor, statusText) = when (boss.status) {
        BossStatus.DEFEATED -> Quadruple(Icons.Default.Check, c.hpHigh, c.textSecondary, "Defeated")
        BossStatus.CURRENT -> Quadruple(Icons.Default.PlayArrow, c.cta, c.textPrimary, "⚔️ Current")
        BossStatus.LOCKED -> Quadruple(Icons.Default.Lock, c.textDisabled, c.textDisabled, "Locked")
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (boss.status == BossStatus.CURRENT) c.brand.copy(alpha = 0.12f) else c.surfaceElevated
            )
            .border(
                1.dp,
                if (boss.status == BossStatus.CURRENT) c.cta.copy(alpha = 0.5f) else c.border,
                RoundedCornerShape(8.dp)
            )
            .padding(FocusQuestTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (boss.status == BossStatus.CURRENT) c.cta.copy(alpha = 0.12f) else c.background
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(FocusQuestTheme.spacing.sm))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = boss.name,
                style = FocusQuestTheme.typography.bodyLarge,
                fontWeight = if (boss.status == BossStatus.CURRENT) FontWeight.Bold else FontWeight.Normal,
                color = textColor
            )
            Text(
                text = "HP: ${boss.maxHp}",
                style = FocusQuestTheme.typography.bodySmall,
                color = c.textSecondary
            )
        }

        Text(
            text = statusText,
            style = FocusQuestTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = iconColor
        )
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
