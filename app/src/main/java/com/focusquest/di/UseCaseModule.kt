package com.focusquest.di

import com.focusquest.domain.usecase.AwardXpUseCase
import com.focusquest.domain.usecase.CompleteFocusSessionUseCase
import com.focusquest.domain.usecase.GetCurrentBossUseCase
import com.focusquest.domain.usecase.GetPlayerStateUseCase
import com.focusquest.domain.usecase.GetRecentSessionsUseCase
import com.focusquest.domain.usecase.GetWeeklyFocusStatsUseCase
import com.focusquest.domain.usecase.UnlockNextBossUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing all use case instances.
 *
 * Use cases are @Singleton because they hold no mutable state —
 * they are stateless orchestrators over repositories.
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetPlayerStateUseCase(
        playerRepository: com.focusquest.domain.repository.PlayerRepository
    ): GetPlayerStateUseCase = GetPlayerStateUseCase(playerRepository)

    @Provides
    @Singleton
    fun provideGetCurrentBossUseCase(
        bossRepository: com.focusquest.domain.repository.BossRepository
    ): GetCurrentBossUseCase = GetCurrentBossUseCase(bossRepository)

    @Provides
    @Singleton
    fun provideGetRecentSessionsUseCase(
        focusSessionRepository: com.focusquest.domain.repository.FocusSessionRepository
    ): GetRecentSessionsUseCase = GetRecentSessionsUseCase(focusSessionRepository)

    @Provides
    @Singleton
    fun provideGetWeeklyFocusStatsUseCase(
        focusSessionRepository: com.focusquest.domain.repository.FocusSessionRepository
    ): GetWeeklyFocusStatsUseCase = GetWeeklyFocusStatsUseCase(focusSessionRepository)

    @Provides
    @Singleton
    fun provideUnlockNextBossUseCase(
        bossRepository: com.focusquest.domain.repository.BossRepository,
        playerRepository: com.focusquest.domain.repository.PlayerRepository
    ): UnlockNextBossUseCase = UnlockNextBossUseCase(bossRepository, playerRepository)

    @Provides
    @Singleton
    fun provideAwardXpUseCase(
        playerRepository: com.focusquest.domain.repository.PlayerRepository
    ): AwardXpUseCase = AwardXpUseCase(playerRepository)

    @Provides
    @Singleton
    fun provideCompleteFocusSessionUseCase(
        playerRepository: com.focusquest.domain.repository.PlayerRepository,
        bossRepository: com.focusquest.domain.repository.BossRepository,
        focusSessionRepository: com.focusquest.domain.repository.FocusSessionRepository
    ): CompleteFocusSessionUseCase = CompleteFocusSessionUseCase(
        playerRepository, bossRepository, focusSessionRepository
    )
}
