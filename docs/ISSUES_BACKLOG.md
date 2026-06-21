# 📋 GitHub Issues Backlog

> 86 issues across 10 epics. Ready for GitHub Projects import.

---

## Epic 1: Project Setup (E1)

### Issue #1 — Create Gradle project with Compose + Material 3
**Labels:** `epic:project-setup` `type:setup` `priority:p0` `sprint:1`
**Description:** Create new Android Studio project with Kotlin, Jetpack Compose, Material 3. Configure build.gradle.kts with compileSdk 35, minSdk 26, targetSdk 35.
**Acceptance Criteria:**
- [ ] Project created with Kotlin + Compose enabled
- [ ] Material 3 dependency added
- [ ] `./gradlew assembleDebug` succeeds
- [ ] App launches to blank screen
**Priority:** P0
**Dependencies:** None

### Issue #2 — Add Hilt, Room, Navigation, Firebase dependencies
**Labels:** `epic:project-setup` `type:setup` `priority:p0` `sprint:1`
**Description:** Add all required dependencies to build.gradle.kts: Hilt 2.53.1, Room 2.6.1, Navigation Compose 2.8.5, Firebase BOM 33.7.0, testing libs (MockK, Turbine, coroutines-test).
**Acceptance Criteria:**
- [ ] All dependencies added and resolve correctly
- [ ] Hilt annotation processing works (kapt)
- [ ] Room compiler works (kapt)
- [ ] `./gradlew assembleDebug` succeeds with all deps
**Priority:** P0
**Dependencies:** #1

### Issue #3 — Create package structure
**Labels:** `epic:project-setup` `type:setup` `priority:p0` `sprint:1`
**Description:** Create the full package structure per ARCHITECTURE.md: data/local/{entity,dao}, data/repository, data/mapper, domain/{model,usecase,service}, presentation/{theme,navigation,battle/components,victory,stats/components,components}, di, util.
**Acceptance Criteria:**
- [ ] All packages created
- [ ] .gitkeep or placeholder file in each package
- [ ] Structure matches ARCHITECTURE.md exactly
**Priority:** P0
**Dependencies:** #1

### Issue #4 — Set up FocusQuestApplication + MainActivity
**Labels:** `epic:project-setup` `type:setup` `priority:p0` `sprint:1`
**Description:** Create FocusQuestApplication.kt with @HiltAndroidApp. Create MainActivity.kt with @AndroidEntryPoint, setContent { FocusQuestTheme { FocusQuestNavHost() } }. Register application in AndroidManifest.xml.
**Acceptance Criteria:**
- [ ] FocusQuestApplication has @HiltAndroidApp
- [ ] MainActivity has @AndroidEntryPoint
- [ ] AndroidManifest references FocusQuestApplication
- [ ] App launches without crash
**Priority:** P0
**Dependencies:** #2, #3

