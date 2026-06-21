package com.focusquest.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/**
 * Unit tests for DateTimeUtils.
 *
 * Verifies:
 * - Date formatting and parsing round-trip
 * - isYesterday logic
 * - isToday logic
 * - daysBetween calculation
 * - formatFocusTime formatting
 */
class DateTimeUtilsTest {

    @Test
    fun `formatDate produces ISO string`() {
        val date = LocalDate.of(2026, 6, 21)
        val formatted = DateTimeUtils.formatDate(date)
        assertEquals("2026-06-21", formatted)
    }

    @Test
    fun `parseDate reads ISO string correctly`() {
        val parsed = DateTimeUtils.parseDate("2026-06-21")
        assertEquals(LocalDate.of(2026, 6, 21), parsed)
    }

    @Test
    fun `formatDate and parseDate are inverse operations`() {
        val original = LocalDate.of(2026, 1, 15)
        val roundTrip = DateTimeUtils.parseDate(DateTimeUtils.formatDate(original))
        assertEquals(original, roundTrip)
    }

    @Test
    fun `isYesterday returns true for previous day`() {
        val today = LocalDate.of(2026, 6, 21)
        val yesterday = LocalDate.of(2026, 6, 20)
        assertTrue(DateTimeUtils.isYesterday(yesterday, today))
    }

    @Test
    fun `isYesterday returns false for same day`() {
        val today = LocalDate.of(2026, 6, 21)
        assertFalse(DateTimeUtils.isYesterday(today, today))
    }

    @Test
    fun `isYesterday returns false for 2 days ago`() {
        val today = LocalDate.of(2026, 6, 21)
        val twoDaysAgo = LocalDate.of(2026, 6, 19)
        assertFalse(DateTimeUtils.isYesterday(twoDaysAgo, today))
    }

    @Test
    fun `isToday returns true for same date`() {
        val today = LocalDate.of(2026, 6, 21)
        assertTrue(DateTimeUtils.isToday(today, today))
    }

    @Test
    fun `isToday returns false for different date`() {
        val today = LocalDate.of(2026, 6, 21)
        val other = LocalDate.of(2026, 6, 20)
        assertFalse(DateTimeUtils.isToday(other, today))
    }

    @Test
    fun `daysBetween returns correct count`() {
        val date1 = LocalDate.of(2026, 6, 18)
        val date2 = LocalDate.of(2026, 6, 21)
        assertEquals(3L, DateTimeUtils.daysBetween(date1, date2))
    }

    @Test
    fun `formatFocusTime formats minutes only`() {
        assertEquals("25m", DateTimeUtils.formatFocusTime(25))
    }

    @Test
    fun `formatFocusTime formats hours and minutes`() {
        assertEquals("2h 10m", DateTimeUtils.formatFocusTime(130))
    }

    @Test
    fun `formatFocusTime formats zero minutes`() {
        assertEquals("0m", DateTimeUtils.formatFocusTime(0))
    }
}
