# 🏃 Sprint 1 — Days 1-7: Foundation → Core Game Loop

> **Goal:** By end of Day 7, the core game loop is playable: start timer → focus → damage boss → defeat boss → XP/level → streak → next boss.

## Sprint 1 Scope

| Day | Focus | Issues | Deliverable |
|-----|-------|--------|-------------|
| 1 | Project Skeleton | #1-#7 | Compiling app with Hilt, Room, Navigation, Theme |
| 2 | Room Database | #8-#14 | All entities, DAOs, database, seed data, DAO tests |
| 3 | Domain Layer | #15-#19 | Domain models, repositories, use cases, use case tests |
| 4 | Focus Timer Engine | #20-#26 | Timestamp-based timer, backgrounding, app kill recovery |
| 5 | Battle Screen UI | #27-#33 | Full battle screen with timer, HP bars, buttons |
| 6 | Boss Progression | #35 | Boss defeat → unlock next → campaign logic |
| 7 | XP, Leveling, Streaks | #36-#41 | XP gain, level up, streak logic, streak tests, UI display |

## Day-by-Day Tasks

### Day 1: Project Skeleton (7.5h)
| Task | Issue | Hours | Shippable? |
|------|-------|-------|------------|
| Create Gradle project with Compose + Material 3 | #1 | 0.5 | ✅ Compiles |
| Add Hilt, Room, Navigation, Firebase deps | #2 | 1.0 | ✅ Deps resolve |
| Create package structure | #3 | 1.0 | ✅ Folders exist |
| Set up FocusQuestApplication + MainActivity | #4 | 1.0 | ✅ App launches |
| Create Material 3 theme | #5 | 1.0 | ✅ Theme applies |
| Create NavHost with 3 empty screens | #6 | 1.0 | ✅ Nav works |
| Set up Firebase project | #7 | 1.0 | ✅ Analytics logs |
| Git commit + push | — | 1.0 | ✅ Repo updated |

**Verification:**
- [ ] `./gradlew assembleDebug` succeeds
- [ ] App launches to Battle tab (empty screen)
- [ ] Bottom nav switches between Battle and Stats
- [ ] Hilt processes without errors
- [ ] Firebase test event logs

---

### Day 2: Room Database (7.5h)
| Task | Issue | Hours | Shippable? |
|------|-------|-------|------------|
| Create 4 Room entities | #8 | 1.5 | ✅ Entities compile |
| Create 4 DAOs with all queries | #9 | 2.0 | ✅ DAOs compile |
| Create FocusQuestDatabase + seed callback | #10 | 1.0 | ✅ DB builds |
| Create DatabaseModule (Hilt) | #11 | 0.5 | ✅ DI works |
| Create entity ↔ domain mappers | #12 | 1.0 | ✅ Mappers compile |
| Write BossDao unit tests | #13 | 0.5 | ✅ Tests pass |
| Write PlayerStateDao unit tests | #14 | 0.5 | ✅ Tests pass |
| Git commit | — | 0.5 | ✅ Repo updated |

**Verification:**
- [ ] All entities compile with @Entity
- [ ] All DAOs compile with correct queries
- [ ] Database pre-populates 5 bosses on first launch
- [ ] Boss 1 isUnlocked=true in seed
- [ ] All DAO unit tests pass
- [ ] Hilt provides database injection

---

### Day 3: Domain Layer (7.5h)
| Task | Issue | Hours | Shippable? |
|------|-------|-------|------------|
| Create domain models (5) | #15 | 1.0 | ✅ Models compile |
| Create 3 repositories | #16 | 1.5 | ✅ Repos compile |
| Create 7 use cases | #17 | 2.5 | ✅ Use cases compile |
| Create RepositoryModule + UseCaseModule | #18 | 0.5 | ✅ DI works |
| Write use case unit tests | #19 | 1.5 | ✅ Tests pass |
| Git commit | — | 0.5 | ✅ Repo updated |

**Verification:**
- [ ] No Android imports in domain/ layer
- [ ] All use cases injectable via Hilt
- [ ] Test: complete session → XP +100, boss HP -100
- [ ] Test: boss HP = 0 → marked defeated
- [ ] Test: streak +1 on consecutive day
- [ ] Test: streak resets on gap
- [ ] All use case tests pass

---

### Day 4: Focus Timer Engine (7.5h)
| Task | Issue | Hours | Shippable? |
|------|-------|-------|------------|
| Create FocusTimerService | #20 | 2.0 | ✅ Service compiles |
| Implement state machine transitions | #21 | 1.0 | ✅ Transitions work |
| Handle app backgrounding | #22 | 1.0 | ✅ State persists |
| Handle app foregrounding | #23 | 1.0 | ✅ Elapsed calculated |
| Handle screen off | #24 | 0.5 | ✅ Timestamp works |
| Handle app kill during session | #26 | 1.0 | ✅ State restored |
| Write timer unit tests | #25 | 1.0 | ✅ Tests pass |

