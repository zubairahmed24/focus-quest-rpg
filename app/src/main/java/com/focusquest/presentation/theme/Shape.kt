package com.focusquest.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Corner radii derived from the design system's component anatomy:
 *  - controls (checkbox / tag): 6–8dp
 *  - buttons / inputs: 12dp
 *  - cards / sheets: 16dp
 *  - modals / large surfaces: 24dp
 *
 * The pill radius for CTA buttons & toggles is applied per-component (50%),
 * not through Material's [Shapes].
 */
val FocusQuestShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp),
)