### Issue #5 — Create Material 3 theme (dark mode RPG aesthetic)
**Labels:** `epic:project-setup` `type:ui` `priority:p0` `sprint:1`
**Description:** Create Color.kt (deep purple #1A1A2E, gold accents #FFD700), Theme.kt (dark mode default, Material 3 dynamic color disabled), Type.kt (Material 3 typography), Shape.kt.
**Acceptance Criteria:**
- [ ] Color palette defined (deep purple, gold, red for HP, green for XP)
- [ ] Theme supports dark mode (default)
- [ ] Typography defined
- [ ] Theme applied to app
**Priority:** P0
**Dependencies:** #4

### Issue #6 — Create NavHost with 3 empty screens
**Labels:** `epic:project-setup` `type:navigation` `priority:p0` `sprint:1`
**Description:** Create Screen.kt sealed class (Battle, Victory, Stats). Create FocusQuestNavHost.kt with NavHost, start destination = Battle. Add bottom navigation (Battle | Stats). Victory is navigated to, not a tab.
**Acceptance Criteria:**
- [ ] Screen sealed class with 3 routes
- [ ] NavHost configured with 3 composable destinations
- [ ] Bottom navigation with 2 tabs (Battle, Stats)
- [ ] Navigation between screens works
**Priority:** P0
**Dependencies:** #5

### Issue #7 — Set up Firebase project + config
**Labels:** `epic:project-setup` `type:analytics` `priority:p1` `sprint:1`
**Description:** Create Firebase project, add google-services.json, configure Firebase Analytics. Add analytics dependency. Create AnalyticsUtil for logging events.
**Acceptance Criteria:**
- [ ] Firebase project created
- [ ] google-services.json added to app/
- [ ] Firebase Analytics initialized
- [ ] Test event logs successfully
**Priority:** P1
**Dependencies:** #2

---

## Epic 2: Data Persistence (E2)

### Issue #8 — Create Room entities (4 tables)
**Labels:** `epic:data-persistence` `type:data` `priority:p0` `sprint:1`
**Description:** Create PlayerStateEntity, BossEntity, FocusSessionEntity, DailyFocusLogEntity per ARCHITECTURE.md. All fields, @PrimaryKey, @Entity annotations.
**Acceptance Criteria:**
- [ ] All 4 entity classes compile
- [ ] @Entity annotations with correct table names
- [ ] @PrimaryKey annotations correct
- [ ] Field types match ARCHITECTURE.md
**Priority:** P0
**Dependencies:** #3

### Issue #9 — Create DAOs with all queries
**Labels:** `epic:data-persistence` `type:data` `priority:p0` `sprint:1`
**Description:** Create PlayerStateDao, BossDao, FocusSessionDao, DailyFocusLogDao with all queries per ARCHITECTURE.md. Flow for live data, suspend for one-shot.
**Acceptance Criteria:**
- [ ] All 4 DAO interfaces compile
- [ ] Flow<T> used for observable queries
- [ ] suspend fun used for one-shot operations
- [ ] All queries from ARCHITECTURE.md implemented
**Priority:** P0
**Dependencies:** #8

### Issue #10 — Create FocusQuestDatabase + pre-population callback
**Labels:** `epic:data-persistence` `type:data` `priority:p0` `sprint:1`
**Description:** Create FocusQuestDatabase.kt with @Database(entities, version=1). Add Callback that pre-populates 5 bosses (SEED_BOSSES) and initial PlayerState on first launch.
**Acceptance Criteria:**
- [ ] Database class compiles with @Database
- [ ] Pre-population callback seeds 5 bosses
- [ ] Pre-population callback creates initial player state
- [ ] Boss 1 isUnlocked=true in seed data
**Priority:** P0
**Dependencies:** #9

### Issue #11 — Create DatabaseModule (Hilt)
**Labels:** `epic:data-persistence` `type:di` `priority:p0` `sprint:1`
**Description:** Create DatabaseModule.kt with @Module. @Provides for FocusQuestDatabase, PlayerStateDao, BossDao, FocusSessionDao, DailyFocusLogDao.
**Acceptance Criteria:**
- [ ] Hilt module compiles
- [ ] All DAOs injectable
- [ ] Database injectable
- [ ] Pre-population callback executes on first launch
**Priority:** P0
**Dependencies:** #10

### Issue #12 — Create entity ↔ domain mappers
**Labels:** `epic:data-persistence` `type:data` `priority:p0` `sprint:1`
**Description:** Create PlayerStateMapper (Entity↔Domain), BossMapper (Entity↔Domain). Handle ISO string ↔ LocalDate/LocalDateTime conversion.
**Acceptance Criteria:**
- [ ] Mappers convert entities to domain models
- [ ] Mappers convert domain models to entities
- [ ] Date string conversion handled correctly
- [ ] Unit tests for mappers pass
**Priority:** P0
**Dependencies:** #8

### Issue #13 — Write BossDao unit tests
**Labels:** `epic:data-persistence` `type:testing` `priority:p1` `sprint:1`
**Description:** Write unit tests for BossDao: insert all bosses, query all, query by id, mark defeated, unlock boss.
**Acceptance Criteria:**
- [ ] Test: insertAll → getAllBosses returns 5
- [ ] Test: getBossById(1) returns correct boss
- [ ] Test: markDefeated(1) → isDefeated=true
- [ ] Test: unlockBoss(2) → isUnlocked=true
- [ ] All tests pass
**Priority:** P1
**Dependencies:** #11

### Issue #14 — Write PlayerStateDao unit tests
**Labels:** `epic:data-persistence` `type:testing` `priority:p1` `sprint:1`
**Description:** Write unit tests for PlayerStateDao: insert, query, update XP/level, update boss, update streak, add focus session.
**Acceptance Criteria:**
- [ ] Test: insert → getPlayerState returns state
- [ ] Test: updateXpAndLevel → values updated
- [ ] Test: updateCurrentBoss → values updated
- [ ] Test: updateStreak → values updated
- [ ] Test: addFocusSession → totals incremented
- [ ] All tests pass
**Priority:** P1
**Dependencies:** #11

### Issue #15 — Create domain models
**Labels:** `epic:data-persistence` `type:data` `priority:p0` `sprint:1`
**Description:** Create domain models: PlayerState, Boss, FocusSession, FocusTimerState (sealed), BattleResult (sealed). Pure Kotlin, no Android imports.
**Acceptance Criteria:**
- [ ] All domain models compile
- [ ] No Android framework imports in domain/
- [ ] Sealed classes for FocusTimerState and BattleResult
- [ ] Data classes for PlayerState, Boss, FocusSession
**Priority:** P0
**Dependencies:** #3

### Issue #16 — Create repositories (3)
**Labels:** `epic:data-persistence` `type:data` `priority:p0` `sprint:1`
**Description:** Create PlayerRepository, BossRepository, FocusSessionRepository. Each wraps DAOs, returns domain models via mappers.
**Acceptance Criteria:**
- [ ] All 3 repositories compile
- [ ] Repositories return domain models (not entities)
- [ ] Flow<T> preserved for observable data
- [ ] suspend fun for one-shot operations
**Priority:** P0
**Dependencies:** #12, #15

### Issue #17 — Create use cases (7)
**Labels:** `epic:data-persistence` `type:logic` `priority:p0` `sprint:1`
**Description:** Create all 7 use cases: StartFocusSession, CompleteFocusSession, AbandonFocusSession, GetPlayerState, GetCurrentBoss, GetWeeklyFocusLog, CheckAndUpdateStreak.
**Acceptance Criteria:**
- [ ] All 7 use cases compile
- [ ] Each use case has single invoke() operator function
- [ ] Use cases depend on repositories (not DAOs)
- [ ] No Android imports in use cases
**Priority:** P0
**Dependencies:** #16

### Issue #18 — Create RepositoryModule + UseCaseModule (Hilt)
**Labels:** `epic:data-persistence` `type:di` `priority:p0` `sprint:1`
**Description:** Create RepositoryModule (@Binds for 3 repos) and UseCaseModule (@Provides for 7 use cases).
**Acceptance Criteria:**
- [ ] Both Hilt modules compile
- [ ] All repositories injectable
- [ ] All use cases injectable
- [ ] App launches with full DI graph
**Priority:** P0
**Dependencies:** #17

### Issue #19 — Write use case unit tests
**Labels:** `epic:data-persistence` `type:testing` `priority:p1` `sprint:1`
**Description:** Write unit tests for CompleteFocusSessionUseCase (XP increases, boss HP decreases, boss defeated when HP=0) and CheckAndUpdateStreakUseCase (consecutive day, gap, same-day, first-ever).
**Acceptance Criteria:**
- [ ] Test: complete session → XP +100, boss HP -100
- [ ] Test: boss HP reaches 0 → marked defeated
- [ ] Test: streak +1 on consecutive day
- [ ] Test: streak resets on gap
- [ ] Test: streak unchanged on same-day
- [ ] Test: first-ever session → streak = 1
- [ ] All tests pass
**Priority:** P1
**Dependencies:** #17

---

## Epic 3: Focus Timer Engine (E3)

### Issue #20 — Create FocusTimerService with coroutine timer
**Labels:** `epic:focus-timer` `type:timer` `priority:p0` `sprint:1`
**Description:** Create FocusTimerService in domain/service. Manages timer state machine. Uses timestamp-based calculation (NOT running counter). Emits FocusTimerState via StateFlow.
**Acceptance Criteria:**
- [ ] Service manages Idle/Focusing/Break/Paused states
- [ ] StateFlow<FocusTimerState> emitted
- [ ] startFocus() → Focusing(1500)
- [ ] startBreak() → Break(300)
- [ ] pause() → Paused
- [ ] resume() → returns to previous state
- [ ] abandon() → Idle
**Priority:** P0
**Dependencies:** #15

### Issue #21 — Implement timer state machine transitions
**Labels:** `epic:focus-timer` `type:timer` `priority:p0` `sprint:1`
**Description:** Implement all state transitions: Idle→Focusing, Focusing→Break (on complete), Focusing→Paused, Paused→Focusing, Break→Idle (on complete), Break→Paused, Paused→Break, Focusing→Idle (abandon).
**Acceptance Criteria:**
- [ ] All transitions implemented and tested
- [ ] No invalid transitions possible
- [ ] State machine unit tests pass
**Priority:** P0
**Dependencies:** #20

### Issue #22 — Handle app backgrounding (save timestamp)
**Labels:** `epic:focus-timer` `type:timer` `priority:p0` `sprint:1`
**Description:** On app backgrounding (onStop/onPause), save current session start time to Room. Timer state persists.
**Acceptance Criteria:**
- [ ] Session start time saved to Room on background
- [ ] Timer state saved to Room on background
- [ ] No data lost on background
**Priority:** P0
**Dependencies:** #20

### Issue #23 — Handle app foregrounding (calculate elapsed)
**Labels:** `epic:focus-timer` `type:timer` `priority:p0` `sprint:1`
**Description:** On app foregrounding (onStart/onResume), calculate elapsed time from saved timestamp. Update remaining time. If elapsed >= duration, trigger session completion.
**Acceptance Criteria:**
- [ ] Elapsed time calculated correctly from timestamp
- [ ] Remaining time updated on foreground
- [ ] If session complete during background → CompleteFocusSessionUseCase called
- [ ] Timer displays correct remaining time after backgrounding
**Priority:** P0
**Dependencies:** #22

### Issue #24 — Handle screen off (timestamp-based continuation)
**Labels:** `epic:focus-timer` `type:timer` `priority:p0` `sprint:1`
**Description:** Timer continues during screen off using timestamp math. No wake lock needed for MVP (timestamp calculation on next foreground).
**Acceptance Criteria:**
- [ ] Screen off → timer continues via timestamp
- [ ] Screen on → correct remaining time shown
- [ ] No wake lock required
**Priority:** P0
**Dependencies:** #23

### Issue #25 — Write timer unit tests
**Labels:** `epic:focus-timer` `type:testing` `priority:p1` `sprint:1`
**Description:** Write unit tests for FocusTimerService: start, pause, resume, complete, abandon, state transitions.
**Acceptance Criteria:**
- [ ] Test: start → Focusing state
- [ ] Test: pause → Paused state
- [ ] Test: resume → correct state restored
- [ ] Test: complete → triggers callback
- [ ] Test: abandon → Idle, no damage
- [ ] All tests pass
**Priority:** P1
**Dependencies:** #21

### Issue #26 — Handle app kill during session
**Labels:** `epic:focus-timer` `type:timer` `priority:p0` `sprint:1`
**Description:** If app is killed during a focus session, on next launch detect incomplete session from Room and restore timer state.
**Acceptance Criteria:**
- [ ] App killed during focus → reopen → timer restored
- [ ] If elapsed >= 25 min during kill → session auto-completes
- [ ] If elapsed < 25 min → timer shows remaining time
**Priority:** P0
**Dependencies:** #23

---

## Epic 4: Battle System (E4)

### Issue #27 — Create BattleScreen layout
**Labels:** `epic:battle-system` `type:ui` `priority:p0` `sprint:1`
**Description:** Create BattleScreen.kt with: player level/XP at top, streak indicator, boss name + sprite, boss HP bar, player HP bar (cosmetic), timer display, START/PAUSE/GIVE UP buttons, boss taunt bubble.
**Acceptance Criteria:**
- [ ] Screen layout matches mockup
- [ ] All elements visible and positioned correctly
- [ ] Responsive on different screen sizes
**Priority:** P0
**Dependencies:** #6

### Issue #28 — Create HpBar composable
**Labels:** `epic:battle-system` `type:component` `priority:p0` `sprint:1`
**Description:** Create HpBar.kt reusable composable. Animated fill, color changes (green > 50%, yellow 25-50%, red < 25%). Label with current/max HP.
**Acceptance Criteria:**
- [ ] HP bar fills proportionally
- [ ] Color changes based on HP percentage
- [ ] Smooth animation on HP change
- [ ] Current/max HP text displayed
**Priority:** P0
**Dependencies:** #27

### Issue #29 — Create TimerDisplay composable
**Labels:** `epic:battle-system` `type:component` `priority:p0` `sprint:1`
**Description:** Create TimerDisplay.kt. Shows MM:SS countdown. Circular progress ring around the number. Ring depletes as time decreases.
**Acceptance Criteria:**
- [ ] MM:SS format displayed
- [ ] Circular progress ring animates
- [ ] Ring color matches theme
- [ ] Updates every second
**Priority:** P0
**Dependencies:** #27

### Issue #30 — Create BossSprite composable
**Labels:** `epic:battle-system` `type:component` `priority:p1` `sprint:1`
**Description:** Create BossSprite.kt. Placeholder boss visual (emoji or simple vector graphic for MVP). Shake animation on damage.
**Acceptance Criteria:**
- [ ] Boss visual displayed (emoji or vector)
- [ ] Shake animation on damage
- [ ] Fade animation on defeat
**Priority:** P1
**Dependencies:** #27

### Issue #31 — Create TauntBubble composable
**Labels:** `epic:battle-system` `type:component` `priority:p1` `sprint:1`
**Description:** Create TauntBubble.kt. Speech bubble showing boss taunt text. Appears above boss sprite.
**Acceptance Criteria:**
- [ ] Speech bubble displays taunt text
- [ ] Positioned above boss sprite
- [ ] Text wraps correctly
**Priority:** P1
**Dependencies:** #27

### Issue #32 — Wire BattleViewModel to BattleScreen
**Labels:** `epic:battle-system` `type:viewmodel` `priority:p0` `sprint:1`
**Description:** Create BattleViewModel with BattleUiState. Wire to GetPlayerStateUseCase, GetCurrentBossUseCase, FocusTimerService. Expose StateFlow<BattleUiState>.
**Acceptance Criteria:**
- [ ] BattleViewModel @HiltViewModel
- [ ] StateFlow<BattleUiState> exposed
- [ ] UI state contains: timerState, playerLevel, playerXp, currentBoss, bossHp, streak
- [ ] Screen collects state with collectAsStateWithLifecycle()
**Priority:** P0
**Dependencies:** #27, #20

### Issue #33 — Implement START/PAUSE/GIVE UP buttons
**Labels:** `epic:battle-system` `type:ui` `priority:p0` `sprint:1`
**Description:** Wire buttons to BattleViewModel actions. START → startFocus(). PAUSE → pause(). RESUME → resume(). GIVE UP → confirmation dialog → abandon().
**Acceptance Criteria:**
- [ ] START FOCUSING button starts timer
- [ ] PAUSE button pauses timer, shows RESUME
- [ ] GIVE UP shows confirmation dialog
- [ ] Confirm GIVE UP → resets to Idle, no damage
- [ ] Button states change based on timer state
**Priority:** P0
**Dependencies:** #32

### Issue #34 — Add damage animation (boss shake)
**Labels:** `epic:battle-system` `type:animation` `priority:p1` `sprint:2`
**Description:** When focus session completes and damage is dealt, boss sprite shakes. Floating "+100 DMG" text appears and fades.
**Acceptance Criteria:**
- [ ] Boss shakes on damage
- [ ] "+100 DMG" floats up and fades
- [ ] Animation duration ~1 second
**Priority:** P1
**Dependencies:** #30, #32

### Issue #35 — Implement boss progression logic
**Labels:** `epic:battle-system` `type:logic` `priority:p0` `sprint:1`
**Description:** In CompleteFocusSessionUseCase: when boss HP reaches 0, mark defeated, unlock next boss, update player state. Handle "all bosses defeated" state.
**Acceptance Criteria:**
- [ ] Boss HP reaches 0 → marked defeated in DB
- [ ] Next boss auto-unlocked
- [ ] Player state currentBossId updated
- [ ] All 5 bosses defeated → campaign complete state
- [ ] Integration test: full campaign flow passes
**Priority:** P0
**Dependencies:** #17

---

## Epic 5: Progression System (E5)

### Issue #36 — Implement XP gain (+100 per session)
**Labels:** `epic:progression` `type:logic` `priority:p0` `sprint:1`
**Description:** In CompleteFocusSessionUseCase: add +100 XP to player state on session completion.
**Acceptance Criteria:**
- [ ] Complete session → XP increases by 100
- [ ] XP persists in Room
**Priority:** P0
**Dependencies:** #35

### Issue #37 — Implement level up (every 300 XP)
**Labels:** `epic:progression` `type:logic` `priority:p0` `sprint:1`
**Description:** When XP >= 300, level +1, XP -= 300. Handle multiple level-ups in one session (if XP > 600).
**Acceptance Criteria:**
- [ ] XP reaches 300 → level +1, XP resets
- [ ] Multiple level-ups handled (XP > 600)
- [ ] Level persists in Room
**Priority:** P0
**Dependencies:** #36

### Issue #38 — Create level up animation
**Labels:** `epic:progression` `type:animation` `priority:p2` `sprint:2`
**Description:** When player levels up, show burst animation (scaling number + glow effect) on battle screen.
**Acceptance Criteria:**
- [ ] Level up triggers burst animation
- [ ] Animation is smooth (no jank)
- [ ] Animation auto-dismisses after 2 seconds
**Priority:** P2
**Dependencies:** #37

### Issue #39 — Implement streak logic
**Labels:** `epic:progression` `type:logic` `priority:p0` `sprint:1`
**Description:** In CheckAndUpdateStreakUseCase: streak +1 if lastFocusDate is yesterday. Reset to 1 if gap > 1 day. Unchanged if same day. First-ever session → streak = 1.
**Acceptance Criteria:**
- [ ] First session ever → streak = 1
- [ ] Session on consecutive day → streak +1
- [ ] Session after 2-day gap → streak = 1
- [ ] Multiple sessions same day → streak unchanged
- [ ] Streak persists in Room
**Priority:** P0
**Dependencies:** #17

### Issue #40 — Write streak unit tests
**Labels:** `epic:progression` `type:testing` `priority:p1` `sprint:1`
**Description:** Write comprehensive unit tests for streak logic: first-ever, consecutive, gap, same-day, multi-day gap.
**Acceptance Criteria:**
- [ ] Test: first-ever → streak = 1
- [ ] Test: consecutive day → streak +1
- [ ] Test: 2-day gap → streak = 1
- [ ] Test: same-day → streak unchanged
- [ ] Test: 3-day gap → streak = 1
- [ ] All tests pass
**Priority:** P1
**Dependencies:** #39

### Issue #41 — Display streak on battle screen
**Labels:** `epic:progression` `type:ui` `priority:p0` `sprint:1`
**Description:** Show streak counter with 🔥 emoji on battle screen header. Update in real-time.
**Acceptance Criteria:**
- [ ] Streak displayed with 🔥 emoji
- [ ] Updates after session completion
- [ ] Visible at top of battle screen
**Priority:** P0
**Dependencies:** #39, #32

---

## Epic 6: Victory & Boss Defeat Flow (E6)

### Issue #42 — Create VictoryScreen layout
**Labels:** `epic:victory-flow` `type:ui` `priority:p0` `sprint:2`
**Description:** Create VictoryScreen.kt: "BOSS DEFEATED!" header, defeated boss name, XP gained, level (with level-up indicator), next boss reveal (name + HP + taunt), START NEXT BATTLE button, TAKE A BREAK button, share button.
**Acceptance Criteria:**
- [ ] Layout shows all elements
- [ ] Responsive on different screens
- [ ] Buttons clearly visible
**Priority:** P0
**Dependencies:** #35

### Issue #43 — Create VictoryViewModel
**Labels:** `epic:victory-flow` `type:viewmodel` `priority:p0` `sprint:2`
**Description:** Create VictoryViewModel with VictoryUiState. Derives from player state + defeated boss + next boss.
**Acceptance Criteria:**
- [ ] VictoryViewModel @HiltViewModel
- [ ] StateFlow<VictoryUiState> exposed
- [ ] State contains: defeatedBossName, xpGained, newLevel, leveledUp, nextBossName, nextBossHp, isFinalBoss
**Priority:** P0
**Dependencies:** #42

### Issue #44 — Wire Battle → Victory → Battle navigation
**Labels:** `epic:victory-flow` `type:navigation` `priority:p0` `sprint:2`
**Description:** When boss defeated, navigate to Victory screen. From Victory, START NEXT BATTLE → popBackStack to Battle. TAKE A BREAK → popBackStack + start break timer.
**Acceptance Criteria:**
- [ ] Boss defeated → Victory screen appears
- [ ] START NEXT BATTLE → returns to Battle with new boss
- [ ] TAKE A BREAK → returns to Battle + break timer starts
- [ ] No navigation stack issues
**Priority:** P0
**Dependencies:** #42, #43

### Issue #45 — Add next boss reveal on victory screen
**Labels:** `epic:victory-flow` `type:ui` `priority:p0` `sprint:2`
**Description:** Show next boss name, HP, and taunt on victory screen. Build anticipation.
**Acceptance Criteria:**
- [ ] Next boss name displayed
- [ ] Next boss HP shown
- [ ] Next boss taunt shown
- [ ] If final boss defeated → "Campaign Complete!" message
**Priority:** P0
**Dependencies:** #43

### Issue #46 — Add victory animation
**Labels:** `epic:victory-flow` `type:animation` `priority:p2` `sprint:2`
**Description:** Boss fade-out animation + next boss appear animation on victory screen.
**Acceptance Criteria:**
- [ ] Defeated boss fades out
- [ ] Next boss appears with animation
- [ ] No jank on mid-range devices
**Priority:** P2
**Dependencies:** #42

### Issue #47 — Handle final boss victory (campaign complete)
**Labels:** `epic:victory-flow` `type:ui` `priority:p0` `sprint:2`
**Description:** When Boss 5 (Burnout Dragon) is defeated, show special "Campaign Complete!" victory screen. "More bosses coming soon" message.
**Acceptance Criteria:**
- [ ] Final boss defeat → special victory screen
- [ ] "Campaign Complete!" message
- [ ] "More bosses coming soon" text
- [ ] Player can still use timer (free focus mode)
**Priority:** P0
**Dependencies:** #45

### Issue #48 — Add share button on victory screen
**Labels:** `epic:victory-flow` `type:ui` `priority:p1` `sprint:2`
**Description:** Add share button that triggers share card generation and Android share sheet.
**Acceptance Criteria:**
- [ ] Share button visible on victory screen
- [ ] Tapping triggers share card generation
- [ ] Android share sheet opens
- [ ] Firebase "share_clicked" event logged
**Priority:** P1
**Dependencies:** #42, #55

---

## Epic 7: Stats & Progress Display (E7)

### Issue #49 — Create StatsScreen layout
**Labels:** `epic:stats-display` `type:ui` `priority:p0` `sprint:2`
**Description:** Create StatsScreen.kt: level, total focus time, bosses defeated (X/5), streak, weekly bar chart, boss progress list, share button.
**Acceptance Criteria:**
- [ ] Layout shows all elements
- [ ] Responsive on different screens
- [ ] Back navigation to Battle
**Priority:** P0
**Dependencies:** #6

### Issue #50 — Create WeeklyFocusChart composable
**Labels:** `epic:stats-display` `type:component` `priority:p0` `sprint:2`
**Description:** Create WeeklyFocusChart.kt. 7 bars (Mon-Sun) showing focus minutes. Bars scale to max value. Days with no focus show empty bar.
**Acceptance Criteria:**
- [ ] 7 bars displayed (Mon-Sun)
- [ ] Bars scale correctly (max = full height)
- [ ] Empty days show minimal bar
- [ ] Labels for each day
- [ ] Focus minutes shown on/near bars
**Priority:** P0
**Dependencies:** #49

### Issue #51 — Create BossProgressList composable
**Labels:** `epic:stats-display` `type:component` `priority:p0` `sprint:2`
**Description:** Create BossProgressList.kt. Shows all 5 bosses with ✅ (defeated) / 🔒 (locked) / ⚔️ (current) icons.
**Acceptance Criteria:**
- [ ] All 5 bosses listed
- [ ] Defeated bosses show ✅
- [ ] Locked bosses show 🔒
- [ ] Current boss shows ⚔️
- [ ] Boss names displayed
**Priority:** P0
**Dependencies:** #49

### Issue #52 — Create StreakDisplay composable
**Labels:** `epic:stats-display` `type:component` `priority:p0` `sprint:2`
**Description:** Create StreakDisplay.kt. Shows current streak with 🔥 + best streak.
**Acceptance Criteria:**
- [ ] Current streak displayed with 🔥
- [ ] Best streak displayed
- [ ] Visual emphasis on current streak
**Priority:** P0
**Dependencies:** #49

### Issue #53 — Create StatsViewModel
**Labels:** `epic:stats-display` `type:viewmodel` `priority:p0` `sprint:2`
**Description:** Create StatsViewModel with StatsUiState. Combines GetPlayerStateUseCase + GetWeeklyFocusLogUseCase. Formats total focus time as "Xh Ym".
**Acceptance Criteria:**
- [ ] StatsViewModel @HiltViewModel
- [ ] StateFlow<StatsUiState> exposed
- [ ] State contains: level, totalFocusTimeFormatted, bossesDefeated, streak, weeklyData, bossProgress
- [ ] Focus time formatted as "Xh Ym"
**Priority:** P0
**Dependencies:** #49

### Issue #54 — Add share button on stats screen
**Labels:** `epic:stats-display` `type:ui` `priority:p1` `sprint:2`
**Description:** Add share button that triggers share card generation and Android share sheet.
**Acceptance Criteria:**
- [ ] Share button visible on stats screen
- [ ] Tapping triggers share card generation
- [ ] Android share sheet opens
- [ ] Firebase "share_clicked" event logged
**Priority:** P1
**Dependencies:** #49, #55

---

## Epic 8: Social Sharing (E8)

### Issue #55 — Create ShareCard composable
**Labels:** `epic:social-sharing` `type:component` `priority:p1` `sprint:2`
**Description:** Create ShareCard.kt. Designed for screenshot capture. Shows: app logo, level, streak 🔥, total focus time, bosses defeated. RPG-styled card.
**Acceptance Criteria:**
- [ ] Card renders with all data
- [ ] RPG visual style (matches theme)
- [ ] Correct aspect ratio for social sharing
- [ ] No clipping or overflow
**Priority:** P1
**Dependencies:** #53

### Issue #56 — Implement Bitmap capture from Compose
**Labels:** `epic:social-sharing` `type:ui` `priority:p1` `sprint:2`
**Description:** Capture ShareCard composable as Bitmap. Use ComposeView → Bitmap approach. Handle correct resolution.
**Acceptance Criteria:**
- [ ] Composable captured as Bitmap
- [ ] Correct resolution (1080x1920 or similar)
- [ ] No clipping
- [ ] Try-catch for failure cases
**Priority:** P1
**Dependencies:** #55

### Issue #57 — Save bitmap to cache + FileProvider URI
**Labels:** `epic:social-sharing` `type:ui` `priority:p1` `sprint:2`
**Description:** Save captured bitmap to cache directory. Get FileProvider URI for sharing. Configure FileProvider in manifest.
**Acceptance Criteria:**
- [ ] Bitmap saved to cache dir
- [ ] FileProvider configured in manifest
- [ ] URI obtained correctly
- [ ] No file permission errors on Android 13+
**Priority:** P1
**Dependencies:** #56

### Issue #58 — Create share intent (ACTION_SEND)
**Labels:** `epic:social-sharing` `type:ui` `priority:p1` `sprint:2`
**Description:** Create ACTION_SEND intent with image URI + text. Open Android share sheet.
**Acceptance Criteria:**
- [ ] Share intent created with image + text
- [ ] Android share sheet opens
- [ ] Works with Instagram, X, LinkedIn, WhatsApp
**Priority:** P1
**Dependencies:** #57

### Issue #59 — Test sharing to Instagram/X/LinkedIn/WhatsApp
**Labels:** `epic:social-sharing` `type:testing` `priority:p1` `sprint:2`
**Description:** Test share functionality across major platforms. Verify image appears correctly.
**Acceptance Criteria:**
- [ ] Image appears in Instagram Stories
- [ ] Image appears in X post
- [ ] Image appears in LinkedIn post
- [ ] Image appears in WhatsApp
**Priority:** P1
**Dependencies:** #58

### Issue #60 — Add Firebase share_clicked event
**Labels:** `epic:social-sharing` `type:analytics` `priority:p1` `sprint:2`
**Description:** Log "share_clicked" event to Firebase Analytics when share button is tapped. Include params: screen (victory/stats), level, streak.
**Acceptance Criteria:**
- [ ] Event logged on share tap
- [ ] Params included (screen, level, streak)
- [ ] Event visible in Firebase console
**Priority:** P1
**Dependencies:** #7, #58

---

## Epic 9: Polish & Animations (E9)

### Issue #61 — Add floating damage text animation
**Labels:** `epic:polish` `type:animation` `priority:p2` `sprint:2`
**Description:** "+100 DMG" text floats up from boss and fades out on damage.
**Acceptance Criteria:**
- [ ] Text appears on damage
- [ ] Floats up smoothly
- [ ] Fades out
- [ ] No jank
**Priority:** P2
**Dependencies:** #34

### Issue #62 — Add HP bar drain animation
**Labels:** `epic:polish` `type:animation` `priority:p2` `sprint:2`
**Description:** HP bar animates smoothly when decreasing (300ms drain, not instant).
**Acceptance Criteria:**
- [ ] HP bar animates on decrease
- [ ] 300ms duration
- [ ] Smooth, no jank
**Priority:** P2
**Dependencies:** #28

### Issue #63 — Add timer ring pulse
**Labels:** `epic:polish` `type:animation` `priority:p2` `sprint:2`
**Description:** Timer ring pulses subtly during focus session to indicate active state.
**Acceptance Criteria:**
- [ ] Ring pulses during focus
- [ ] Subtle, not distracting
- [ ] Stops when paused/idle
**Priority:** P2
**Dependencies:** #29

### Issue #64 — Add level-up burst
**Labels:** `epic:polish` `type:animation` `priority:p2` `sprint:2`
**Description:** Level-up burst animation (scaling + glow) on battle screen.
**Acceptance Criteria:**
- [ ] Burst plays on level up
- [ ] Auto-dismisses after 2 seconds
- [ ] No jank
**Priority:** P2
**Dependencies:** #38

### Issue #65 — Polish color scheme + dark mode
**Labels:** `epic:polish` `type:ui` `priority:p1` `sprint:2`
**Description:** Final color pass. Ensure dark mode looks good. RPG aesthetic: deep purples, gold accents, red HP, green XP.
**Acceptance Criteria:**
- [ ] Colors consistent across all screens
- [ ] Dark mode looks polished
- [ ] No contrast issues
- [ ] RPG aesthetic achieved
**Priority:** P1
**Dependencies:** #5

### Issue #66 — Add haptic feedback
**Labels:** `epic:polish` `type:ui` `priority:p2` `sprint:2`
**Description:** Add haptic feedback on boss damage, level up, and boss defeat.
**Acceptance Criteria:**
- [ ] Haptic on boss damage
- [ ] Haptic on level up
- [ ] Haptic on boss defeat
- [ ] Not excessive
**Priority:** P2
**Dependencies:** #34

### Issue #67 — Handle midnight edge case
**Labels:** `epic:polish` `type:logic` `priority:p1` `sprint:2`
**Description:** If focus session crosses midnight, streak should update to the new day, not the day the session started.
**Acceptance Criteria:**
- [ ] Session crossing midnight → streak uses new day
- [ ] Daily focus log attributes to correct day
- [ ] No streak duplication
**Priority:** P1
**Dependencies:** #39

### Issue #68 — Optimize recompositions
**Labels:** `epic:polish` `type:performance` `priority:p1` `sprint:2`
**Description:** Audit Compose recompositions. Use stable params, derivedStateOf, remember correctly. Check with Layout Inspector.
**Acceptance Criteria:**
- [ ] No unnecessary recompositions
- [ ] Stable params on all composables
- [ ] derivedStateOf used for computed values
- [ ] Layout Inspector shows minimal recompositions
**Priority:** P1
**Dependencies:** #32

### Issue #69 — Add "Are you sure?" dialog on GIVE UP
**Labels:** `epic:polish` `type:ui` `priority:p1` `sprint:2`
**Description:** Confirmation dialog when user taps GIVE UP during focus session. "Are you sure? Your progress in this session will be lost."
**Acceptance Criteria:**
- [ ] Dialog appears on GIVE UP tap
- [ ] "Are you sure?" message
- [ ] Confirm → abandon session
- [ ] Cancel → return to focusing
**Priority:** P1
**Dependencies:** #33

---

## Epic 10: Play Store Launch (E10)

### Issue #70 — Create app icon (adaptive)
**Labels:** `epic:launch` `type:assets` `priority:p0` `sprint:2`
**Description:** Create 512x512 PNG app icon + adaptive icon XML. Shield + sword + clock concept. Deep purple background, gold shield.
**Acceptance Criteria:**
- [ ] 512x512 PNG created
- [ ] Adaptive icon XML configured
- [ ] Icon looks good at 48x48 and 512x512
- [ ] RPG aesthetic (not productivity app look)
**Priority:** P0
**Dependencies:** None

### Issue #71 — Create feature graphic
**Labels:** `epic:launch` `type:assets` `priority:p0` `sprint:2`
**Description:** Create 1024x500 feature graphic for Play Store. Show battle screen mockup + tagline.
**Acceptance Criteria:**
- [ ] 1024x500 PNG created
- [ ] Shows app concept clearly
- [ ] Tagline: "Defeat procrastination. One focus session at a time."
**Priority:** P0
**Dependencies:** #70

### Issue #72 — Create 3 screenshots
**Labels:** `epic:launch` `type:assets` `priority:p0` `sprint:2`
**Description:** Create 3 Play Store screenshots (1080x1920): 1) Battle screen with timer, 2) Victory screen with level-up, 3) Stats screen with weekly chart.
**Acceptance Criteria:**
- [ ] 3 screenshots at 1080x1920
- [ ] Screenshot 1: Battle screen + "Focus for 25 minutes. Defeat the boss."
- [ ] Screenshot 2: Victory + "Level up. Unlock new bosses. Keep your streak."
- [ ] Screenshot 3: Stats + "Track your focus. Build the habit."
**Priority:** P0
**Dependencies:** None

