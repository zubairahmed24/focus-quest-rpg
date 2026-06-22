package com.focusquest.presentation.battle.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Boss visual placeholder.
 *
 * Uses a large emoji for MVP. In v1.1, this will be replaced
 * with actual boss artwork (AI-generated or commissioned).
 *
 * @param bossName The boss name (used to pick emoji)
 * @param isShaking Whether to show shake animation (on damage)
 * @param modifier Standard Compose modifier
 */
@Composable
fun BossSprite(
    bossName: String,
    isShaking: Boolean = false,
    modifier: Modifier = Modifier
) {
    // Pick emoji based on boss name (simple mapping for MVP)
    val emoji = when {
        bossName.contains("Slime", ignoreCase = true) -> "🫠"
        bossName.contains("Goblin", ignoreCase = true) -> "👺"
        bossName.contains("Demon", ignoreCase = true) -> "👹"
        bossName.contains("Wraith", ignoreCase = true) -> "👻"
        bossName.contains("Dragon", ignoreCase = true) -> "🐉"
        else -> "👹"
    }

    val shakeOffset by animateFloatAsState(
        targetValue = if (isShaking) 15f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessMedium),
        label = "bossShake"
    )
    val bossScale by animateFloatAsState(
        targetValue = if (isShaking) 0.9f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "bossScale"
    )

    Box(
        modifier = modifier.size(120.dp).offset(x = shakeOffset.dp).scale(bossScale),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            fontSize = 72.sp
        )
    }
}
