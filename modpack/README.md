# Final Fantasy V Reborn — Modpack

Minecraft Java 26.3 · NeoForge 26.3.0.95 · Java 25

This repository builds three Gradle modules into one installable mod:
- elemental-jobs-core — job progression and RPG domain logic
- elemental-abilities — learnable abilities and slot rules
- elemental-content — Minecraft integration, entities, items, effects and client presentation

The release workflow packages the compiled mod into an installable overrides folder. The first bestiary release intentionally excludes bosses.

## Current original bestiary
- Cinder Wolf — fast melee hunter; overworld spawn pool
- Mire Hag — heavier swamp predator
- Aether Wisp — quick, fragile ethereal hunter

## Progression and loot
Hostile kills award job-specific XP (levels 1–50) and ABP on level-up. Hostile drops have a 9.25% shard chance and a 1.25% rare-weapon chance before luck adjustment. Drops are server authoritative.

## Camera and controller
The adventure camera adjusts detached third-person follow distance, smooths turn response, adds subtle motion roll, and applies speed-aware FOV. DualSenseX should expose the DualSense as an XInput controller. A dedicated in-mod gamepad mapping layer remains part of the client integration work.

## Install
Install Minecraft Java 26.3 with NeoForge 26.3.0.95 and Java 25, then copy the release JAR into the instance mods folder. Not compatible with Fabric or Forge 1.20.x.
