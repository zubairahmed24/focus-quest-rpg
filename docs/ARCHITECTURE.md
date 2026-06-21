# 🏗️ ARCHITECTURE.md

## Overview

Focus Quest RPG uses **MVVM + Clean Architecture** with three layers:

```
┌─────────────────────────────────────────┐
│         PRESENTATION LAYER              │
│  Compose Screens · ViewModels · UI State │
│         (depends on domain)              │
├─────────────────────────────────────────┤
│           DOMAIN LAYER                   │
│  Models · Use Cases · Services           │
│    (pure Kotlin, no Android deps)        │
├─────────────────────────────────────────┤
│            DATA LAYER                    │
│  Room Entities · DAOs · Repositories     │
│         (implements domain)              │
└─────────────────────────────────────────┘
```

## Package Structure

```
com.focusquest/
├── FocusQuestApplication.kt          @HiltAndroidApp
├── MainActivity.kt                   Single activity, Compose host
│
├── data/
│   ├── local/
│   │   ├── FocusQuestDatabase.kt     @Database, pre-population
│   │   ├── entity/
│   │   │   ├── PlayerStateEntity.kt
│   │   │   ├── BossEntity.kt
│   │   │   ├── FocusSessionEntity.kt
│   │   │   └── DailyFocusLogEntity.kt
│   │   └── dao/
│   │       ├── PlayerStateDao.kt
│   │       ├── BossDao.kt
│   │       ├── FocusSessionDao.kt
│   │       └── DailyFocusLogDao.kt
│   ├── repository/
│   │   ├── PlayerRepository.kt
│   │   ├── BossRepository.kt
│   │   └── FocusSessionRepository.kt
│   └── mapper/
│       ├── PlayerStateMapper.kt
│       └── BossMapper.kt
│
├── domain/
│   ├── model/
│   │   ├── PlayerState.kt
│   │   ├── Boss.kt
│   │   ├── FocusSession.kt
│   │   ├── FocusTimerState.kt         sealed: Idle, Focusing, Break, Paused
│   │   └── BattleResult.kt            sealed: DamageDealt, BossDefeated
│   ├── usecase/
│   │   ├── StartFocusSessionUseCase.kt
│   │   ├── CompleteFocusSessionUseCase.kt
│   │   ├── AbandonFocusSessionUseCase.kt
│   │   ├── GetPlayerStateUseCase.kt
│   │   ├── GetCurrentBossUseCase.kt
│   │   ├── GetWeeklyFocusLogUseCase.kt
│   │   └── CheckAndUpdateStreakUseCase.kt
│   └── service/
│       └── FocusTimerService.kt       Timer logic, foreground-aware
│
├── presentation/
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   ├── Type.kt
│   │   └── Shape.kt
│   ├── navigation/
│   │   ├── FocusQuestNavHost.kt
│   │   └── Screen.kt                  sealed: Battle, Victory, Stats
│   ├── battle/
│   │   ├── BattleScreen.kt
│   │   ├── BattleViewModel.kt
│   │   └── components/
│   │       ├── HpBar.kt
│   │       ├── TimerDisplay.kt
│   │       ├── BossSprite.kt
│   │       └── TauntBubble.kt
│   ├── victory/
│   │   ├── VictoryScreen.kt
│   │   └── VictoryViewModel.kt
│   ├── stats/
│   │   ├── StatsScreen.kt
│   │   ├── StatsViewModel.kt
│   │   └── components/
│   │       ├── WeeklyFocusChart.kt
│   │       ├── BossProgressList.kt
│   │       └── StreakDisplay.kt
│   └── components/
│       ├── ShareCard.kt
│       └── ConfirmDialog.kt
│
├── di/
│   ├── DatabaseModule.kt
│   ├── RepositoryModule.kt
│   └── UseCaseModule.kt
│
└── util/
    ├── DateTimeUtils.kt
    └── Constants.kt
```

## Data Models

### Domain Models (Pure Kotlin)

```kotlin
data class PlayerState(
    val id: Long = 1,
    val level: Int = 1,
    val xp: Int = 0,
    val currentBossId: Int = 1,
    val currentBossHpRemaining: Int = 100,
    val streak: Int = 0,
    val lastFocusDate: LocalDate = LocalDate.now(),
    val totalFocusMinutes: Int = 0,
    val totalSessionsCompleted: Int = 0,
    val totalBossesDefeated: Int = 0
)

data class Boss(
    val id: Int,
    val name: String,
    val maxHp: Int,
    val taunt: String,
    val isUnlocked: Boolean,
    val isDefeated: Boolean,
    val order: Int
)

data class FocusSession(
    val id: Long = 0,
    val startTime: LocalDateTime,
    val durationMinutes: Int,
    val completed: Boolean,
    val bossId: Int,
    val damageDealt: Int
)

sealed class FocusTimerState {
    object Idle : FocusTimerState()
    data class Focusing(val remainingSeconds: Int) : FocusTimerState()
    data class Break(val remainingSeconds: Int) : FocusTimerState()
    data class Paused(val remainingSeconds: Int, val wasFocusing: Boolean) : FocusTimerState()
}

sealed class BattleResult {
    data class DamageDealt(val damage: Int, val bossHpRemaining: Int) : BattleResult()
    data class BossDefeated(val boss: Boss, val xpGained: Int, val nextBoss: Boss?) : BattleResult()
}
```

