package com.focusquest.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object DateTimeUtils {

    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    fun formatDate(date: LocalDate): String = date.format(dateFormatter)

    fun parseDate(dateString: String): LocalDate = LocalDate.parse(dateString, dateFormatter)

    fun formatDateTime(dateTime: LocalDateTime): String = dateTime.format(dateTimeFormatter)

    fun parseDateTime(dateTimeString: String): LocalDateTime =
        LocalDateTime.parse(dateTimeString, dateTimeFormatter)

    fun isYesterday(date: LocalDate, reference: LocalDate = LocalDate.now()): Boolean {
        return ChronoUnit.DAYS.between(date, reference) == 1L
    }

    fun isToday(date: LocalDate, reference: LocalDate = LocalDate.now()): Boolean {
        return date == reference
    }

    fun daysBetween(date1: LocalDate, date2: LocalDate): Long {
        return ChronoUnit.DAYS.between(date1, date2)
    }

    fun formatFocusTime(totalMinutes: Int): String {
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return when {
            hours > 0 -> "${hours}h ${minutes}m"
            else -> "${minutes}m"
        }
    }
}