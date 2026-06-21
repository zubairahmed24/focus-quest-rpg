package com.focusquest.di

import android.content.Context
import androidx.room.Room
import com.focusquest.data.local.FocusQuestDatabase
import com.focusquest.data.local.dao.BossDao
import com.focusquest.data.local.dao.DailyFocusLogDao
import com.focusquest.data.local.dao.FocusSessionDao
import com.focusquest.data.local.dao.PlayerStateDao
import com.focusquest.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing the Room database and all DAO instances.
 *
 * The database is a singleton scoped to the application.
 * Pre-population callback seeds 5 bosses + initial player state on first creation.
 *
 * fallbackToDestructiveMigration is used for MVP — acceptable because there
 * are no production users yet. Proper migrations must be added before v1.1.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFocusQuestDatabase(
        @ApplicationContext context: Context
    ): FocusQuestDatabase {
        return Room.databaseBuilder(
            context,
            FocusQuestDatabase::class.java,
            Constants.DATABASE_NAME
        )
            .addCallback(FocusQuestDatabase.prepopulateCallback())
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providePlayerStateDao(database: FocusQuestDatabase): PlayerStateDao {
        return database.playerStateDao()
    }

    @Provides
    fun provideBossDao(database: FocusQuestDatabase): BossDao {
        return database.bossDao()
    }

    @Provides
    fun provideFocusSessionDao(database: FocusQuestDatabase): FocusSessionDao {
        return database.focusSessionDao()
    }

    @Provides
    fun provideDailyFocusLogDao(database: FocusQuestDatabase): DailyFocusLogDao {
        return database.dailyFocusLogDao()
    }
}
