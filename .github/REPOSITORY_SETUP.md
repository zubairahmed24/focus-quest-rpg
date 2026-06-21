# GitHub Repository Setup Plan

## Repository Name
```
focus-quest-rpg
```

## Repository Description
```
⚔️ Focus Quest RPG — Defeat procrastination bosses with pomodoro focus sessions. Android, Kotlin, Jetpack Compose, Material 3.
```

## Folder Structure
```
focus-quest-rpg/
├── .github/
│   ├── ISSUE_TEMPLATE/
│   │   ├── feature.md
│   │   ├── bug.md
│   │   └── task.md
│   └── labels.yml
├── docs/
│   ├── PRODUCT_REQUIREMENTS.md
│   └── ARCHITECTURE.md
├── app/
│   └── src/
│       ├── main/java/com/focusquest/...
│       ├── main/res/...
│       ├── test/java/com/focusquest/...
│       └── androidTest/java/com/focusquest/...
├── README.md
├── AGENTS.md
├── CONTRIBUTING.md
├── CHANGELOG.md
├── .gitignore
└── settings.gradle.kts
```

## Labels

### Type Labels
| Label | Color | Description |
|-------|-------|-------------|
| `type:setup` | #c5def5 | Project configuration |
| `type:data` | #bfdadc | Data layer work |
| `type:timer` | #d4c5f9 | Timer engine |
| `type:ui` | #b7b7f7 | UI/Compose work |
| `type:logic` | #bfd4f2 | Business logic |
| `type:animation` | #e99695 | Animation work |
| `type:component` | #c2e0c6 | Reusable composable |
| `type:viewmodel` | #f9d0c4 | ViewModel work |
| `type:navigation` | #d1bcf9 | Navigation work |
| `type:di` | #fef2c0 | Dependency injection |
| `type:analytics` | #f9d0c4 | Firebase analytics |
| `type:performance` | #fad8c7 | Optimization |
| `type:assets` | #fef2c0 | Store assets |
| `type:launch` | #e99695 | Launch activities |
| `type:testing` | #c5def5 | Testing work |

### Priority Labels
| Label | Color | Description |
|-------|-------|-------------|
| `priority:p0` | #b60205 | Must have — blocks MVP |
| `priority:p1` | #d93f0b | Should have — MVP if time |
| `priority:p2` | #fbca04 | Could have — nice to have |

### Sprint Labels
| Label | Color | Description |
|-------|-------|-------------|
| `sprint:1` | #0e8a16 | Days 1-7 |
| `sprint:2` | #1d76db | Days 8-14 |

### Epic Labels
| Label | Color | Description |
|-------|-------|-------------|
| `epic:project-setup` | #c5def5 | E1 |
| `epic:data-persistence` | #bfdadc | E2 |
| `epic:focus-timer` | #d4c5f9 | E3 |
| `epic:battle-system` | #b7b7f7 | E4 |
| `epic:progression` | #bfd4f2 | E5 |
| `epic:victory-flow` | #f9d0c4 | E6 |
| `epic:stats-display` | #c2e0c6 | E7 |
| `epic:social-sharing` | #fef2c0 | E8 |
| `epic:polish` | #e99695 | E9 |
| `epic:launch` | #d93f0b | E10 |

### Status Labels
| Label | Color | Description |
|-------|-------|-------------|
| `status:blocked` | #b60205 | Waiting on dependency |
| `status:bug` | #e11d48 | Something is broken |

## Milestones

| Milestone | Due Date | Description |
|-----------|----------|-------------|
| M1: Project Skeleton | Day 1 | Compiling project with Hilt, Room, Navigation, theme |
| M2: Data Layer | Day 3 | Room database, entities, DAOs, repositories, use cases |
| M3: Core Game Loop | Day 7 | Timer, battle screen, boss system, XP/leveling, streaks |
| M4: Full MVP | Day 11 | Victory screen, stats screen, share card, polish |
| M5: Launch Ready | Day 14 | Testing, Play Store assets, closed testing launch |

## Project Board Columns

```
┌──────────┬───────────┬───────────┬──────────┬──────────┐
│ Backlog  │ Sprint    │ In        │ Review   │ Done     │
│          │ Planning  │ Progress  │          │          │
├──────────┼───────────┼───────────┼──────────┼──────────┤
│ All 86   │ Issues    │ Currently │ PR       │ Merged   │
│ issues   │ selected  │ being     │ created  │ &        │
│ not yet  │ for       │ worked    │ awaiting │ verified │
│ sprinted │ current   │ on        │ review   │          │
│          │ sprint    │           │          │          │
└──────────┴───────────┴───────────┴──────────┴──────────┘
```

### Column Rules
- **Backlog**: All issues not yet assigned to a sprint
- **Sprint Planning**: Issues selected for current sprint (sprint:1 or sprint:2)
- **In Progress**: Issue is being actively worked on (one at a time)
- **Review**: PR created, awaiting self-review + tests passing
- **Done**: PR merged to main, acceptance criteria verified