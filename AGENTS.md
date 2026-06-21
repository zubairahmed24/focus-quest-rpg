# 🤖 AGENTS.md — AI Agent Coding Instructions

> This file provides context for AI coding assistants (Claude, GPT, Cursor, etc.) working on this codebase.

## Project Context

**Focus Quest RPG** is an Android pomodoro timer wrapped in RPG combat mechanics. The app is offline-first, single-player, and built for solo maintenance.

## Tech Stack (Non-Negotiable)

- **Language:** Kotlin only. No Java.
- **UI:** Jetpack Compose + Material 3. No XML layouts.
- **Architecture:** MVVM + Clean Architecture (presentation / domain / data).
- **DI:** Hilt. No manual DI.
- **Persistence:** Room. No SQLite directly.
- **Async:** Coroutines + Flow (StateFlow/SharedFlow for UI state).
- **Navigation:** Navigation Compose.
- **Serialization:** kotlinx.serialization (if needed).
- **Testing:** JUnit + MockK + Turbine.

## Architecture Rules

### Layer Dependencies (Enforced)
```
presentation → domain ← data
```
- `domain/` must have **ZERO** Android framework imports. Pure Kotlin.
- `data/` implements `domain/` interfaces.
- `presentation/` depends on `domain/` use cases only (never data layer directly).

### Package Convention
```
com.focusquest/
├── data/local/entity/     # Room @Entity classes
├── data/local/dao/        # Room @Dao interfaces
├── data/repository/       # Repository implementations
├── data/mapper/           # Entity ↔ Domain mappers
├── domain/model/          # Pure Kotlin data classes & sealed classes
├── domain/usecase/        # Business logic use cases
├── domain/service/        # Domain services (timer, etc.)
├── presentation/          # Compose screens + ViewModels
├── di/                    # Hilt @Module classes
└── util/                  # Constants, date utils
```

## Code Quality Rules

1. **No placeholder logic.** Every function must have real implementation.
2. **No `TODO` comments** unless explicitly requested.
3. **No `lateinit var`** in domain models. Use constructor injection or nullable.
4. **Minimize recompositions.** Use stable params, `remember`, `derivedStateOf`.
5. **Prefer composition over inheritance.**
6. **Keep functions short.** Max ~40 lines per function.
7. **Name things explicitly.** No `it` in multi-line lambdas.
8. **Always explain non-trivial architectural decisions** in comments.
9. **Optimize for mid-range devices.** Assume modest CPU/RAM.
10. **Handle edge cases.** Empty states, errors, loading states.

## Compose Rules

- Hoist state to ViewModel. Screens should be stateless where possible.
- Use `StateFlow<UiState>` pattern for all screen states.
- Seal classes for UI state, not for data models.
- Use `collectAsStateWithLifecycle()` (not `collectAsState()`).
- Stable composable parameters to prevent recomposition.
- No `LazyColumn` with unstable keys.

## Room Rules

- All DAO methods returning live data use `Flow<T>`.
- One-shot queries use `suspend fun`.
- Entity classes are `data class` with `@Entity`.
- Database version starts at 1. Always add migration scripts.
- Use `@Transaction` for multi-query operations.

## Timer Rules (Critical)

- Timer uses **timestamp-based calculation**, NOT a running counter.
- On session start: save `sessionStartTime` to Room.
- On app open: `elapsed = now - sessionStartTime`. If >= duration → complete.
- This survives app kills, backgrounding, and screen-off.
- Never rely on a running coroutine for timer accuracy.

## Testing Rules

- Unit test all use cases.
- Unit test all DAOs (in-memory Room).
- Test streak logic edge cases (consecutive day, gap, same-day, first-ever).
- Test timer state transitions (Idle → Focusing → Break → Idle).
- Use `Turbine` for Flow testing.

## Git Workflow

- `main` branch is always shippable.
- Feature branches: `feature/issue-XX-short-description`
- Commit format: `type(scope): description [closes #XX]`
- Types: `feat`, `fix`, `refactor`, `test`, `docs`, `chore`, `ui`
- Squash merge to main.

## What NOT to Do

- ❌ Do not add network/Retrofit code (app is offline-first).
- ❌ Do not add user authentication (local-only MVP).
- ❌ Do not add ads SDK (not in MVP).
- ❌ Do not add push notifications (not in MVP).
- ❌ Do not add cloud sync (not in MVP).
- ❌ Do not add new features not in the issue backlog.
- ❌ Do not change the architecture pattern.
- ❌ Do not add Java files.
- ❌ Do not use XML layouts.
