package com.focusquest.domain.usecase

import com.focusquest.domain.model.Boss
import com.focusquest.domain.model.PlayerState
import com.focusquest.domain.repository.BossRepository
import com.focusquest.domain.repository.FocusSessionRepository
import com.focusquest.domain.repository.PlayerRepository
import com.focusquest.util.Constants
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

/**
 * Unit tests for CompleteFocusSessionUseCase.
 *
 * Verifies:
 * - Streak calculation: first session, consecutive day, same-day, gap reset
 * - Boss damage: 100 damage applied, HP clamped to 0
 * - Boss defeat: marked defeated, next boss unlocked, counter incremented
 * - XP award: 100 XP added, level up at 300
 * - BattleResult: DamageDealt when boss survives, BossDefeated when HP = 0
 * - Campaign complete: no next boss after final boss
 */
class CompleteFocusSessionUseCaseTest {

    private lateinit var playerRepository: PlayerRepository
    private lateinit var bossRepository: BossRepository
    private lateinit var focusSessionRepository: FocusSessionRepository
    private lateinit var useCase: CompleteFocusSessionUseCase

    private val today = LocalDate.now()
    private val yesterday = today.minusDays(1)
    private val twoDaysAgo = today.minusDays(2)

    private val boss1 = Boss(1, "Slime of Laziness", 100, "taunt1", true, false, 1)
    private val boss2 = Boss(2, "Distraction Goblin", 200, "taunt2", false, false, 2)
    private val boss3 = Boss(3, "Procrastination Demon", 300, "taunt3", false, false, 3)
    private val boss4 = Boss(4, "Distraction Wraith", 400, "taunt4", false, false, 4)
    private val boss5 = Boss(5, "Burnout Dragon", 500, "taunt5", false, false, 5)
    private val allBosses = listOf(boss1, boss2, boss3, boss4, boss5)

    @Before
    fun setUp() {
        playerRepository = mockk(relaxed = true)
        bossRepository = mockk(relaxed = true)
        focusSessionRepository = mockk(relaxed = true)
        useCase = CompleteFocusSessionUseCase(playerRepository, bossRepository, focusSessionRepository)

        coEvery { bossRepository.getAllBossesOnce() } returns allBosses
    }

    // ═══════════════════════════════════════════════════════════════
    // STREAK TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun `first session ever sets streak to 1`() = runTest {
        val player = PlayerState(streak = 0, lastFocusDate = today, currentBossHpRemaining = 100)
        coEvery { playerRepository.getPlayerStateOnce() } returns player
        coEvery { bossRepository.getBossById(1) } returns boss1

        useCase()

