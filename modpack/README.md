# Final Fantasy V Reborn — Modpack

**Target:** Minecraft Java 26.3 · NeoForge 26.3.0.95 · Java 25

The pack contains one installable mod JAR built from three communicating Gradle modules:
- elemental-jobs-core — persistent job progression, XP, ABP and mana
- elemental-abilities — job-gated abilities and techniques
- elemental-content — Minecraft registration, creatures, spells, summons, loot, camera and controller integration

## Gameplay included

**Jobs and progression**
- 18 selectable jobs, switched in-world using the Crystal of Dawn
- Independent job XP and levels 1–50
- Hostile mob kills grant job XP; level-ups grant ABP
- Ability sigils learn techniques by spending the active job's ABP and cast them using mana

**Original bestiary — bosses excluded**
- Cinder Wolf — fast melee hunter, naturally spawns in the Overworld
- Mire Hag — heavy predator, naturally spawns in the Overworld
- Aether Wisp — quick ethereal hunter, naturally spawns in the Overworld

**Summons**
- Pyrewing — fire-aligned allied hunter
- Frostmane — frost-aligned allied hunter
- Terragorn — earth-aligned allied hunter
- Summons fight the hostile bestiary and expire after 90 seconds

**Abilities and gear**
- 12 job-gated techniques: elemental attacks, healing, regeneration, haste, temporal snare, shadow strike, chakra, skyfall and wind cut
- Fire, frost and rune custom particle groups
- Embersteel Blade, Tideglass Staff, Wyrmfang Spear and Verdant Longbow
- Aether Shards and rare weapons can drop from hostile mobs; relics are also added to simple dungeon chests

**Presentation and controls**
- Original procedural creature model animation
- Third-person adventure camera: 4.35-block detached follow distance, speed-aware FOV, subtle movement roll and tuned turn response
- SDL3 gamepad bridge supports a directly connected DualSense and DualSenseX configured as an XInput controller

### DualSense / DualSenseX mapping
Connect the controller before launching the game. In DualSenseX, enable Xbox 360 / XInput emulation if the physical DualSense is not detected as a gamepad.

| Control | Action |
|---|---|
| Left stick | Move |
| Right stick | Camera look |
| Cross | Jump |
| Circle | Sprint |
| Square / R2 | Attack |
| Triangle / L2 | Use selected sigil or item |
| Touchpad | Toggle first/third person |

The camera also remains accessible with Minecraft's normal perspective toggle (F5).

## Install
1. Install Minecraft Java 26.3.
2. Install NeoForge 26.3.0.95 and use Java 25.
3. Extract the workflow artifact named Final-Fantasy-V-Reborn-modpack.
4. Copy the JAR from overrides/mods into the instance mods folder.

The pack is not compatible with Fabric or older Forge releases.

## Verification
The automated hosted VM compiles all three modules, launches the actual NeoForge GameTestServer and runs 9 mod GameTests plus Minecraft's default test. The workflow artifact includes the modpack ZIP and server logs.
