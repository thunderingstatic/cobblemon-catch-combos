# Cobblemon Catch Combos — Clean Foundation

Target:

- Minecraft 1.21.1
- NeoForge 21.1.235
- Cobblemon 1.7.3
- Java 21
- Server-side only

This is the clean, modular replacement for the earlier single-class prototype.

## Included in this foundation

- Per-player same-species catch combos
- A different captured species begins a new combo at 1
- Persistent player data using the same `CatchCombo` NBT root as the earlier build
- Action-bar catch feedback
- Guaranteed perfect-IV milestones
- Matching-species shiny-roll bonus
- `/catchcombo`
- `/catchcombo reset`
- A small public `CatchComboAPI`

## Source layout

- `combo/` — data, persistence, and combo logic
- `events/` — Cobblemon and NeoForge event adapters
- `ivs/` — IV milestone logic
- `shiny/` — shiny roll logic
- `hud/` — user-facing display
- `commands/` — Brigadier commands
- `api/` — integration surface for other mods

## Build

1. Put `Cobblemon-neoforge-1.7.3+1.21.1.jar` in `libs/`.
2. Copy `gradlew`, `gradlew.bat`, and the `gradle/` folder from your working project into this folder.
3. Run:

```powershell
.\build.ps1
```

The script automatically uses your known Temurin Java 21 installation when available.

Output:

```text
build/libs/cobblemon-catch-combos-1.0.0-clean.jar
```

## Next controlled additions

1. Persistent statistics and disk-backed leaderboards
2. JSON configuration and reward commands
3. Sounds, particles, and richer HUD options
4. Spawn weighting and rare-spawn attraction
5. Optional Hidden Ability and size extensions
