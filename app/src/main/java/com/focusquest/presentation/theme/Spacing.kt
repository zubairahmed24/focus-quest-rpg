package com.focusquest.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Spacing scale, ported 1:1 from the design system's Spacing sheet
 * (8 · 12 · 16 · 24 · 32 · 40 · 56 · 72 · 80 · 96 · 120).
 *
 * Use via the theme: `FocusQuestTheme.spacing.md`. Never hardcode dp paddings in
 * screens — reach for a named step so layout rhythm stays consistent everywhere.
 */
@Immutable
data class Spacing(
    val xxs: Dp = 8.dp,
    val xs: Dp = 12.dp,
    val sm: Dp = 16.dp,
    val md: Dp = 24.dp,
    val lg: Dp = 32.dp,
    val xl: Dp = 40.dp,
    val xxl: Dp = 56.dp,
    val xxxl: Dp = 72.dp,
    val huge: Dp = 80.dp,
    val giant: Dp = 96.dp,
    val colossal: Dp = 120.dp,
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }
