package com.focusquest.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.focusquest.presentation.theme.FocusQuestTheme
import com.focusquest.presentation.theme.Base140

/**
 * Tag — gold pill label (e.g. "NEW", "x2 DAMAGE"). Sizes S/M/L per design system.
 */
enum class FQTagSize { S, M, L }

@Composable
fun FQTag(
    text: String,
    modifier: Modifier = Modifier,
    size: FQTagSize = FQTagSize.M,
) {
    val c = FocusQuestTheme.colors
    val pad = when (size) {
        FQTagSize.S -> PaddingValues(horizontal = 8.dp, vertical = 2.dp)
        FQTagSize.M -> PaddingValues(horizontal = 10.dp, vertical = 4.dp)
        FQTagSize.L -> PaddingValues(horizontal = 14.dp, vertical = 6.dp)
    }
    val fs = when (size) { FQTagSize.S -> 10.sp; FQTagSize.M -> 12.sp; FQTagSize.L -> 16.sp }
    Text(
        text = text,
        color = Base140,
        fontSize = fs,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(c.cta)
            .padding(pad),
    )
}
