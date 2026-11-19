# Final Fantasy V: Reborn

Original RPG overhaul for Minecraft Java 26.3 / NeoForge 26.3.0.95 / Java 25.

The build is split into three Gradle modules and packaged as one installable NeoForge mod:
- elemental-jobs-core — persistent player jobs, levels, XP, ABP and mana
- elemental-abilities — job-gated techniques and ability catalogue
- elemental-content — Minecraft entities, items, spell effects, summons, camera, controller and loot integration

## Included gameplay
- 18 selectable jobs; the Crystal of Dawn cycles the active job without commands.
- Independent job progression, levels 1–50, combat XP and ABP rewards.
- 12 learnable techniques across Black Mage, White Mage, Time Mage, Thief, Monk, Dragoon and Ninja.
- Three original hostile creatures: Cinder Wolf, Mire Hag and Aether Wisp.
- Three temporary allied summons: Pyrewing, Frostmane and Terragorn.
- Four rare weapons and Aether Shards, dropped by hostiles and added to simple dungeon chests.
- Three custom elemental particle groups and distinct procedural creature rigs and animations.
- Over-the-shoulder third-person adventure camera mixin, dynamic follow distance/FOV/roll, and SDL3 controller input for DualSense / DualSenseX XInput mode.

Bosses are intentionally excluded from this release.

## Build and test
GitHub Actions installs Java 25 and Gradle 9.2.1, compiles all modules, launches the real Minecraft 26.3 NeoForge GameTestServer, runs the in-game test suite and packages the installable modpack.

See modpack/README.md for installation and controller mapping.
