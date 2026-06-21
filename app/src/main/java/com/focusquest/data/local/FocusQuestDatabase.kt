package com.focusquest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.focusquest.data.local.entity.BossEntity
import com.focusquest.data.local.entity.DailyFocusLogEntity
import com.focusquest.data.local.entity.FocusSessionEntity
import com.focusquest.data.local.entity.PlayerStateEntity
import com.focusquest.data.local.dao.BossDao
import com.focusquest.data.local.dao.DailyFocusLogDao
import com.focusquest.data.local.dao.FocusSessionDao
import com.focusquest.data.local.dao.PlayerStateDao
import com.focusquest.util.Constants
import com.focusquest.util.DateTimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Room database for Focus Quest RPG.
 *
 * Contains 4 tables:
 *  - player_state (singleton row, id = 1)
 *  - bosses (5 pre-populated procrastination bosses)
 *  - focus_sessions (individual session records)
 *  - daily_focus_log (daily aggregated minutes for weekly chart)
 *
 * On first creation, the database is pre-populated with:
 *  - 5 boss entities (Boss 1 unlocked, Bosses 2-5 locked)
 *  - Initial player state (level 1, 0 XP, boss 1, 0 streak, no active session)
 */
@Database(
    entities = [
        PlayerStateEntity::class,
        BossEntity::class,
        FocusSessionEntity::class,
        DailyFocusLogEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class FocusQuestDatabase : RoomDatabase() {

    abstract fun playerStateDao(): PlayerStateDao
    abstract fun bossDao(): BossDao
    abstract fun focusSessionDao(): FocusSessionDao
    abstract fun dailyFocusLogDao(): DailyFocusLogDao

    companion object {

        /**
         * Pre-population callback — runs once when the database is first created.
         * Seeds 5 bosses and initial player state.
         */
        fun prepopulateCallback(): Callback = object : Callback() {
            private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                scope.launch {
                    seedBosses(db)
                    seedPlayerState(db)
                }
            }

            private fun seedBosses(db: SupportSQLiteDatabase) {
                val bosses = listOf(
                    BossEntity(1, "Slime of Laziness", 100, "Why do today what you can put off...?", true, false, 1),
                    BossEntity(2, "Distraction Goblin", 200, "Just one more video, it'll only take a minute...", false, false, 2),
                    BossEntity(3, "Procrastination Demon", 300, "I'll start fresh tomorrow, I promise...", false, false, 3),
                    BossEntity(4, "Distraction Wraith", 400, "Your phone is buzzing. Better check it now...", false, false, 4),
                    BossEntity(5, "Burnout Dragon", 500, "You've done enough. You deserve a break. A permanent one...", false, false, 5)
                )

                bosses.forEach { boss ->
                    db.execSQL(
                        "INSERT INTO bosses (id, name, maxHp, taunt, isUnlocked, isDefeated, `order`) VALUES (?, ?, ?, ?, ?, ?, ?)",
                        arrayOf(
                            boss.id,
                            boss.name,
                            boss.maxHp,
                            boss.taunt,
                            if (boss.isUnlocked) 1 else 0,
                            if (boss.isDefeated) 1 else 0,
                            boss.order
                        )
                    )
                }
            }

            private fun seedPlayerState(db: SupportSQLiteDatabase) {
                val today = DateTimeUtils.formatDate(LocalDate.now())
                db.execSQL(
                    "INSERT INTO player_state (id, level, xp, currentBossId, currentBossHpRemaining, streak, lastFocusDate, totalFocusMinutes, totalSessionsCompleted, totalBossesDefeated, activeSessionStartTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    arrayOf(1L, 1, 0, 1, 100, 0, today, 0, 0, 0, null)
                )
            }
        }
    }
}
