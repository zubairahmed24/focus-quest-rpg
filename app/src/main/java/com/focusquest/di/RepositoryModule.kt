package com.focusquest.di

import com.focusquest.data.repository.BossRepositoryImpl
import com.focusquest.data.repository.FocusSessionRepositoryImpl
import com.focusquest.data.repository.PlayerRepositoryImpl
import com.focusquest.domain.repository.BossRepository
import com.focusquest.domain.repository.FocusSessionRepository
import com.focusquest.domain.repository.PlayerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module binding repository interfaces to their concrete implementations.
 *
 * Uses @Binds (not @Provides) because we're binding an interface to an impl —
 * this is more efficient than @Provides and the standard Hilt pattern for
 * interface-to-implementation binding.
 *
 * All repositories are @Singleton because they wrap singleton DAOs.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPlayerRepository(
        impl: PlayerRepositoryImpl
    ): PlayerRepository

    @Binds
    @Singleton
    abstract fun bindBossRepository(
        impl: BossRepositoryImpl
    ): BossRepository

    @Binds
    @Singleton
    abstract fun bindFocusSessionRepository(
        impl: FocusSessionRepositoryImpl
    ): FocusSessionRepository
}