### Issue #73 — Write store listing
**Labels:** `epic:launch` `type:assets` `priority:p0` `sprint:2`
**Description:** Write Play Store title (≤30 chars), short description (≤80 chars), full description (≤4000 chars). ASO keywords: focus, pomodoro, timer, productivity, RPG, quest, boss battle.
**Acceptance Criteria:**
- [ ] Title ≤ 30 characters
- [ ] Short description ≤ 80 characters
- [ ] Full description ≤ 4000 characters
- [ ] ASO keywords included
**Priority:** P0
**Dependencies:** None

### Issue #74 — Create privacy policy page
**Labels:** `epic:launch` `type:assets` `priority:p0` `sprint:2`
**Description:** Create simple privacy policy page (Carrd or hosted). State: no personal data collected, Firebase Analytics uses anonymous data, all data stored locally.
**Acceptance Criteria:**
- [ ] Privacy policy page is live
- [ ] URL accessible
- [ ] States no personal data collected
- [ ] Mentions Firebase Analytics
**Priority:** P0
**Dependencies:** None

### Issue #75 — Configure Play Console
**Labels:** `epic:launch` `type:launch` `priority:p0` `sprint:2`
**Description:** Configure Play Console: app details, content rating questionnaire, data safety form, target audience.
**Acceptance Criteria:**
- [ ] App details filled
- [ ] Content rating completed
- [ ] Data safety form completed
- [ ] Target audience set
**Priority:** P0
**Dependencies:** #73, #74

