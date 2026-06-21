package com.focusquest.presentation.battle.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Speech bubble displaying the boss's taunt text.
 *
 * Positioned above the boss sprite to create a dialogue effect.
 * Uses a rounded card with semi-transparent background.
 *
 * @param taunt The taunt text to display
 * @param modifier Standard Compose modifier
 */
@Composable
fun TauntBubble(
    taunt: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .widthIn(max = 280.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f))
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = taunt,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
        )
    }
}
