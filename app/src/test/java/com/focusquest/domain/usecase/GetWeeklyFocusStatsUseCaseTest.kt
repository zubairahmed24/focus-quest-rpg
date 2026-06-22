package com.focusquest.domain.usecase

import com.focusquest.domain.model.FocusSession
import com.focusquest.domain.repository.FocusSessionRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Unit tests for GetWeeklyFocusStatsUseCase.
 *
 * Verifies the weekly bar-chart data pipeline:
 * - Always produces exactly 7 entries (today-6 .. today), chronological.
 * - Empty / missing days are filled with zeros.
 * - Sessions are grouped and minutes summed per day; session count is correct.
 * - The 6-days-ago lower boundary is inclusive; older sessions are excluded.
 */
class GetWeeklyFocusStatsUseCaseTest {

    private lateinit var focusSessionRepository: FocusSessionRepository
    private lateinit var useCase: GetWeeklyFocusStatsUseCase

    @Before
    fun setUp() {
        focusSessionRepository = mockk()
        useCase = GetWeeklyFocusStatsUseCase(focusSessionRepository)
    }

    private fun session(start: LocalDateTime, minutes: Int = 25) = FocusSession(
        startTime = start,
        durationMinutes = minutes,
        completed = true,
        bossId = 1,
        damageDealt = 100
    )

    @Test
    fun `produces exactly 7 entries`() = runTest {
        every { focusSessionRepository.getRecentSessions() } returns flowOf(emptyList())

        val result = useCase().first()

        assertEquals(7, result.size)
    }

    @Test
    fun `entries span from 6 days ago through today in chronological order`() = runTest {
        every { focusSessionRepository.getRecentSessions() } returns flowOf(emptyList())

        val result = useCase().first()
        val today = LocalDate.now()

        assertEquals(today.minusDays(6), result.first().date)
        assertEquals(today, result.last().date)
        // strictly increasing, one day apart
        for (i in 1 until result.size) {
            assertEquals(result[i - 1].date.plusDays(1), result[i].date)
        }
    }

    @Test
    fun `empty sessions yield all-zero days`() = runTest {
        every { focusSessionRepository.getRecentSessions() } returns flowOf(emptyList())

        val result = useCase().first()

        assertTrue(result.all { it.totalMinutes == 0 && it.sessionsCompleted == 0 })
    }

    @Test
    fun `sums minutes and counts sessions for the same day`() = runTest {
        val today = LocalDate.now()
        every { focusSessionRepository.getRecentSessions() } returns flowOf(
            listOf(
                session(today.atTime(9, 0), 25),
                session(today.atTime(11, 0), 25),
                session(today.atTime(15, 0), 25)
            )
        )

        val result = useCase().first()
        val todayStats = result.last()

        assertEquals(today, todayStats.date)
        assertEquals(75, todayStats.totalMinutes)
        assertEquals(3, todayStats.sessionsCompleted)
    }

    @Test
    fun `days with no activity remain zero while active days are populated`() = runTest {
        val today = LocalDate.now()
        val threeDaysAgo = today.minusDays(3)
        every { focusSessionRepository.getRecentSessions() } returns flowOf(
            listOf(session(threeDaysAgo.atTime(10, 0), 25))
        )

        val result = useCase().first()
        val activeDay = result.first { it.date == threeDaysAgo }
        val otherDays = result.filter { it.date != threeDaysAgo }

        assertEquals(25, activeDay.totalMinutes)
        assertEquals(1, activeDay.sessionsCompleted)
        assertTrue(otherDays.all { it.totalMinutes == 0 && it.sessionsCompleted == 0 })
    }

    @Test
    fun `includes a session exactly 6 days ago (inclusive lower boundary)`() = runTest {
        val today = LocalDate.now()
        val sixDaysAgo = today.minusDays(6)
        every { focusSessionRepository.getRecentSessions() } returns flowOf(
            listOf(session(sixDaysAgo.atTime(8, 0), 25))
        )

        val result = useCase().first()

        assertEquals(sixDaysAgo, result.first().date)
        assertEquals(25, result.first().totalMinutes)
        assertEquals(1, result.first().sessionsCompleted)
    }

    @Test
    fun `excludes sessions older than 7 days`() = runTest {
        val today = LocalDate.now()
        every { focusSessionRepository.getRecentSessions() } returns flowOf(
            listOf(
                session(today.minusDays(10).atTime(10, 0), 25), // out of window
                session(today.atTime(10, 0), 25)                 // in window
            )
        )

        val result = useCase().first()
        val totalMinutesAcrossWeek = result.sumOf { it.totalMinutes }
        val totalSessionsAcrossWeek = result.sumOf { it.sessionsCompleted }

        // only today's session counts
        assertEquals(25, totalMinutesAcrossWeek)
        assertEquals(1, totalSessionsAcrossWeek)
    }

    @Test
    fun `aggregates multiple distinct days independently`() = runTest {
        val today = LocalDate.now()
        val twoDaysAgo = today.minusDays(2)
        every { focusSessionRepository.getRecentSessions() } returns flowOf(
            listOf(
                session(twoDaysAgo.atTime(9, 0), 25),
                session(twoDaysAgo.atTime(10, 0), 25),
                session(today.atTime(9, 0), 25)
            )
        )

        val result = useCase().first()

        val twoDaysAgoStats = result.first { it.date == twoDaysAgo }
        val todayStats = result.last()

        assertEquals(50, twoDaysAgoStats.totalMinutes)
        assertEquals(2, twoDaysAgoStats.sessionsCompleted)
        assertEquals(25, todayStats.totalMinutes)
        assertEquals(1, todayStats.sessionsCompleted)
    }
}
