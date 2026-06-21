package com.focusquest.presentation.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Dark mode is the default — RPG aesthetic
private val DarkColorScheme = darkColorScheme(
    primary = Gold,
    onPrimary = DeepPurple,
    secondary = RoyalPurple,
    onSecondary = Gold,
    tertiary = XpPurple,
    onTertiary = OnSurface,
    background = DeepPurple,
    onBackground = OnSurface,
    surface = SurfaceDark,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    error = HpRed,
    onError = OnSurface,
)

private val LightColorScheme = lightColorScheme(
    primary = DarkGold,
    onPrimary = OnSurface,
    secondary = RoyalPurple,
    onSecondary = Gold,
    tertiary = XpPurple,
    onTertiary = OnSurface,
    background = SurfaceVariant,
    onBackground = DeepPurple,
    surface = OnSurface,
    onSurface = DeepPurple,
)

@Composable
fun FocusQuestTheme(
    darkTheme: Boolean = true, // Force dark mode for RPG aesthetic
    dynamicColor: Boolean = false, // Disable dynamic color for consistent RPG theme
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FocusQuestTypography,
        shapes = FocusQuestShapes,
        content = content
    )
}