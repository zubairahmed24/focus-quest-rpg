# ⚔️ Focus Quest RPG

> **Defeat procrastination. One focus session at a time.**

Focus Quest turns your pomodoro timer into an RPG battle. Every 25-minute focus session deals damage to a procrastination boss. Defeat bosses to level up, unlock new enemies, and build your focus streak.

## 🎮 How It Works

1. Start a focus session (25 minutes)
2. Your focus time deals damage to the boss
3. Complete the session → boss takes 100 damage
4. Earn XP, level up, and unlock the next boss
5. Maintain your daily streak 🔥

## 🏗️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt |
| Persistence | Room (offline-first) |
| Navigation | Navigation Compose |
| Analytics | Firebase Analytics |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 35 |

## 📁 Project Structure

```
com.focusquest/
├── data/           # Room entities, DAOs, repositories, mappers
├── domain/         # Domain models, use cases, timer service
├── presentation/   # Compose screens, ViewModels, theme, navigation
├── di/             # Hilt modules
└── util/           # Constants, date utilities
```

## 🚀 Quick Start

```bash
# Clone
git clone https://github.com/zubairahmed24/focus-quest-rpg.git
cd focus-quest-rpg

# Open in Android Studio
# Select SDK 35, Kotlin 2.0+
# Run on emulator or device (Android 8.0+)

# Build
./gradlew assembleDebug

# Test
./gradlew test
```

## 📋 Documentation

| Document | Description |
|----------|-------------|
| [Product Requirements](docs/PRODUCT_REQUIREMENTS.md) | Full PRD with success metrics |
| [Architecture](docs/ARCHITECTURE.md) | Technical architecture & data models |
| [Contributing](CONTRIBUTING.md) | Development workflow & conventions |
| [Agents](AGENTS.md) | AI agent coding instructions |
| [Changelog](CHANGELOG.md) | Version history |

## 🗺️ Roadmap

### MVP (v1.0) — 14 Days
- [ ] Pomodoro timer (25/5)
- [ ] 5 hand-designed bosses
- [ ] XP & leveling system
- [ ] Daily streak tracker
- [ ] Stats screen with weekly chart
- [ ] Share card generation
- [ ] Closed testing launch

### Post-MVP
- [ ] v1.1: Abilities system, subscription, push notifications, sound
- [ ] v1.2: AI boss generation, weekly leaderboard, achievements
- [ ] v1.3: Cloud sync, seasonal events, home screen widget

## 📊 Success Metrics

| Metric | MVP Target | Stretch |
|--------|-----------|--------|
| D1 Retention | >40% | >50% |
| D7 Retention | >18% | >25% |
| Session Completion Rate | >65% | >80% |
| Boss 2 Reach Rate | >35% | >50% |
| Play Store Rating | >4.0 | >4.5 |

## 📜 License

Proprietary. All rights reserved.

## 👤 Author

**CreativeX Edits** — Solo Android developer building in public.
