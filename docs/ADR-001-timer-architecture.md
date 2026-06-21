# ADR-001: Timestamp-Based Timer Architecture

## Status
Accepted — 2026-06-21

## Context

Focus Quest RPG requires a focus timer that:
- **Survives app backgrounding** — user may switch apps during a 25-minute session
- **Survives screen rotation** — configuration changes must not reset the timer
- **Survives process death** — Android may kill the app process at any time
- **Has no countdown drift** — 25 minutes must be exactly 25 minutes, not 24:58 or 25:02
- **Has no memory leaks** — timer coroutines must be properly cancelled

### Approaches Considered

| Approach | Survives Death? | Drift? | Memory Leaks? | Complexity |
|----------|----------------|--------|---------------|------------|
| Handler.postDelayed | ❌ | ✅ None | ❌ Leaks if not cancelled | Low |
| CountDownTimer | ❌ | ⚠️ Slight | ❌ Leaks if not cancelled | Low |
| Coroutine delay loop (counter) | ❌ | ⚠️ Accumulates | ✅ Cancellable | Medium |
| Foreground Service + Handler | ✅ | ✅ None | ✅ | High |
| **Timestamp-based (chosen)** | **✅** | **✅ None** | **✅** | **Medium** |

### Why Not Foreground Service?
A foreground service with a persistent notification would survive process death,
but it requires:
- POST_NOTIFICATIONS permission (Android 13+)
- User-visible notification for 25 minutes (annoying for a focus app)
- Service lifecycle management complexity
- Battery drain concerns

For a focus timer, the user intentionally puts the phone away. A foreground
notification defeats the purpose. Timestamp-based recovery is superior:
the timer "runs" implicitly via real-world time passing.

## Decision

Use **timestamp-based calculation** as the source of truth for timer state.

### How It Works

1. **Session Start**: Save `sessionStartTime = LocalDateTime.now()` to Room
   (`player_state.activeSessionStartTime`)

2. **UI Ticker**: A coroutine ticks every second for display purposes only.
   On each tick, it recalculates: `remaining = FOCUS_DURATION - (now - sessionStartTime)`
   This ensures zero drift because the calculation is always from real time.

3. **App Backgrounded**: The ticker coroutine continues (Main dispatcher).
   If the process is killed, the ticker dies but the timestamp persists in Room.

4. **App Foregrounded / Process Recovery**: Read `sessionStartTime` from Room.
   - If `elapsed >= FOCUS_DURATION` → session completed while away → trigger completion
   - If `elapsed < FOCUS_DURATION` → resume with `remaining = FOCUS_DURATION - elapsed`

5. **Session Complete**: `CompleteFocusSessionUseCase` clears `activeSessionStartTime`
   to null, marking the session as finished.

### Pause / Resume

- **Pause**: Cancel ticker, save `remainingSeconds` in `FocusTimerState.Paused`
  (in-memory only, NOT persisted)
- **Resume**: Adjust `sessionStartTime` to `now - (totalDuration - remainingSeconds)`
  This makes the timestamp reflect only the active (non-paused) time.
- **If app dies during pause**: The persisted timestamp shows the original start time.
  On recovery, `elapsed` will be larger than expected, so the session will either
  auto-complete (if elapsed >= duration) or resume with less remaining time.
  This is acceptable for MVP — the user loses their pause but the session is handled.

### Break Timer

- Break timers are **in-memory only** (not persisted to Room)
- If the app dies during a break, the break is lost
- This is acceptable because breaks have no game mechanics (no damage, no XP)
- The user simply starts a new focus session when they return

## Consequences

### Schema
- `PlayerStateEntity` includes `activeSessionStartTime: String?` (ISO datetime)
- `PlayerStateDao` includes `updateSessionStartTime(time: String?)` query
- Database version bumped to 2 (with `fallbackToDestructiveMigration` for MVP)

### Architecture
- `FocusTimerService` is `@Singleton` (survives configuration changes)
- Service exposes `StateFlow<FocusTimerState>` for UI observation
- Service exposes `SharedFlow<TimerEvent>` for completion events
- `CompleteFocusSessionUseCase` clears `activeSessionStartTime` after completion
- Timer recovery runs on ViewModel init via `FocusTimerService.recoverSession()`

### Testing
- Timer accuracy tested via timestamp calculation, not real-time waiting
- Recovery tested by simulating elapsed time > duration
- Pause/resume tested by verifying state transitions

### Future Considerations (v1.1+)
- Persist pause state to Room for full pause recovery
- Add foreground service option for users who want persistent notifications
- Add widget support (timer state is in Room, accessible from widget)