        coVerify { playerRepository.updateStreak(streak = 1, date = today) }
    }

    @Test
    fun `consecutive day increments streak`() = runTest {
        val player = PlayerState(streak = 3, lastFocusDate = yesterday, currentBossHpRemaining = 100)
        coEvery { playerRepository.getPlayerStateOnce() } returns player
        coEvery { bossRepository.getBossById(1) } returns boss1

        useCase()

        coVerify { playerRepository.updateStreak(streak = 4, date = today) }
    }

    @Test
    fun `same day session does not change streak`() = runTest {
        val player = PlayerState(streak = 5, lastFocusDate = today, currentBossHpRemaining = 100)
        coEvery { playerRepository.getPlayerStateOnce() } returns player
        coEvery { bossRepository.getBossById(1) } returns boss1

        useCase()

        coVerify { playerRepository.updateStreak(streak = 5, date = today) }
    }

    @Test
    fun `gap of 2 days resets streak to 1`() = runTest {
        val player = PlayerState(streak = 10, lastFocusDate = twoDaysAgo, currentBossHpRemaining = 100)
        coEvery { playerRepository.getPlayerStateOnce() } returns player
        coEvery { bossRepository.getBossById(1) } returns boss1

        useCase()

        coVerify { playerRepository.updateStreak(streak = 1, date = today) }
    }

    // ═══════════════════════════════════════════════════════════════
    // BOSS DAMAGE TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun `boss takes 100 damage and survives`() = runTest {
        val player = PlayerState(streak = 1, lastFocusDate = today, currentBossHpRemaining = 300)
        coEvery { playerRepository.getPlayerStateOnce() } returns player
        coEvery { bossRepository.getBossById(3) } returns boss3

        val result = useCase()

        assertTrue(result is com.focusquest.domain.model.BattleResult.DamageDealt)
        val damageResult = result as com.focusquest.domain.model.BattleResult.DamageDealt
        assertEquals(100, damageResult.damage)
        assertEquals(200, damageResult.bossHpRemaining)
    }

    @Test
    fun `boss HP clamps to 0 when damage exceeds remaining`() = runTest {
        val player = PlayerState(streak = 1, lastFocusDate = today, currentBossHpRemaining = 50)
        coEvery { playerRepository.getPlayerStateOnce() } returns player
        coEvery { bossRepository.getBossById(1) } returns boss1

        val result = useCase()

        // Boss should be defeated since HP reached 0
        assertTrue(result is com.focusquest.domain.model.BattleResult.BossDefeated)
        coVerify { bossRepository.markDefeated(1) }
    }

    // ═══════════════════════════════════════════════════════════════
    // BOSS DEFEAT + UNLOCK TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun `boss defeat marks boss defeated and increments counter`() = runTest {
        val player = PlayerState(streak = 1, lastFocusDate = today, currentBossHpRemaining = 100)
        coEvery { playerRepository.getPlayerStateOnce() } returns player
        coEvery { bossRepository.getBossById(1) } returns boss1

        useCase()

        coVerify { bossRepository.markDefeated(1) }
        coVerify { playerRepository.incrementBossesDefeated() }
    }

    @Test
    fun `boss defeat unlocks next boss and updates player state`() = runTest {
        val player = PlayerState(streak = 1, lastFocusDate = today, currentBossHpRemaining = 100)
        coEvery { playerRepository.getPlayerStateOnce() } returns player
        coEvery { bossRepository.getBossById(1) } returns boss1

        useCase()

        coVerify { bossRepository.unlockBoss(2) }
        coVerify { playerRepository.updateCurrentBoss(bossId = 2, hp = 200) }
    }

    @Test
    fun `final boss defeat does not unlock next boss`() = runTest {
        val player = PlayerState(streak = 1, lastFocusDate = today, currentBossHpRemaining = 100, currentBossId = 5)
        coEvery { playerRepository.getPlayerStateOnce() } returns player
        coEvery { bossRepository.getBossById(5) } returns boss5

        val result = useCase()

        assertTrue(result is com.focusquest.domain.model.BattleResult.BossDefeated)
        val defeated = result as com.focusquest.domain.model.BattleResult.BossDefeated
        assertNull(defeated.nextBoss)
    }

    // ═══════════════════════════════════════════════════════════════
    // XP + LEVEL PROGRESSION TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun `XP is awarded on session completion`() = runTest {
        val player = PlayerState(streak = 1, lastFocusDate = today, currentBossHpRemaining = 300, xp = 0, level = 1)
        coEvery { playerRepository.getPlayerStateOnce() } returns player
        coEvery { bossRepository.getBossById(3) } returns boss3

        useCase()

        coVerify { playerRepository.updateXpAndLevel(xp = 100, level = 1) }
    }

    @Test
    fun `level up occurs when XP reaches 300 during session`() = runTest {
        val player = PlayerState(streak = 1, lastFocusDate = today, currentBossHpRemaining = 300, xp = 200, level = 1)
        coEvery { playerRepository.getPlayerStateOnce() } returns player
        coEvery { bossRepository.getBossById(3) } returns boss3

        useCase()

        coVerify { playerRepository.updateXpAndLevel(xp = 0, level = 2) }
    }

    // ═══════════════════════════════════════════════════════════════
    // SESSION RECORD + FOCUS MINUTES TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun `session record is inserted with correct values`() = runTest {
        val player = PlayerState(streak = 1, lastFocusDate = today, currentBossHpRemaining = 300)
        coEvery { playerRepository.getPlayerStateOnce() } returns player
        coEvery { bossRepository.getBossById(3) } returns boss3

        useCase()

        coVerify {
            focusSessionRepository.insert(match {
                it.durationMinutes == 25 &&
                it.completed &&
                it.bossId == 3 &&
                it.damageDealt == 100
            })
        }
    }

    @Test
    fun `focus minutes are added to player total`() = runTest {
        val player = PlayerState(streak = 1, lastFocusDate = today, currentBossHpRemaining = 300)
        coEvery { playerRepository.getPlayerStateOnce() } returns player
        coEvery { bossRepository.getBossById(3) } returns boss3

        useCase()

        coVerify { playerRepository.addFocusSession(25) }
    }

    // ═══════════════════════════════════════════════════════════════
    // BATTLE RESULT TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun `returns DamageDealt when boss survives`() = runTest {
        val player = PlayerState(streak = 1, lastFocusDate = today, currentBossHpRemaining = 300)
        coEvery { playerRepository.getPlayerStateOnce() } returns player
        coEvery { bossRepository.getBossById(3) } returns boss3

        val result = useCase()

        assertTrue(result is com.focusquest.domain.model.BattleResult.DamageDealt)
    }

    @Test
    fun `returns BossDefeated with next boss when boss is defeated`() = runTest {
        val player = PlayerState(streak = 1, lastFocusDate = today, currentBossHpRemaining = 100)
        coEvery { playerRepository.getPlayerStateOnce() } returns player
        coEvery { bossRepository.getBossById(1) } returns boss1

        val result = useCase()

        assertTrue(result is com.focusquest.domain.model.BattleResult.BossDefeated)
        val defeated = result as com.focusquest.domain.model.BattleResult.BossDefeated
        assertEquals(boss1, defeated.boss)
        assertEquals(100, defeated.xpGained)
        assertNotNull(defeated.nextBoss)
        assertEquals(boss2, defeated.nextBoss)
    }
}