### Issue #76 — Set up internal testing track
**Labels:** `epic:launch` `type:launch` `priority:p0` `sprint:2`
**Description:** Create internal testing track in Play Console. Prepare for 12-tester requirement.
**Acceptance Criteria:**
- [ ] Internal testing track created
- [ ] Track configured
**Priority:** P0
**Dependencies:** #75

### Issue #77 — Prepare 12 testers
**Labels:** `epic:launch` `type:launch` `priority:p0` `sprint:2`
**Description:** Recruit 12 testers: 4 from r/productivity, 3 from r/GetStudying, 2 from personal network, 2 from r/PlayMyGame, 1 from X. Collect email addresses.
**Acceptance Criteria:**
- [ ] 12 testers identified with emails
- [ ] All have Android devices (8.0+)
- [ ] All willing to complete 5-question survey
**Priority:** P0
**Dependencies:** None

### Issue #78 — Create landing page
**Labels:** `epic:launch` `type:assets` `priority:p1` `sprint:2`
**Description:** Create simple Carrd landing page: app concept, screenshots, email capture for updates.
**Acceptance Criteria:**
- [ ] Landing page live
- [ ] App concept clearly explained
- [ ] Screenshots shown
- [ ] Email capture working
**Priority:** P1
**Dependencies:** #72