**Verification:**
- [ ] Timer starts at 25:00, counts down
- [ ] App backgrounded 5 min → foreground → shows 20:00
- [ ] Screen off → on → correct time shown
- [ ] App killed → reopen → timer restored
- [ ] If elapsed >= 25 min during kill → auto-complete
- [ ] All state transitions tested
- [ ] Pause/resume/abandon work correctly

---

### Day 5: Battle Screen UI (7.5h)
| Task | Issue | Hours | Shippable? |
|------|-------|-------|------------|
| Create BattleScreen layout | #27 | 1.5 | ✅ Screen renders |
| Create HpBar composable | #28 | 1.0 | ✅ HP bar works |
| Create TimerDisplay composable | #29 | 1.0 | ✅ Timer displays |
| Create BossSprite composable | #30 | 0.5 | ✅ Boss shows |
| Create TauntBubble composable | #31 | 0.5 | ✅ Taunt shows |
| Create BattleViewModel | #32 | 1.5 | ✅ State flows |
| Implement START/PAUSE/GIVE UP | #33 | 1.0 | ✅ Buttons work |
| Git commit | — | 0.5 | ✅ Repo updated |

**Verification:**
- [ ] Battle screen shows boss name, HP bar, taunt
- [ ] START FOCUSING → timer countdown begins
- [ ] Timer shows MM:SS + progress ring
- [ ] PAUSE → pauses, shows RESUME
- [ ] GIVE UP → confirmation → resets to Idle
- [ ] Timer completes → boss HP decreases by 100
- [ ] Player level + XP shown at top
- [ ] Streak shown at top

---

### Day 6: Boss Progression (7.5h)
| Task | Issue | Hours | Shippable? |
|------|-------|-------|------------|
| Implement boss progression in use case | #35 | 2.0 | ✅ Progression works |
| Boss defeated → mark in DB | #35 | 1.0 | ✅ DB updates |
| Unlock next boss | #35 | 1.0 | ✅ Next boss available |
| Campaign complete state | #35 | 1.0 | ✅ End state handled |
| Integration test: full campaign | #35 | 1.5 | ✅ Test passes |
| Git commit | — | 1.0 | ✅ Repo updated |

**Verification:**
- [ ] Defeat boss 1 → boss 2 unlocked
- [ ] Boss 2 requires 2 sessions (200 HP)
- [ ] Defeat all 5 → campaign complete
- [ ] Campaign complete → "More bosses coming soon"
- [ ] Player state tracks totalBossesDefeated
- [ ] Full campaign integration test passes

---

### Day 7: XP, Leveling, Streaks (7.5h)
| Task | Issue | Hours | Shippable? |
|------|-------|-------|------------|
| Implement XP gain | #36 | 0.5 | ✅ XP increases |
| Implement level up | #37 | 1.0 | ✅ Level increases |
| Implement streak logic | #39 | 1.5 | ✅ Streak works |
| Write streak unit tests | #40 | 1.0 | ✅ Tests pass |
| Display streak on battle screen | #41 | 0.5 | ✅ Streak visible |
| Sprint 1 integration test | — | 2.0 | ✅ Full loop works |
| Git commit + tag v0.1.0-sprint1 | — | 1.0 | ✅ Tagged |

**Verification:**
- [ ] Complete session → XP +100
- [ ] XP reaches 300 → level up, XP resets
- [ ] First session → streak = 1
- [ ] Consecutive day → streak +1
- [ ] 2-day gap → streak = 1
- [ ] Same-day → streak unchanged
- [ ] Streak 🔥 visible on battle screen
- [ ] **FULL LOOP TEST:** Start → focus → damage → defeat → XP → level → streak → next boss → repeat

---

## Sprint 1 Exit Criteria

All must be met to proceed to Sprint 2:

- [ ] App compiles and launches
- [ ] Timer works (start, pause, resume, complete, abandon)
- [ ] Timer survives backgrounding + app kill
- [ ] Battle screen displays boss, HP, timer, buttons
- [ ] Focus session completion deals 100 damage
- [ ] Boss defeat unlocks next boss
- [ ] XP gained per session, levels up at 300
- [ ] Streak increments on consecutive days
- [ ] All unit tests pass
- [ ] Full campaign (5 bosses) completable
- [ ] Git tagged as v0.1.0-sprint1