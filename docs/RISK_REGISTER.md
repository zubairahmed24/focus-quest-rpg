# ⚠️ Risk Register

## Technical Risks

| ID | Risk | Severity | Probability | Mitigation |
|----|------|----------|-------------|------------|
| T1 | **Timer drift in background** — Android may kill app process during 25-min focus session, causing timer to reset | 🔴 HIGH | Medium | Use timestamp-based calculation (save sessionStartTime to Room). On app open: `elapsed = now - sessionStartTime`. If elapsed >= 25 min → session complete. If app killed → restore from Room. **This is the #1 technical priority.** |
| T2 | **Room migration issues** — schema changes between versions could lose user data | 🟡 MEDIUM | Low (MVP) | Use `fallbackToDestructiveMigration()` for MVP (acceptable — no real users yet). Add proper migrations in v1.1. |
| T3 | **Compose performance on low-end devices** — complex animations may jank on budget phones | 🟡 MEDIUM | Medium | Test on Android emulator with 2GB RAM. Use `derivedStateOf` for computed values. Avoid lambda allocations in hot paths. Keep animations simple. |
| T4 | **Share card bitmap capture fails** — ComposeView to Bitmap can fail on some devices | 🟡 MEDIUM | Low | Wrap in try-catch. Fallback to text-only share ("I defeated the Procrastination Demon in Focus Quest! 🔥 3-day streak"). |
| T5 | **Firebase Analytics adds app size** — SDK is ~200KB+ | 🟢 LOW | High | Acceptable for MVP. Can remove if app size becomes an issue. |
| T6 | **ProGuard/R8 strips Room/Hilt classes** — release build crashes | 🟡 MEDIUM | Medium | Add ProGuard keep rules for Room entities, DAOs, Hilt modules. Test release build on Day 12. |
| T7 | **Timer inaccuracy across timezone changes** — user travels during focus session | 🟢 LOW | Low | Use UTC timestamps internally. Convert to local only for display. |
| T8 | **Multiple rapid taps on START button** — creates duplicate timer instances | 🟡 MEDIUM | Low | Disable button immediately on tap. Use single-source-of-truth state machine. |

## Retention Risks

| ID | Risk | Severity | Probability | Mitigation |
|----|------|----------|-------------|------------|
| R1 | **RPG layer feels tacked on** — users see it as "just a timer with a health bar" and revert to existing timer | 🔴 HIGH | Medium | Make boss feel alive: taunts that change, damage animation that's satisfying, victory screen that feels rewarding. Survey: "Does defeating a boss feel satisfying?" |
| R2 | **5 bosses is too few** — players defeat all 5 in a week and have nothing left | 🔴 HIGH | High | Expected for MVP. Goal is to prove the loop. Post-MVP: AI boss generation (v1.2). MVP: "Campaign Complete! More coming soon" + email capture. |
| R3 | **25-minute sessions too long** — users abandon before completion | 🟡 MEDIUM | Medium | Track abandonment rate. If >40%, add 15-min option in v1.1. MVP: keep 25 min (standard pomodoro). |
| R4 | **No push notifications** — users forget to return | 🟡 MEDIUM | High | Known MVP limitation. Streak system + welcome-back must carry retention. Add push in v1.1 (highest priority backlog). |
| R5 | **No social/competitive element** — no reason to compare with others | 🟡 MEDIUM | Medium | MVP is single-player. Share card provides minimal social hook. Weekly leaderboard is v1.2. |
| R6 | **Novelty wears off after 3 days** — RPG framing stops feeling exciting | 🔴 HIGH | Medium | Boss variety (names, taunts, HP) must feel distinct. Leveling provides ongoing progression. Streak provides daily motivation. Test: does boss + level + streak create enough motivation to return? |
| R7 | **No audio feedback** — silence makes experience feel incomplete | 🟢 LOW | Medium | Add sound effects in v1.1. MVP: haptic feedback provides physical response. |

## Launch Risks

| ID | Risk | Severity | Probability | Mitigation |
|----|------|----------|-------------|------------|
| L1 | **Google Play 12-tester requirement** — new personal accounts require 12 testers in closed testing for 14 days | 🔴 HIGH | High | Start recruiting testers on Day 10. Have backup testers. Use organization account if available. |
| L2 | **ASO competition** — "focus timer" and "pomodoro" are competitive keywords | 🟡 MEDIUM | High | Differentiate with "RPG" and "boss battle" keywords. Target long-tail: "focus timer RPG", "pomodoro game", "productivity RPG". |
| L3 | **No audience to launch to** — founder has no existing audience for productivity apps | 🟡 MEDIUM | Medium | Post in r/productivity (3M+), r/GetStudying (500K+), r/ADHD (1.8M+ — as focus tool, not medical). Shorts: "I turned my pomodoro timer into an RPG" is a compelling hook. |
| L4 | **App rejected by Play Store** — content rating or policy violation | 🟢 LOW | Low | No UGC, no ads, no data collection beyond Firebase. Content rating: "Everyone." Data safety: "No data shared." Low risk. |
| L5 | **Testers don't provide feedback** — 12 testers install but don't respond to survey | 🟡 MEDIUM | Medium | Send survey after 3 days (not 7). Keep to 5 questions. Incentive: "Complete survey → free lifetime premium when we launch." |
| L6 | **Negative reviews from closed testing** — early bugs damage rating | 🟢 LOW | Low | Closed testing reviews don't appear on public listing. Fix bugs before open testing. |
| L7 | **App size too large** — Firebase + Compose + Room may bloat APK | 🟢 LOW | Low | Target < 15MB. Enable R8 shrinking. Remove unused dependencies. |
| L8 | **Build delays** — Gradle/Hilt/Room compilation slow | 🟢 LOW | Medium | Use Gradle configuration cache. Pre-download dependencies. Acceptable for solo dev. |

## Top 3 Risks to Watch Daily

1. **T1: Timer drift in background** — Test backgrounding every single day. If timer loses accuracy, fix immediately.
2. **R1: RPG feels tacked on** — Watch for "just a timer" in tester feedback. If >2 testers say this, redesign boss interaction.
3. **L1: 12-tester requirement** — Start recruiting Day 10. If <8 testers by Day 12, escalate recruitment.