package com.focusquest.domain.model

import java.time.LocalDateTime

/**
 * Domain representation of a single focus session record.
 *
 * Pure Kotlin — no Android dependencies.
 */
data class FocusSession(
    val id: Long = 0,
    val startTime: LocalDateTime,
    val durationMinutes: Int,
    val completed: Boolean,
    val bossId: Int,
    val damageDealt: Int
)
