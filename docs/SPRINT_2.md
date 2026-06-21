# 🏃 Sprint 2 — Days 8-14: Victory → Stats → Polish → Launch

> **Goal:** By end of Day 14, app is in closed testing on Play Store with 12 testers.

## Sprint 2 Scope

| Day | Focus | Issues | Deliverable |
|-----|-------|--------|-------------|
| 8 | Victory Screen | #42-#45, #47 | Boss defeat → victory → next boss flow |
| 9 | Stats Screen | #49-#53 | Stats with weekly chart, boss progress, streak |
| 10 | Share Card | #55-#60 | Share card generation + sharing works |
| 11 | Polish & Animations | #34, #61-#69 | Animations, edge cases, recomposition optimization |
| 12 | Testing & Bug Fixes | — | Full campaign test, balance, release build |
| 13 | Play Store Assets | #70-#79 | Icon, screenshots, store listing, testers recruited |
| 14 | Closed Testing Launch | #80-#86 | AAB uploaded, testers onboarded, posts live |

## Day-by-Day Tasks

### Day 8: Victory Screen (7.5h)
| Task | Issue | Hours | Shippable? |
|------|-------|-------|------------|
| Create VictoryScreen layout | #42 | 1.5 | ✅ Screen renders |
| Create VictoryViewModel | #43 | 1.0 | ✅ State flows |
| Wire Battle → Victory → Battle nav | #44 | 1.0 | ✅ Navigation works |
| Add next boss reveal | #45 | 1.0 | ✅ Next boss shown |
| Handle final boss victory | #47 | 1.0 | ✅ Campaign complete |
| Add share button (stub) | #48 | 0.5 | ✅ Button visible |
| Git commit | — | 1.5 | ✅ Repo updated |

**Verification:**
- [ ] Boss defeated → Victory screen appears
- [ ] Shows: boss name, XP gained, level, next boss
- [ ] START NEXT BATTLE → Battle with new boss
- [ ] TAKE A BREAK → Battle + break timer
- [ ] Final boss → "Campaign Complete!"
- [ ] No navigation stack issues

---

### Day 9: Stats Screen (7.5h)
| Task | Issue | Hours | Shippable? |
|------|-------|-------|------------|
| Create StatsScreen layout | #49 | 1.5 | ✅ Screen renders |
| Create WeeklyFocusChart | #50 | 2.0 | ✅ Chart renders |
| Create BossProgressList | #51 | 1.0 | ✅ List shows |
| Create StreakDisplay | #52 | 0.5 | ✅ Streak shows |
| Create StatsViewModel | #53 | 1.5 | ✅ State flows |
| Add share button (stub) | #54 | 0.5 | ✅ Button visible |
| Git commit | — | 0.5 | ✅ Repo updated |

**Verification:**
- [ ] Stats screen shows: level, total focus time, bosses X/5, streak
- [ ] Weekly chart: 7 bars, correct scaling
- [ ] Boss progress: ✅/🔒/⚔️ icons
- [ ] Focus time formatted as "Xh Ym"
- [ ] Stats update after completing sessions

---

### Day 10: Share Card (7.5h)
| Task | Issue | Hours | Shippable? |
|------|-------|-------|------------|
| Create ShareCard composable | #55 | 2.0 | ✅ Card renders |
| Implement Bitmap capture | #56 | 2.0 | ✅ Bitmap captured |
| Save to cache + FileProvider URI | #57 | 1.0 | ✅ URI obtained |
| Create share intent | #58 | 0.5 | ✅ Share sheet opens |
| Test sharing to platforms | #59 | 1.0 | ✅ Sharing works |
| Add Firebase event | #60 | 0.5 | ✅ Event logs |
| Wire share buttons | #48, #54 | 0.5 | ✅ Buttons work |

**Verification:**
- [ ] Share card shows: logo, level, streak, focus time, bosses
- [ ] Bitmap captured at correct resolution
- [ ] Share sheet opens with image
- [ ] Image appears in Instagram/X/LinkedIn/WhatsApp
- [ ] Firebase logs "share_clicked"

---

### Day 11: Polish & Animations (7.5h)
| Task | Issue | Hours | Shippable? |
|------|-------|-------|------------|
| Add boss shake + damage text | #34, #61 | 1.5 | ✅ Animations play |
| Add HP bar drain animation | #62 | 0.5 | ✅ Smooth drain |
| Add timer ring pulse | #63 | 0.5 | ✅ Pulse works |
| Add level-up burst | #38, #64 | 1.0 | ✅ Burst plays |
| Polish color scheme | #65 | 1.0 | ✅ Looks good |
| Add haptic feedback | #66 | 0.5 | ✅ Haptics fire |
| Handle midnight edge case | #67 | 0.5 | ✅ Streak correct |
| Optimize recompositions | #68 | 1.0 | ✅ No excess recomp |
| Add GIVE UP confirmation | #69 | 0.5 | ✅ Dialog works |

