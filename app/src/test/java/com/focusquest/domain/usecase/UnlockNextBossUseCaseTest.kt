package com.focusquest.domain.usecase

import com.focusquest.domain.model.Boss
import com.focusquest.domain.repository.BossRepository
import com.focusquest.domain.repository.PlayerRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for UnlockNextBossUseCase.
 *
 * Verifies:
 * - Next boss is unlocked when previous is defeated
 * - Player state is updated with new boss ID and HP
 * - No action when defeated boss is the final boss (campaign complete)
 * - No action when defeated boss ID is not found
 */
class UnlockNextBossUseCaseTest {

    private lateinit var bossRepository: BossRepository
    private lateinit var playerRepository: PlayerRepository
    private lateinit var useCase: UnlockNextBossUseCase

    private val boss1 = Boss(1, "Slime", 100, "taunt", true, false, 1)
    private val boss2 = Boss(2, "Goblin", 200, "taunt", false, false, 2)
    private val boss5 = Boss(5, "Dragon", 500, "taunt", true, false, 5)
    private val allBosses = listOf(boss1, boss2, boss5)

    @Before
    fun setUp() {
        bossRepository = mockk(relaxed = true)
        playerRepository = mockk(relaxed = true)
        useCase = UnlockNextBossUseCase(bossRepository, playerRepository)
    }

    @Test
    fun `unlocks next boss and updates player state`() = runTest {
        coEvery { bossRepository.getAllBossesOnce() } returns allBosses

        useCase(defeatedBossId = 1)

        coVerify { bossRepository.unlockBoss(2) }
        coVerify { playerRepository.updateCurrentBoss(bossId = 2, hp = 200) }
    }

    @Test
    fun `does nothing when final boss is defeated`() = runTest {
        coEvery { bossRepository.getAllBossesOnce() } returns allBosses

        useCase(defeatedBossId = 5)

        // Verify no unlock or update was called
        coVerify(exactly = 0) { bossRepository.unlockBoss(any()) }
        coVerify(exactly = 0) { playerRepository.updateCurrentBoss(any(), any()) }
    }

    @Test
    fun `does nothing when defeated boss ID not found`() = runTest {
        coEvery { bossRepository.getAllBossesOnce() } returns allBosses

        useCase(defeatedBossId = 999)

        coVerify(exactly = 0) { bossRepository.unlockBoss(any()) }
        coVerify(exactly = 0) { playerRepository.updateCurrentBoss(any(), any()) }
    }
}
