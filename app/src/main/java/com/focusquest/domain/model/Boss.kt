package com.focusquest.domain.model

/**
 * Domain representation of a procrastination boss.
 *
 * Pure Kotlin — no Android dependencies.
 */
data class Boss(
    val id: Int,
    val name: String,
    val maxHp: Int,
    val taunt: String,
    val isUnlocked: Boolean,
    val isDefeated: Boolean,
    val order: Int
)
