# Focus Quest RPG — MVP Scope Freeze

## Must Have (P0) — Blocks MVP Launch

| ID | Feature | Issue(s) | Sprint |
|----|---------|----------|--------|
| F1 | Pomodoro timer (25 min focus + 5 min break) | #20-#26 | 1 |
| F2 | Battle screen with boss HP bar + player HP bar (cosmetic) | #27-#33 | 1 |
| F3 | Focus session completion = 100 damage to boss | #35 | 1 |
| F4 | 5 hand-designed bosses with names, HP, taunt text | #10, #35 | 1 |
| F5 | Sequential boss progression (defeat → unlock next) | #35 | 1 |
| F6 | XP system (+100 per session, level up every 300 XP) | #36-#37 | 1 |
| F7 | Daily streak counter (reset on missed day) | #39-#41 | 1 |
| F8 | Victory screen (boss defeated → XP → next boss revealed) | #42-#47 | 2 |
| F9 | Stats screen (level, focus time, bosses, streak, weekly chart) | #49-#53 | 2 |
| F10 | Break timer (auto-starts after focus, skippable) | #20-#21 | 1 |
| F11 | Local persistence (Room — survives app restart) | #8-#18 | 1 |
| F12 | App icon + Play Store listing assets | #70-#75 | 2 |

## Should Have (P1) — MVP If Time Permits

| ID | Feature | Issue(s) | Sprint |
|----|---------|----------|--------|
| F13 | Share card generation (static image, Android share sheet) | #55-#60 | 2 |
| F14 | Boss taunt/flavor text on battle screen | #31 | 1 |
| F15 | Firebase Analytics (session events) | #7, #60 | 1 |
| F16 | HP bar drain animation | #62 | 2 |
| F17 | GIVE UP confirmation dialog | #69 | 2 |
| F18 | Midnight edge case handling | #67 | 2 |
| F19 | Recomposition optimization | #68 | 2 |
| F20 | Color scheme polish | #65 | 2 |

## Could Have (P2) — Nice to Have

| ID | Feature | Issue(s) | Sprint |
|----|---------|----------|--------|
| F21 | Boss shake animation on damage | #34 | 2 |
| F22 | Floating "+100 DMG" text animation | #61 | 2 |
| F23 | Timer ring pulse | #63 | 2 |
| F24 | Level-up burst animation | #38, #64 | 2 |
| F25 | Haptic feedback | #66 | 2 |
| F26 | Victory animation (boss fade) | #46 | 2 |
| F27 | Landing page | #78 | 2 |
| F28 | Launch posts drafted | #79 | 2 |

## Not For MVP — Post-MVP Backlog

| ID | Feature | Phase | Rationale |
|----|---------|-------|-----------|
| B1 | Abilities/skills system | v1.1 | Adds complexity, needs balance testing |
| B2 | AI boss generation pipeline | v1.2 | Scaling feature, not validation feature |
| B3 | Subscription paywall ($3.99/mo) | v1.1 | Prove engagement before monetizing |
| B4 | Rewarded ads (2x damage boost) | v1.1 | No ad SDK in MVP |
| B5 | Push notifications | v1.1 | Retention feature, not validation feature |
| B6 | Weekly leaderboard (async) | v1.2 | Needs backend |
| B7 | Sound effects + music | v1.1 | Nice to have, not validation-critical |
| B8 | Character customization | v1.2 | Cosmetic, not core loop |
| B9 | Story/dialogue system | v1.2 | Depth feature |
| B10 | Equipment system | v1.3 | Progression expansion |
| B11 | Multiple timer presets (15/25/45/90) | v1.1 | Based on abandonment data |
| B12 | Cloud sync (Firebase) | v1.3 | MVP is local-only |
| B13 | Seasonal events | v1.3 | Live-ops feature |
| B14 | Achievements/badges | v1.2 | Meta-progression |
| B15 | Home screen widget | v1.3 | Android-specific feature |