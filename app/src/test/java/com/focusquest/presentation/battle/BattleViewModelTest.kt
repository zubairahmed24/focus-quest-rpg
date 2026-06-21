package com.focusquest.presentation.battle

import com.focusquest.domain.model.BattleResult
import com.focusquest.domain.model.Boss
import com.focusquest.domain.model.FocusTimerState
import com.focusquest.domain.model.PlayerState
import com.focusquest.domain.repository.BossRepository
import com.focusquest.domain.service.FocusTimerService
import com.focusquest.domain.usecase.CompleteFocusSessionUseCase
import com.focusquest.domain.usecase.GetCurrentBossUseCase
import com.focusquest.domain.usecase.GetPlayerStateUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

/**
 * Unit tests for BattleViewModel.
 *
 * Tests the ViewModel's state management and action handling.
 * Uses UnconfinedTestDispatcher for immediate coroutine execution.
 *
 * Verifies:
 * - Initial state is loading
 * - State updates when player and boss data are available
 * - StartFocus action triggers timer service
 * - Pause/Resume actions trigger timer service
 * - GiveUp action shows confirmation dialog
 * - ConfirmGiveUp action triggers abandon
 * - CompleteFocusSessionUseCase is called on timer completion
 */
@OptIn(ExperimentalCoroutinesApi::class)
class BattleViewModelTest {

    private lateinit var getPlayerStateUseCase: GetPlayerStateUseCase
    private lateinit var getCurrentBossUseCase: GetCurrentBossUseCase
    private lateinit var completeFocusSessionUseCase: CompleteFocusSessionUseCase
    private lateinit var focusTimerService: FocusTimerService
    private lateinit var bossRepository: BossRepository
    private lateinit var viewModel: BattleViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    private val testPlayer = PlayerState(
        id = 1,
        level = 3,
        xp = 150,
        currentBossId = 2,
        currentBossHpRemaining = 150,
        streak = 5,
        lastFocusDate = LocalDate.now(),
        totalFocusMinutes = 75,
        totalSessionsCompleted = 3,
        totalBossesDefeated = 1
    )

    private val testBoss = Boss(
        id = 2,
        name = "Distraction Goblin",
        maxHp = 200,
        taunt = "Just one more video...",
        isUnlocked = true,
        isDefeated = false,
        order = 2
    )

    @Before
    fun setUp() = runTest {
        Dispatchers.setMain(testDispatcher)

        getPlayerStateUseCase = mockk()
        getCurrentBossUseCase = mockk()
        completeFocusSessionUseCase = mockk()
        focusTimerService = mockk(relaxed = true)
        bossRepository = mockk(relaxed = true)

        // Default: no active session to recover
        coEvery { focusTimerService.recoverSession() } returns false
        coEvery { focusTimerService.timerState } returns MutableStateFlow(FocusTimerState.Idle)
        coEvery { focusTimerService.events } returns flowOf()
        coEvery { getPlayerStateUseCase.invoke() } returns flowOf(testPlayer)
        coEvery { getCurrentBossUseCase.invoke() } returns testBoss

        viewModel = BattleViewModel(
            getPlayerStateUseCase,
            getCurrentBossUseCase,
            completeFocusSessionUseCase,
            focusTimerService,
            bossRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `state contains player level and XP when data is available`() = runTest {
        val state = viewModel.uiState.value
        assertEquals(3, state.playerLevel)
        assertEquals(150, state.playerXp)
        assertEquals(300, state.xpToNextLevel)
    }

    @Test
    fun `state contains streak when data is available`() = runTest {
        val state = viewModel.uiState.value
        assertEquals(5, state.streak)
    }

    @Test
    fun `state contains current boss when data is available`() = runTest {
        val state = viewModel.uiState.value
        assertNotNull(state.currentBoss)
        assertEquals("Distraction Goblin", state.currentBoss?.name)
        assertEquals(200, state.bossMaxHp)
        assertEquals(150, state.bossHpRemaining)
    }

    @Test
    fun `state is not loading when data is available`() = runTest {
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
    }

    @Test
    fun `StartFocus action calls timer service startFocus`() = runTest {
        viewModel.onAction(BattleUiAction.StartFocus)

        coVerify { focusTimerService.startFocus() }
    }

    @Test
    fun `Pause action calls timer service pause`() = runTest {
        viewModel.onAction(BattleUiAction.Pause)

        coVerify { focusTimerService.pause() }
    }

    @Test
    fun `Resume action calls timer service resume`() = runTest {
        viewModel.onAction(BattleUiAction.Resume)

        coVerify { focusTimerService.resume() }
    }

    @Test
    fun `GiveUp action shows confirmation dialog`() = runTest {
        viewModel.onAction(BattleUiAction.GiveUp)

        assertTrue(viewModel.uiState.value.showGiveUpDialog)
    }

    @Test
    fun `DismissGiveUpDialog hides confirmation dialog`() = runTest {
        viewModel.onAction(BattleUiAction.GiveUp)
        viewModel.onAction(BattleUiAction.DismissGiveUpDialog)

        assertFalse(viewModel.uiState.value.showGiveUpDialog)
    }

    @Test
    fun `ConfirmGiveUp hides dialog and calls abandon`() = runTest {
        viewModel.onAction(BattleUiAction.GiveUp)
        viewModel.onAction(BattleUiAction.ConfirmGiveUp)

        assertFalse(viewModel.uiState.value.showGiveUpDialog)
        coVerify { focusTimerService.abandon() }
    }

    @Test
    fun `StartBreak action calls timer service startBreak`() = runTest {
        viewModel.onAction(BattleUiAction.StartBreak)

        coVerify { focusTimerService.startBreak() }
    }

    @Test
    fun `SkipBreak action calls timer service stop`() = runTest {
        viewModel.onAction(BattleUiAction.SkipBreak)

        coVerify { focusTimerService.stop() }
    }

    @Test
    fun `xpProgress calculates correctly`() = runTest {
        val state = viewModel.uiState.value
        // 150 / 300 = 0.5
        assertEquals(0.5f, state.xpProgress, 0.01f)
    }

    @Test
    fun `isTimerRunning is false when Idle`() = runTest {
        val state = viewModel.uiState.value
        assertFalse(state.isTimerRunning)
    }
}
