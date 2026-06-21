package com.focusquest.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Focus Quest theme.
 *
 * Design philosophy: the design system is a TOKEN system, not a "light theme".
 * We keep the tokens (ramps, spacing, radii, type) and re-map the *semantics* for a
 * dark-first RPG surface. Dark is canonical (the game lives in the dark); a real
 * light scheme is derived from the same tokens so consistency holds — it is NOT a
 * forced inversion.
 *
 * Material 3's [androidx.compose.material3.ColorScheme] only carries a handful of
 * roles, so game-specific semantics (HP, XP, streak, the full accent ramps) live in
 * [FocusQuestColors], exposed via [LocalFocusQuestColors] and the [FocusQuestTheme]
 * accessor object.
 */

@Immutable
data class FocusQuestColors(
    val brand: Color,
    val brandMuted: Color,
    val onBrand: Color,
    val cta: Color,
    val ctaPressed: Color,
    val onCta: Color,
    val background: Color,
    val surface: Color,
    val surfaceElevated: Color,
    val border: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textDisabled: Color,
    val hpHigh: Color,
    val hpMid: Color,
    val hpLow: Color,
    val xp: Color,
    val streak: Color,
    val levelUp: Color,
    val success: Color,
    val warning: Color,
    val error: Color,
    val info: Color,
    val isDark: Boolean,
)

/** Dark — canonical RPG surface. */
private val DarkFocusQuestColors = FocusQuestColors(
    brand = Primary80,
    brandMuted = Primary60,
    onBrand = Base00,
    cta = Secondary100,
    ctaPressed = Secondary80,
    onCta = Base140,
    background = Base140,
    surface = Base120,
    surfaceElevated = Color(0xFF2A2B36),
    border = Color(0xFF34353F),
    textPrimary = Base00,
    textSecondary = Base80,
    textDisabled = Base100,
    hpHigh = Success80,
    hpMid = Warning80,
    hpLow = Error80,
    xp = Primary60,
    streak = Pink80,
    levelUp = Secondary60,
    success = Success80,
    warning = Warning80,
    error = Error80,
    info = Info60,
    isDark = true,
)

/** Light — same tokens, inverted surfaces. Provided for completeness, not default. */
private val LightFocusQuestColors = FocusQuestColors(
    brand = Primary100,
    brandMuted = Primary60,
    onBrand = Base00,
    cta = Secondary100,
    ctaPressed = Secondary80,
    onCta = Base140,
    background = Base20,
    surface = Base00,
    surfaceElevated = Base00,
    border = Base60,
    textPrimary = Base140,
    textSecondary = Base100,
    textDisabled = Base80,
    hpHigh = Success100,
    hpMid = Warning100,
    hpLow = Error100,
    xp = Primary100,
    streak = Pink100,
    levelUp = Secondary100,
    success = Success100,
    warning = Warning100,
    error = Error100,
    info = Info100,
    isDark = false,
)

private val DarkColorScheme = darkColorScheme(
    primary = Primary80,
    onPrimary = Base00,
    primaryContainer = Primary120,
    onPrimaryContainer = Primary40,
    secondary = Secondary100,
    onSecondary = Base140,
    secondaryContainer = Secondary40,
    onSecondaryContainer = Base140,
    tertiary = Pink80,
    onTertiary = Base140,
    background = Base140,
    onBackground = Base00,
    surface = Base120,
    onSurface = Base00,
    surfaceVariant = Color(0xFF2A2B36),
    onSurfaceVariant = Base80,
    outline = Color(0xFF34353F),
    error = Error80,
    onError = Base140,
)

private val LightColorScheme = lightColorScheme(
    primary = Primary100,
    onPrimary = Base00,
    primaryContainer = Primary20,
    onPrimaryContainer = Primary120,
    secondary = Secondary100,
    onSecondary = Base140,
    secondaryContainer = Secondary20,
    onSecondaryContainer = Base140,
    tertiary = Pink100,
    onTertiary = Base00,
    background = Base20,
    onBackground = Base140,
    surface = Base00,
    onSurface = Base140,
    surfaceVariant = Base40,
    onSurfaceVariant = Base100,
    outline = Base60,
    error = Error100,
    onError = Base00,
)

val LocalFocusQuestColors = staticCompositionLocalOf { DarkFocusQuestColors }

@Composable
fun FocusQuestTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val fqColors = if (darkTheme) DarkFocusQuestColors else LightFocusQuestColors

    CompositionLocalProvider(
        LocalFocusQuestColors provides fqColors,
        LocalSpacing provides Spacing(),
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = FocusQuestTypography,
            shapes = FocusQuestShapes,
            content = content
        )
    }
}

/** Ergonomic accessor: `FocusQuestTheme.colors.cta`, `FocusQuestTheme.spacing.md`. */
object FocusQuestTheme {
    val colors: FocusQuestColors
        @Composable @ReadOnlyComposable get() = LocalFocusQuestColors.current
    val spacing: Spacing
        @Composable @ReadOnlyComposable get() = LocalSpacing.current
    val typography: Typography
        @Composable @ReadOnlyComposable get() = MaterialTheme.typography
}
