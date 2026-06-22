package com.focusquest.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalContext
import com.focusquest.presentation.theme.FocusQuestTheme

@Composable
fun ShareCard(
    playerLevel: Int,
    streak: Int,
    totalFocusTimeFormatted: String,
    bossesDefeatedCount: Int,
    totalBossesCount: Int,
    modifier: Modifier = Modifier
) {
    val c = FocusQuestTheme.colors

    Box(
        modifier = modifier
            .width(320.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(c.surface)
            .border(
                BorderStroke(3.dp, c.cta),
                RoundedCornerShape(24.dp)
            )
            .padding(FocusQuestTheme.spacing.md)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header
            Text(
                text = "⚔️ FOCUS QUEST ⚔️",
                style = FocusQuestTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = c.cta,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "RPG Focus Pomodoro",
                style = FocusQuestTheme.typography.bodySmall,
                color = c.textSecondary
            )

            Spacer(modifier = Modifier.height(FocusQuestTheme.spacing.md))

            // Decorative Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(2.dp)
                    .background(c.border)
            )

            Spacer(modifier = Modifier.height(FocusQuestTheme.spacing.md))

            // Hero badge
            Text(
                text = "QUEST VICTOR",
                style = FocusQuestTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = c.textPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(FocusQuestTheme.spacing.md))

            // Stats grid inside the card
            Column(
                verticalArrangement = Arrangement.spacedBy(FocusQuestTheme.spacing.xs),
                modifier = Modifier.fillMaxWidth()
            ) {
                ShareStatRow(
                    label = "Hero Level",
                    value = "Lvl $playerLevel",
                    icon = Icons.Default.Star,
                    iconColor = c.cta
                )
                ShareStatRow(
                    label = "Daily Streak",
                    value = "$streak days 🔥",
                    icon = Icons.Default.Favorite,
                    iconColor = c.streak
                )
                ShareStatRow(
                    label = "Total Focus Time",
                    value = totalFocusTimeFormatted,
                    icon = Icons.Default.PlayArrow,
                    iconColor = c.brand
                )
                ShareStatRow(
                    label = "Bosses Defeated",
                    value = "$bossesDefeatedCount/$totalBossesCount",
                    icon = Icons.Default.Check,
                    iconColor = c.hpHigh
                )
            }

            Spacer(modifier = Modifier.height(FocusQuestTheme.spacing.lg))

            // Footer slogan
            Text(
                text = "Defeat Procrastination. One Focus Session at a Time.",
                style = FocusQuestTheme.typography.bodySmall,
                color = c.textSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun ShareStatRow(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: androidx.compose.ui.graphics.Color
) {
    val c = FocusQuestTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(c.surfaceElevated)
            .padding(FocusQuestTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(FocusQuestTheme.spacing.xs))
        Text(
            text = label,
            style = FocusQuestTheme.typography.bodyMedium,
            color = c.textSecondary
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value,
            style = FocusQuestTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = c.textPrimary
        )
    }
}

@Composable
fun ShareDialog(
    playerLevel: Int,
    streak: Int,
    totalFocusTimeFormatted: String,
    bossesDefeatedCount: Int,
    totalBossesCount: Int,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        val dialogView = LocalView.current

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .wrapContentSize()
                .padding(FocusQuestTheme.spacing.sm)
        ) {
            ShareCard(
                playerLevel = playerLevel,
                streak = streak,
                totalFocusTimeFormatted = totalFocusTimeFormatted,
                bossesDefeatedCount = bossesDefeatedCount,
                totalBossesCount = totalBossesCount
            )

            Spacer(modifier = Modifier.height(FocusQuestTheme.spacing.md))

            Row(
                modifier = Modifier.width(320.dp),
                horizontalArrangement = Arrangement.spacedBy(FocusQuestTheme.spacing.sm)
            ) {
                FQButton(
                    text = "Close",
                    onClick = onDismiss,
                    variant = FQButtonVariant.Outline,
                    modifier = Modifier.weight(1f)
                )
                FQButton(
                    text = "Share Quest",
                    onClick = {
                        ShareUtils.shareBitmap(context, dialogView) {
                            onDismiss()
                        }
                    },
                    variant = FQButtonVariant.Cta,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
