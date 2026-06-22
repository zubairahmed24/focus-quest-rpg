package com.focusquest.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

/**
 * Reusable confirmation dialog.
 *
 * Used for destructive actions like "Give Up" during a focus session.
 *
 * @param title Dialog title
 * @param message Dialog body text
 * @param confirmText Text for the confirm button
 * @param dismissText Text for the dismiss button
 * @param onConfirm Called when confirm is tapped
 * @param onDismiss Called when dismiss is tapped or dialog is dismissed
 */
@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            FQButton(
                text = confirmText,
                onClick = onConfirm,
                variant = FQButtonVariant.Danger
            )
        },
        dismissButton = {
            FQButton(
                text = dismissText,
                onClick = onDismiss,
                variant = FQButtonVariant.Outline
            )
        }
    )
}
