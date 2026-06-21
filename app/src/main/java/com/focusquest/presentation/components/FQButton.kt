package com.focusquest.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.focusquest.presentation.theme.FocusQuestTheme

/**
 * Button — design-system parity.
 *
 * Variants mirror the design sheet: Primary (filled brand), Cta (hero gold pill,
 * for "START FOCUSING"/victory), Secondary (neutral surface), Outline, and Danger
 * (outlined destructive, e.g. "Give Up").
 * Sizes mirror the mobile spec: Md = V12/H16/16sp, Lg = V20/H24/20sp,
 * Cta = V18/H40/24sp pill. Disabled state is handled by Material's container alpha.
 */
enum class FQButtonVariant { Primary, Cta, Secondary, Outline, Danger }
enum class FQButtonSize { Md, Lg, Cta }

private fun FQButtonSize.padding(): PaddingValues = when (this) {
    FQButtonSize.Md -> PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    FQButtonSize.Lg -> PaddingValues(horizontal = 24.dp, vertical = 20.dp)
    FQButtonSize.Cta -> PaddingValues(horizontal = 40.dp, vertical = 18.dp)
}

private fun FQButtonSize.fontSize() = when (this) {
    FQButtonSize.Md -> 16.sp
    FQButtonSize.Lg -> 20.sp
    FQButtonSize.Cta -> 24.sp
}

@Composable
fun FQButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: FQButtonVariant = FQButtonVariant.Primary,
    size: FQButtonSize = FQButtonSize.Md,
    enabled: Boolean = true,
) {
    val c = FocusQuestTheme.colors
    val shape = if (size == FQButtonSize.Cta) RoundedCornerShape(percent = 50) else RoundedCornerShape(12.dp)
    val label: @Composable () -> Unit = {
        Text(text, fontSize = size.fontSize(), fontWeight = FontWeight.SemiBold)
    }

    when (variant) {
        FQButtonVariant.Outline, FQButtonVariant.Danger -> {
            val accent = if (variant == FQButtonVariant.Danger) c.error else c.brand
            OutlinedButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                border = BorderStroke(1.5.dp, if (enabled) accent else c.border),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (variant == FQButtonVariant.Danger) c.error else c.textPrimary,
                    disabledContentColor = c.textDisabled,
                ),
                contentPadding = size.padding(),
                content = { label() },
            )
        }
        else -> {
            val (container, on) = when (variant) {
                FQButtonVariant.Primary -> c.brand to c.onBrand
                FQButtonVariant.Cta -> c.cta to c.onCta
                FQButtonVariant.Secondary -> c.surfaceElevated to c.textPrimary
                else -> Color.Unspecified to Color.Unspecified
            }
            Button(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = container,
                    contentColor = on,
                    disabledContainerColor = c.surface,
                    disabledContentColor = c.textDisabled,
                ),
                contentPadding = size.padding(),
                content = { label() },
            )
        }
    }
}
