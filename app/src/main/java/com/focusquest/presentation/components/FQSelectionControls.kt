package com.focusquest.presentation.components

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.focusquest.presentation.theme.Base00
import com.focusquest.presentation.theme.FocusQuestTheme

/**
 * Selection controls — Toggle, Checkbox, Radio.
 * Match the design system states (default / checked / disabled) re-mapped to dark:
 * the "on/checked" fill uses the brand indigo (the design's filled-control accent).
 */

@Composable
fun FQToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val c = FocusQuestTheme.colors
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Base00,
            checkedTrackColor = c.brand,
            uncheckedThumbColor = c.textSecondary,
            uncheckedTrackColor = c.surfaceElevated,
            uncheckedBorderColor = c.border,
        ),
    )
}

@Composable
fun FQCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val c = FocusQuestTheme.colors
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = CheckboxDefaults.colors(
            checkedColor = c.brand,
            uncheckedColor = c.border,
            checkmarkColor = Base00,
            disabledUncheckedColor = c.textDisabled,
        ),
    )
}

@Composable
fun FQRadio(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val c = FocusQuestTheme.colors
    RadioButton(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = RadioButtonDefaults.colors(
            selectedColor = c.brand,
            unselectedColor = c.border,
            disabledSelectedColor = c.textDisabled,
            disabledUnselectedColor = c.textDisabled,
        ),
    )
}