**Verification:**
- [ ] Boss shakes on damage
- [ ] "+100 DMG" floats and fades
- [ ] HP bar drains smoothly (300ms)
- [ ] Timer ring pulses during focus
- [ ] Level-up burst plays
- [ ] Dark mode looks polished
- [ ] Haptics on damage/level/defeat
- [ ] Midnight crossing → streak correct
- [ ] No unnecessary recompositions
- [ ] GIVE UP shows confirmation

---

### Day 12: Testing & Bug Fixes (7.5h)
| Task | Hours | Shippable? |
|------|-------|------------|
| Full manual test: complete campaign | 2.0 | ✅ No crashes |
| Fix bugs from manual test | 2.0 | ✅ Bugs fixed |
| Run all unit tests | 0.5 | ✅ All pass |
| Test on low-RAM emulator | 0.5 | ✅ No OOM |
| Test offline (airplane mode) | 0.5 | ✅ Works offline |
| Add ProGuard/R8 rules | 0.5 | ✅ Release builds |
| Build release AAB | 0.5 | ✅ AAB builds |
| Run lint, fix warnings | 0.5 | ✅ Clean lint |
| Git commit + tag v1.0.0-rc1 | 0.5 | ✅ Tagged |

**Verification:**
- [ ] Full campaign (5 bosses, ~15 sessions) no crash
- [ ] All unit tests pass
- [ ] Works on low-RAM device
- [ ] Works in airplane mode
- [ ] Release AAB builds and runs
- [ ] Lint: 0 errors, <5 warnings
- [ ] App size < 15MB

---

### Day 13: Play Store Assets (7.5h)
| Task | Issue | Hours | Shippable? |
|------|-------|-------|------------|
| Create app icon | #70 | 1.5 | ✅ Icon ready |
| Create feature graphic | #71 | 1.0 | ✅ Graphic ready |
| Create 3 screenshots | #72 | 2.0 | ✅ Screenshots ready |
| Write store listing | #73 | 1.0 | ✅ Copy ready |
| Create privacy policy | #74 | 0.5 | ✅ Page live |
| Configure Play Console | #75 | 1.0 | ✅ Console set |
| Set up internal testing | #76 | 0.5 | ✅ Track ready |
| Recruit 12 testers | #77 | 0.5 | ✅ Testers ready |

**Verification:**
- [ ] Icon 512x512 + adaptive XML
- [ ] Feature graphic 1024x500
- [ ] 3 screenshots 1080x1920
- [ ] Title ≤ 30 chars, short desc ≤ 80 chars
- [ ] Privacy policy URL live
- [ ] Content rating completed
- [ ] Data safety completed
- [ ] Internal testing track created
- [ ] 12 testers identified

---

### Day 14: Closed Testing Launch (7.5h)
| Task | Issue | Hours | Shippable? |
|------|-------|-------|------------|
| Upload AAB to Play Console | #80 | 1.0 | ✅ AAB uploaded |
| Review pre-launch report | #81 | 1.0 | ✅ No critical issues |
| Add testers + send opt-in link | #82 | 1.0 | ✅ Testers onboarded |
| Post launch announcements | #83 | 1.0 | ✅ Posts live |
| Share 3 Shorts | #84 | 1.5 | ✅ Shorts live |
| Set up feedback + survey | #85 | 1.0 | ✅ Form ready |
| Send Day 1 survey (after 24h) | #86 | 0.5 | ✅ Survey sent |
| Git tag v1.0.0-mvp | — | 0.5 | ✅ Tagged |

**Verification:**
- [ ] AAB uploaded, no errors
- [ ] Pre-launch report: no critical crashes
- [ ] Testers can install via opt-in link
- [ ] Reddit posts live (3 communities)
- [ ] X + LinkedIn posts live
- [ ] 3 Shorts on TikTok/YT/IG
- [ ] Google Form survey live
- [ ] Git tagged v1.0.0-mvp

---

## Sprint 2 Exit Criteria

All must be met for MVP launch:

- [ ] Victory screen works (boss defeat → next boss)
- [ ] Stats screen works (chart, progress, streak)
- [ ] Share card generates and shares correctly
- [ ] All animations polished
- [ ] All edge cases handled (background, kill, midnight)
- [ ] Full campaign completable without crash
- [ ] Release AAB builds
- [ ] Play Store listing complete
- [ ] 12 testers onboarded
- [ ] Launch posts live
- [ ] Feedback survey live
- [ ] Git tagged v1.0.0-mvp