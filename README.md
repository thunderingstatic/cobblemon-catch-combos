# Cobblemon Catch Combos

Server-side NeoForge addon for Minecraft 1.21.1 and Cobblemon 1.7.3.

## v0.3.0

- Catch combo tracking with persistent statistics
- `/catchcombo`, `/catchcombo info`, `/catchcombo stats`, and `/catchcombo top`
- Admin commands: `/catchcombo set`, `/catchcombo add`, `/catchcombo inspect`, and `/catchcombo reset <player>`
- Config reload with `/catchcombo reload`
- Readable species names
- Milestone-colored action bar
- Milestone notifications at 11, 32, and 52 catches
- Configurable command rewards
- Configurable shiny rolls and guaranteed perfect IVs
- Same-species spawn attraction through Cobblemon's spawning influence system

Default progression:

- 0-10: standard odds, 0 guaranteed perfect IVs, no spawn bonus
- 11-31: 2 total shiny rolls, 2 guaranteed perfect IVs, +25% species spawn weight
- 32-51: 4 total shiny rolls, 3 guaranteed perfect IVs, +50% species spawn weight
- 52+: 6 total shiny rolls, 4 guaranteed perfect IVs, +85% species spawn weight

Spawn attraction multiplies the eligible spawn weight of the active combo species. It does not make the species spawn in biomes, times, dimensions, or conditions where it is normally ineligible.

Configuration is generated at `config/catchcombo.json`. Existing v0.2.2 configurations gain a new `spawning` section on the next saved/generated configuration.

## Building

Place `Cobblemon-neoforge-1.7.3+1.21.1.jar` in `libs/`, then run:

```powershell
.\gradlew clean build --no-build-cache
```
