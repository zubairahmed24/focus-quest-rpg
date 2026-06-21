package com.focusquest.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.focusquest.presentation.theme.FocusQuestTheme

/**
 * Card — 16dp radius, hairline border, elevated surface. Mirrors the design
 * system Card container (testimonial/pricing block anatomy).
 */
@Composable
fun FQCard(
    modifier: Modifier = Modifier,
    bordered: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    val c = FocusQuestTheme.colors
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = c.surface,
            contentColor = c.textPrimary,
        ),
        border = if (bordered) BorderStroke(1.dp, c.border) else null,
    ) {
        Column(
            modifier = Modifier.padding(FocusQuestTheme.spacing.md),
            content = content,
        )
    }
}
