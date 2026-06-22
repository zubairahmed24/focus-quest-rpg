package com.focusquest.presentation.battle.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.scale
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import com.focusquest.presentation.theme.FocusQuestTheme

/**
 * Circular timer display with MM:SS text in the center.
 *
 * The progress ring depletes as time runs out. During a focus session the ring
 * shifts cta (gold) -> warning -> error using design-system tokens; during a break
 * it uses the calmer info tone. No hardcoded colors.
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
    val colors = FocusQuestTheme.colors
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
            progress > 0.5f -> colors.cta
            progress > 0.25f -> colors.warning
            else -> colors.error
        }
    } else {
        colors.info
    }

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val timeText = String.format("%02d:%02d", minutes, seconds)

        val infiniteTransition = rememberInfiniteTransition(label = "timerPulse")
    val pulseScale by if (remainingSeconds > 0 && remainingSeconds < totalSeconds && isFocusing) {
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.04f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale"
        )
    } else {
        androidx.compose.runtime.rememberUpdatedState(1f)
    }

    Box(
        modifier = modifier.size(200.dp).scale(pulseScale),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val strokeWidth = 8.dp.toPx()
            val arcSize = size.minDimension - strokeWidth
            val topLeft = Offset(
                (size.width - arcSize) / 2f,
                (size.height - arcSize) / 2f
            )
            val arcDimen = Size(arcSize, arcSize)

            drawArc(
                color = colors.textPrimary.copy(alpha = 0.1f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcDimen,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

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
            color = colors.textPrimary
        )
    }
}