### Room Entities

```kotlin
@Entity(tableName = "player_state")
data class PlayerStateEntity(
    @PrimaryKey val id: Long = 1,
    val level: Int = 1,
    val xp: Int = 0,
    val currentBossId: Int = 1,
    val currentBossHpRemaining: Int = 100,
    val streak: Int = 0,
    val lastFocusDate: String,        // ISO: "2026-06-21"
    val totalFocusMinutes: Int = 0,
    val totalSessionsCompleted: Int = 0,
    val totalBossesDefeated: Int = 0
)

@Entity(tableName = "bosses")
data class BossEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val maxHp: Int,
    val taunt: String,
    val isUnlocked: Boolean,
    val isDefeated: Boolean,
    val order: Int
)

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startTime: String,            // ISO datetime
    val durationMinutes: Int,
    val completed: Boolean,
    val bossId: Int,
    val damageDealt: Int
)

@Entity(tableName = "daily_focus_log")
data class DailyFocusLogEntity(
    @PrimaryKey val date: String,     // ISO: "2026-06-21"
    val totalMinutes: Int,
    val sessionsCompleted: Int
)
```

## Timer Architecture (Critical)

The timer uses **timestamp-based calculation**, NOT a running counter:

```
Session Start:
  1. Save sessionStartTime to Room (ISO datetime)
  2. Set FocusTimerState.Focusing(remainingSeconds = 1500)

App Backgrounded / Killed:
  3. Timer state persists in Room (sessionStartTime survives)

App Foregrounded:
  4. elapsed = now - sessionStartTime
  5. remaining = FOCUS_DURATION - elapsed
  6. If remaining <= 0 → session complete (call CompleteFocusSessionUseCase)
  7. If remaining > 0 → update FocusTimerState.Focusing(remainingSeconds = remaining)

This approach:
  ✅ Survives app kills
  ✅ Survives device reboots
  ✅ Accurate across timezone changes
  ✅ No background service needed
  ✅ No foreground notification needed (MVP)
```

## Navigation

```
NavHost
├── Battle (start destination)
│   └── onBossDefeated → Victory
├── Victory (modal)
│   └── onContinue → popBackStack to Battle
└── Stats
    └── onBack → popBackStack to Battle

Bottom Navigation: Battle (home) | Stats
Victory is navigated to (not a tab), returns to Battle
```

## DI Graph

```
DatabaseModule
  ├── @Provides FocusQuestDatabase
  ├── @Provides PlayerStateDao
  ├── @Provides BossDao
  ├── @Provides FocusSessionDao
  └── @Provides DailyFocusLogDao

RepositoryModule
  ├── @Binds PlayerRepository
  ├── @Binds BossRepository
  └── @Binds FocusSessionRepository

UseCaseModule
  ├── @Provides StartFocusSessionUseCase
  ├── @Provides CompleteFocusSessionUseCase
  ├── @Provides AbandonFocusSessionUseCase
  ├── @Provides GetPlayerStateUseCase
  ├── @Provides GetCurrentBossUseCase
  ├── @Provides GetWeeklyFocusLogUseCase
  └── @Provides CheckAndUpdateStreakUseCase
```

## Constants

```kotlin
object Constants {
    const val FOCUS_DURATION_MINUTES = 25
    const val BREAK_DURATION_MINUTES = 5
    const val DAMAGE_PER_SESSION = 100
    const val XP_PER_SESSION = 100
    const val XP_PER_LEVEL = 300
    const val PLAYER_MAX_HP = 100  // cosmetic only
    const val DATABASE_NAME = "focus_quest_db"
}
```

## Boss Seed Data

```kotlin
val SEED_BOSSES = listOf(
    BossEntity(1, "Slime of Laziness", 100, "Why do today what you can put off...?", true, false, 1),
    BossEntity(2, "Distraction Goblin", 200, "Just one more video, it'll only take a minute...", false, false, 2),
    BossEntity(3, "Procrastination Demon", 300, "I'll start fresh tomorrow, I promise...", false, false, 3),
    BossEntity(4, "Distraction Wraith", 400, "Your phone is buzzing. Better check it now...", false, false, 4),
    BossEntity(5, "Burnout Dragon", 500, "You've done enough. You deserve a break. A permanent one...", false, false, 5)
)
// Boss 1 isUnlocked=true on first launch. Others unlock when previous is defeated.
```

## Build Configuration

```gradle
android {
    compileSdk = 35
    defaultConfig {
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
    }
    buildFeatures { compose = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
```

## Dependencies

```gradle
// Core
implementation("androidx.core:core-ktx:1.15.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
implementation("androidx.activity:activity-compose:1.9.3")

// Compose
implementation(platform("androidx.compose:compose-bom:2024.12.01"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.ui:ui-tooling-preview")
implementation("androidx.navigation:navigation-compose:2.8.5")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

// Hilt
implementation("com.google.dagger:hilt-android:2.53.1")
kapt("com.google.dagger:hilt-android-compiler:2.53.1")
implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

// Room
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")

// Firebase
implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
implementation("com.google.firebase:firebase-analytics")

// Testing
testImplementation("junit:junit:4.13.2")
testImplementation("io.mockk:mockk:1.13.13")
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
testImplementation("app.cash.turbine:turbine:1.2.0")
testImplementation("androidx.room:room-testing:2.6.1")
```