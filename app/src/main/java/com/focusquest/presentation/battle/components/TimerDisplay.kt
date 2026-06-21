package com.focusquest.presentation.battle.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.focusquest.presentation.theme.Gold

/**
 * Circular timer display with MM:SS text in the center.
 *
 * The progress ring depletes as time runs out.
 * Color shifts from gold to red as time decreases.
 *
 * @param remainingSeconds Seconds remaining in the session
 * @param totalSeconds Total seconds for the full session
 * @param isFocusing Whether this is a focus session (affects color)
 * @param modifier Standard Compose modifier
 */
@Composable
fun TimerDisplay(
    remainingSeconds: Int,
    totalSeconds: Int,
    isFocusing: Boolean,
    modifier: Modifier = Modifier
) {
    val progress = if (totalSeconds > 0) {
        remainingSeconds.toFloat() / totalSeconds.toFloat()
    } else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = "timerProgress"
    )

    val ringColor = if (isFocusing) {
        when {
            progress > 0.5f -> Gold
            progress > 0.25f -> Color(0xFFFFA500)
            else -> Color(0xFFFF6B35)
        }
    } else {
        MaterialTheme.colorScheme.tertiary
    }

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val timeText = String.format("%02d:%02d", minutes, seconds)

    Box(
        modifier = modifier.size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val strokeWidth = 8.dp.toPx()
            val arcSize = size.minDimension - strokeWidth
            val topLeft = androidx.compose.ui.geometry.Offset(
                (size.width - arcSize) / 2f,
                (size.height - arcSize) / 2f
            )
            val arcDimen = androidx.compose.ui.geometry.Size(arcSize, arcSize)

            // Background ring
            drawArc(
                color = Color.White.copy(alpha = 0.1f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcDimen,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Progress ring
            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                topLeft = topLeft,
                size = arcDimen,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Text(
            text = timeText,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