### Issue #79 — Draft launch posts
**Labels:** `epic:launch` `type:launch` `priority:p1` `sprint:2`
**Description:** Draft posts for Reddit (r/productivity, r/GetStudying, r/PlayMyGame), X, LinkedIn, Product Hunt. Do NOT post yet.
**Acceptance Criteria:**
- [ ] Reddit posts drafted (3 communities)
- [ ] X post drafted
- [ ] LinkedIn post drafted
- [ ] Product Hunt draft prepared
**Priority:** P1
**Dependencies:** None

### Issue #80 — Upload AAB to Play Console
**Labels:** `epic:launch` `type:launch` `priority:p0` `sprint:2`
**Description:** Build release AAB. Upload to Play Console internal testing track.
**Acceptance Criteria:**
- [ ] Release AAB builds successfully
- [ ] AAB uploaded to Play Console
- [ ] No upload errors
**Priority:** P0
**Dependencies:** #76, #80

### Issue #81 — Review pre-launch report
**Labels:** `epic:launch` `type:testing` `priority:p0` `sprint:2`
**Description:** Review Google Play pre-launch report. Fix any critical issues.
**Acceptance Criteria:**
- [ ] Pre-launch report reviewed
- [ ] No critical crashes
- [ ] Any warnings addressed
**Priority:** P0
**Dependencies:** #80

