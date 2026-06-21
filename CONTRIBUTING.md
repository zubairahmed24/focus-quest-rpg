# 🤝 CONTRIBUTING.md

## Development Workflow

### Branch Strategy
- `main` — always shippable, protected
- `feature/issue-XX-description` — one branch per issue
- `fix/issue-XX-description` — bug fixes

### Commit Convention
```
type(scope): description [closes #XX]

Types:
  feat     — new feature
  fix      — bug fix
  refactor — code restructuring (no behavior change)
  test     — adding/updating tests
  docs     — documentation only
  chore    — build, deps, config
  ui       — UI/UX changes
```

### Pull Request Process
1. Create branch from `main`
2. Implement changes per issue acceptance criteria
3. Run `./gradlew test` — all tests must pass
4. Run `./gradlew assembleDebug` — must compile
5. Self-review code against AGENTS.md rules
6. Create PR with issue reference
7. Squash merge to `main`

### Code Review Checklist
- [ ] No Android imports in `domain/` layer
- [ ] No `TODO` comments
- [ ] No `lateinit var` in domain models
- [ ] Compose: state hoisted to ViewModel
- [ ] Compose: `collectAsStateWithLifecycle()` used
- [ ] Room: Flow for live data, suspend for one-shot
- [ ] Timer: timestamp-based, not running counter
- [ ] Tests: use cases have unit tests
- [ ] No new dependencies without approval

### Testing Requirements
- All use cases: unit tested
- All DAOs: unit tested (in-memory Room)
- Streak logic: edge cases tested
- Timer state machine: transitions tested
- No PR merged without passing tests

### Issue Management
- Each issue has acceptance criteria — implement exactly those
- Move issue to "In Progress" when starting
- Move issue to "Review" when PR created
- Move issue to "Done" when PR merged
- Do not add features not in the issue

### Sprint Cadence
- Sprint 1: Days 1-7 (project skeleton → core game loop)
- Sprint 2: Days 8-14 (victory → stats → polish → launch)
- Daily: commit at least once per day
- End of sprint: tag release (`v0.1.0-sprint1`, `v1.0.0-mvp`)
