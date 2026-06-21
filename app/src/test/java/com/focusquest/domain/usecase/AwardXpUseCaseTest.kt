package com.focusquest.domain.usecase

import com.focusquest.domain.model.PlayerState
import com.focusquest.domain.repository.PlayerRepository
import com.focusquest.util.Constants
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

/**
 * Unit tests for AwardXpUseCase.
 *
 * Verifies:
 * - XP is awarded correctly (+100 per session)
 * - Level up occurs at 300 XP
 * - Multiple level-ups in a single award are handled
 * - XP resets after level up
 * - Edge case: player state not found returns defaults
 */
class AwardXpUseCaseTest {

    private lateinit var playerRepository: PlayerRepository
    private lateinit var awardXpUseCase: AwardXpUseCase

    @Before
    fun setUp() {
        playerRepository = mockk(relaxed = true)
        awardXpUseCase = AwardXpUseCase(playerRepository)
    }

    @Test
    fun `awards 100 XP when player has 0 XP`() = runTest {
        coEvery { playerRepository.getPlayerStateOnce() } returns PlayerState(xp = 0, level = 1)

        val (newXp, newLevel) = awardXpUseCase()

        assertEquals(100, newXp)
        assertEquals(1, newLevel)
        coVerify { playerRepository.updateXpAndLevel(xp = 100, level = 1) }
    }

    @Test
    fun `levels up when XP reaches 300`() = runTest {
        coEvery { playerRepository.getPlayerStateOnce() } returns PlayerState(xp = 200, level = 1)

        val (newXp, newLevel) = awardXpUseCase()

        assertEquals(0, newXp)
        assertEquals(2, newLevel)
        coVerify { playerRepository.updateXpAndLevel(xp = 0, level = 2) }
    }

    @Test
    fun `handles multiple level-ups in single award`() = runTest {
        // XP = 550, award 100 → 650 → level up twice (650 - 600 = 50, level +2)
        coEvery { playerRepository.getPlayerStateOnce() } returns PlayerState(xp = 550, level = 3)

        val (newXp, newLevel) = awardXpUseCase()

        assertEquals(50, newXp)
        assertEquals(5, newLevel)
        coVerify { playerRepository.updateXpAndLevel(xp = 50, level = 5) }
    }

    @Test
    fun `returns default values when player state is null`() = runTest {
        coEvery { playerRepository.getPlayerStateOnce() } returns null

        val (newXp, newLevel) = awardXpUseCase()

        assertEquals(0, newXp)
        assertEquals(1, newLevel)
    }

    @Test
    fun `XP just below threshold does not level up`() = runTest {
        coEvery { playerRepository.getPlayerStateOnce() } returns PlayerState(xp = 199, level = 1)

        val (newXp, newLevel) = awardXpUseCase()

        assertEquals(299, newXp)
        assertEquals(1, newLevel)
    }
}