### Issue #82 — Add testers + send opt-in link
**Labels:** `epic:launch` `type:launch` `priority:p0` `sprint:2`
**Description:** Add 12 testers to internal testing track. Send opt-in link + installation instructions.
**Acceptance Criteria:**
- [ ] 12 testers added to track
- [ ] Opt-in link sent to all testers
- [ ] Testers can install app
**Priority:** P0
**Dependencies:** #77, #81

### Issue #83 — Post launch announcements
**Labels:** `epic:launch` `type:launch` `priority:p1` `sprint:2`
**Description:** Post launch announcements on Reddit, X, LinkedIn. Share build-in-public journey.
**Acceptance Criteria:**
- [ ] Reddit posts live (3 communities)
- [ ] X post live
- [ ] LinkedIn post live
**Priority:** P1
**Dependencies:** #82

### Issue #84 — Share 3 Shorts
**Labels:** `epic:launch` `type:launch` `priority:p1` `sprint:2`
**Description:** Create and share 3 Shorts: 1) Battle screen demo, 2) Boss defeat + victory, 3) Stats + streak. Post to TikTok, YouTube Shorts, Instagram Reels.
**Acceptance Criteria:**
- [ ] 3 Shorts created
- [ ] Posted to TikTok
- [ ] Posted to YouTube Shorts
- [ ] Posted to Instagram Reels
**Priority:** P1
**Dependencies:** #82

### Issue #85 — Set up feedback channel + survey
**Labels:** `epic:launch` `type:launch` `priority:p0` `sprint:2`
**Description:** Create Google Form survey (5 questions). Set up Discord server or feedback email.
**Acceptance Criteria:**
- [ ] Google Form created with 5 questions
- [ ] Discord server or feedback email set up
- [ ] Survey link ready to send
**Priority:** P0
**Dependencies:** None

### Issue #86 — Send Day 1 survey to testers
**Labels:** `epic:launch` `type:launch` `priority:p0` `sprint:2`
**Description:** After 24 hours of testing, send survey to all testers. Collect feedback.
**Acceptance Criteria:**
- [ ] Survey sent 24 hours after launch
- [ ] At least 5 responses collected within 48 hours
- [ ] Feedback reviewed and categorized
**Priority:** P0
**Dependencies:** #82, #85