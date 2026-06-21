package com.focusquest.domain.service

import com.focusquest.domain.model.FocusTimerState
import com.focusquest.domain.repository.PlayerRepository
import com.focusquest.util.Constants
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * Unit tests for FocusTimerService.
 *
 * Tests the timestamp-based timer engine's state management and recovery logic.
 * Does NOT test real-time ticking (that requires instrumented tests).
 *
 * Verifies:
 * - Initial state is Idle
 * - startFocus sets state to Focusing and persists timestamp
 * - startBreak sets state to Break
 * - pause transitions to Paused with correct wasFocusing flag
 * - resume transitions back to Focusing or Break
 * - abandon resets to Idle and clears persisted timestamp
 * - recoverSession returns true when elapsed >= duration (session completed)
 * - recoverSession returns false when elapsed < duration (session resumed)
 * - recoverSession returns false when no persisted timestamp exists
 */
class FocusTimerServiceTest {

    private lateinit var playerRepository: PlayerRepository
    private lateinit var service: FocusTimerService

    @Before
    fun setUp() {
        playerRepository = mockk(relaxed = true)
        service = FocusTimerService(playerRepository)
    }

    @Test
    fun `initial state is Idle`() {
        assertEquals(FocusTimerState.Idle, service.timerState.value)
    }

    @Test
    fun `startFocus sets state to Focusing and persists timestamp`() = runTest {
        service.startFocus()

        assertTrue(service.timerState.value is FocusTimerState.Focusing)
        val focusing = service.timerState.value as FocusTimerState.Focusing
        assertEquals(Constants.FOCUS_DURATION_MINUTES * 60, focusing.remainingSeconds)
        coVerify { playerRepository.updateSessionStartTime(any()) }
    }

    @Test
    fun `startBreak sets state to Break`() {
        service.startBreak()

        assertTrue(service.timerState.value is FocusTimerState.Break)
        val breakState = service.timerState.value as FocusTimerState.Break
        assertEquals(Constants.BREAK_DURATION_MINUTES * 60, breakState.remainingSeconds)
    }

    @Test
    fun `pause from Focusing transitions to Paused with wasFocusing true`() = runTest {
        service.startFocus()
        service.pause()

        val state = service.timerState.value
        assertTrue(state is FocusTimerState.Paused)
        val paused = state as FocusTimerState.Paused
        assertTrue(paused.wasFocusing)
    }

    @Test
    fun `pause from Break transitions to Paused with wasFocusing false`() {
        service.startBreak()
        service.pause()

        val state = service.timerState.value
        assertTrue(state is FocusTimerState.Paused)
        val paused = state as FocusTimerState.Paused
        assertFalse(paused.wasFocusing)
    }

    @Test
    fun `resume from Paused (was focusing) returns to Focusing`() = runTest {
        service.startFocus()
        service.pause()
        service.resume()

        assertTrue(service.timerState.value is FocusTimerState.Focusing)
    }

    @Test
    fun `resume from Paused (was break) returns to Break`() {
        service.startBreak()
        service.pause()
        service.resume()

        assertTrue(service.timerState.value is FocusTimerState.Break)
    }

    @Test
    fun `abandon resets to Idle and clears persisted timestamp`() = runTest {
        service.startFocus()
        service.abandon()

        assertEquals(FocusTimerState.Idle, service.timerState.value)
        coVerify { playerRepository.updateSessionStartTime(null) }
    }

    @Test
    fun `recoverSession returns false when no persisted timestamp`() = runTest {
        coEvery { playerRepository.getPlayerStateOnce() } returns null

        val result = service.recoverSession()

        assertFalse(result)
        assertEquals(FocusTimerState.Idle, service.timerState.value)
    }

    @Test
    fun `recoverSession returns true when session completed while away`() = runTest {
        // Simulate a session that started 30 minutes ago (5 min over the 25-min duration)
        val startTime = LocalDateTime.now().minusMinutes(30)
        val player = com.focusquest.domain.model.PlayerState(
            activeSessionStartTime = startTime
        )
        coEvery { playerRepository.getPlayerStateOnce() } returns player

        val result = service.recoverSession()

        assertTrue(result)
        assertEquals(FocusTimerState.Idle, service.timerState.value)
    }

    @Test
    fun `recoverSession returns false and resumes when session still in progress`() = runTest {
        // Simulate a session that started 10 minutes ago (15 min remaining)
        val startTime = LocalDateTime.now().minusMinutes(10)
        val player = com.focusquest.domain.model.PlayerState(
            activeSessionStartTime = startTime
        )
        coEvery { playerRepository.getPlayerStateOnce() } returns player

        val result = service.recoverSession()

        assertFalse(result)
        assertTrue(service.timerState.value is FocusTimerState.Focusing)
    }

    @Test
    fun `stop resets state to Idle without clearing persisted timestamp`() = runTest {
        service.startFocus()
        service.stop()

        assertEquals(FocusTimerState.Idle, service.timerState.value)
        // updateSessionStartTime(null) should NOT be called by stop()
        // (only abandon clears the persisted timestamp)
        coVerify(exactly = 1) { playerRepository.updateSessionStartTime(any()) } // from startFocus
        coVerify(exactly = 0) { playerRepository.updateSessionStartTime(null) }
    }
}
