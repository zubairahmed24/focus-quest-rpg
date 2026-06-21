# 📋 PRODUCT_REQUIREMENTS.md

## Product Overview

**Focus Quest RPG** is a pomodoro timer wrapped in RPG combat mechanics. Each 25-minute focus session deals damage to a procrastination boss. Players defeat bosses, earn XP, level up, and maintain daily streaks.

## Core Loop

```
START FOCUS SESSION (25 min)
    → TIMER RUNNING (player focuses on real work)
    → COMPLETE → +100 DMG to boss, +100 XP
    → BOSS DEFEATED? → YES: Victory → Unlock next boss → Break (5 min)
                     → NO: Break (5 min) → Next session
```

## Player Motivation

| Layer | Motivation |
|-------|-----------|
| Surface | "I want to defeat the next boss" |
| Habit | "I don't want to lose my streak" |
| Identity | "I'm someone who focuses" |
| Progress | "I'm level 5 now, I've focused 10 hours total" |

## Day 1 Experience
1. App opens → No login, no tutorial
2. Battle Screen: Boss 1 "Slime of Laziness" (100 HP)
3. Player taps "START FOCUSING" → 25-min timer begins
4. Timer completes → Boss takes 100 damage → DEFEATED
5. Victory screen: XP gained, level up, next boss revealed
6. 5-min break timer starts → Player can start next battle or close app

## Day 7 Experience
1. "Welcome back! 3-day streak 🔥"
2. Boss 3 at 200/300 HP (2 sessions done)
3. Player focuses → defeats boss → level up
4. Stats screen: "This week you focused 4h 10m across 10 sessions"
5. Player shares progress card → closes app

## MVP Feature Freeze

### Must Have (P0)
| ID | Feature |
|----|---------|
| F1 | Pomodoro timer (25 min focus + 5 min break) |
| F2 | Battle screen with boss HP bar + player HP bar (cosmetic) |
| F3 | Focus session completion = 100 damage to boss |
| F4 | 5 hand-designed bosses with names, HP, taunt text |
| F5 | Sequential boss progression (defeat → unlock next) |
| F6 | XP system (+100 per session, level up every 300 XP) |
| F7 | Daily streak counter (reset on missed day) |
| F8 | Victory screen (boss defeated → XP → next boss revealed) |
| F9 | Stats screen (level, total focus time, bosses, streak, weekly chart) |
| F10 | Break timer (auto-starts after focus, skippable) |
| F11 | Local persistence (Room — survives app restart) |
| F12 | App icon + Play Store listing assets |

### Should Have (P1)
| ID | Feature |
|----|---------|
| F13 | Share card generation (static image, Android share sheet) |
| F14 | Boss taunt/flavor text on battle screen |
| F15 | Firebase Analytics (session events) |

### Could Have (P2 — only if time permits)
| ID | Feature |
|----|---------|
| F16 | Haptic feedback on boss damage + level up |
| F17 | Boss shake animation on damage |
| F18 | Level-up burst animation |

### Not For MVP
| ID | Feature | Phase |
|----|---------|-------|
| B1 | Abilities/skills system | v1.1 |
| B2 | AI boss generation pipeline | v1.2 |
| B3 | Subscription paywall | v1.1 |
| B4 | Rewarded ads | v1.1 |
| B5 | Push notifications | v1.1 |
| B6 | Weekly leaderboard | v1.2 |
| B7 | Sound effects + music | v1.1 |
| B8 | Character customization | v1.2 |
| B9 | Story/dialogue system | v1.2 |
| B10 | Equipment system | v1.3 |
| B11 | Multiple timer presets | v1.1 |
| B12 | Cloud sync | v1.3 |
| B13 | Seasonal events | v1.3 |
| B14 | Achievements/badges | v1.2 |
| B15 | Home screen widget | v1.3 |

## Boss Data

| # | Name | HP | Sessions Required | Taunt |
|---|------|----|-------------------|-------|
| 1 | Slime of Laziness | 100 | 1 | "Why do today what you can put off...?" |
| 2 | Distraction Goblin | 200 | 2 | "Just one more video, it'll only take a minute..." |
| 3 | Procrastination Demon | 300 | 3 | "I'll start fresh tomorrow, I promise..." |
| 4 | Distraction Wraith | 400 | 4 | "Your phone is buzzing. Better check it now..." |
| 5 | Burnout Dragon | 500 | 5 | "You've done enough. You deserve a break. A permanent one..." |

## Success Metrics

### MVP Targets (100 users)
| Metric | Green | Yellow | Red (Kill) |
|--------|-------|--------|------------|
| D1 retention | >40% | 30-40% | <30% |
| D7 retention | >18% | 12-18% | <12% |
| Session completion rate | >65% | 50-65% | <50% |
| Boss 2 reach rate | >35% | 25-35% | <25% |
| Share card clicks | >8% | 3-8% | <3% |
| "I focused more than usual" | >35% | 20-35% | <20% |

### Scale Targets (1,000 users)
| Metric | Green | Yellow | Red |
|--------|-------|--------|-----|
| D7 retention | >22% | 14-22% | <14% |
| D30 retention | >10% | 5-10% | <5% |
| Avg sessions/user/day | >2 | 1.3-2 | <1.3 |
| "I'd pay for this" signals | >5 | 1-5 | 0 |
| Play Store rating | >4.2 | 3.5-4.2 | <3.5 |

## Kill Criteria
Kill project if ANY are true after 100 users:
1. D1 retention < 30%
2. Session completion rate < 50%
3. Boss 2 reach rate < 25%
4. < 20% say "I focused more than usual"
5. Reviews say "just a timer with a health bar"

## Monetization (Post-MVP)
| Stream | Price | Phase |
|--------|-------|-------|
| Subscription | $3.99/mo, $24.99/yr | v1.1 |
| Lifetime | $29.99 (promotional) | v1.1 |
| Rewarded ads | 2x damage boost | v1.1 |
| IAP | Boss packs, cosmetics | v1.2 |