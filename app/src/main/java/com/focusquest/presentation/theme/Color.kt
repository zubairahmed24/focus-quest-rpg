package com.focusquest.presentation.theme

import androidx.compose.ui.graphics.Color

/**
 * Focus Quest design tokens.
 *
 * These are the RAW palette ramps, ported 1:1 from the Focus Quest design system
 * (Annisa Maghfira Izzani). They are intentionally semantic-free — do NOT reference
 * these directly in screens. Use the semantic colors exposed via [FocusQuestColors]
 * (LocalFocusQuestColors) or the Material 3 [androidx.compose.material3.ColorScheme].
 *
 * Naming follows the design system: <Ramp><Tone> where 100 is the base tone,
 * higher numbers are darker (e.g. 140), lower numbers are lighter (e.g. 20).
 */

// ----------------------------------------------------------------------------
// Primary — electric indigo. Brand / magic / XP / primary actions.
// ----------------------------------------------------------------------------
val Primary140 = Color(0xFF150259)
val Primary120 = Color(0xFF1F0580)
val Primary100 = Color(0xFF5627FF) // base
val Primary80 = Color(0xFF664EFF)
val Primary60 = Color(0xFFAB93FF)
val Primary40 = Color(0xFFD5C9FF)
val Primary20 = Color(0xFFEDE8FF)

// ----------------------------------------------------------------------------
// Secondary — quest gold. Hero CTA ("START FOCUSING"), victory, rewards.
// ----------------------------------------------------------------------------
val Secondary100 = Color(0xFFF4C517) // base
val Secondary80 = Color(0xFFF6CF3E)
val Secondary60 = Color(0xFFF9DB45)
val Secondary40 = Color(0xFFFBECB2)
val Secondary20 = Color(0xFFFDF3D1)

// ----------------------------------------------------------------------------
// Base — neutrals. 140 = near-black surface, 00 = pure white.
// ----------------------------------------------------------------------------
val Base140 = Color(0xFF05060F)
val Base120 = Color(0xFF1E1F27)
val Base100 = Color(0xFF696A6F)
val Base80 = Color(0xFFACACAF)
val Base60 = Color(0xFFEAEAEB)
val Base40 = Color(0xFFF0F0F1)
val Base20 = Color(0xFFF3F3F3)
val Base00 = Color(0xFFFFFFFF)

// ----------------------------------------------------------------------------
// Accent ramps — status & game state.
// ----------------------------------------------------------------------------
val Error100 = Color(0xFFDB2323)
val Error80 = Color(0xFFE66565)
val Error60 = Color(0xFFED9191)
val Error40 = Color(0xFFF4BDBD)
val Error20 = Color(0xFFFBE9E9)

val Success100 = Color(0xFF14B823)
val Success80 = Color(0xFF5BCD65)
val Success60 = Color(0xFF89DB91)
val Success40 = Color(0xFFB9EABD)
val Success20 = Color(0xFFE7F8E9)

val Info100 = Color(0xFF0043CE)
val Info80 = Color(0xFF2A62D6)
val Info60 = Color(0xFF80A1E6)
val Info40 = Color(0xFFAAC0EF)
val Info20 = Color(0xFFCCD9F5)

val Warning100 = Color(0xFFF1C21B)
val Warning80 = Color(0xFFF3CC41)
val Warning60 = Color(0xFFF8E08D)
val Warning40 = Color(0xFFFAEBB3)
val Warning20 = Color(0xFFFCF3D1)

val Pink100 = Color(0xFFFE3D98)
val Pink80 = Color(0xFFFE64AD)
val Pink60 = Color(0xFFFF9ECB)
val Pink40 = Color(0xFFFFC5E0)
val Pink20 = Color(0xFFFFECF5)
