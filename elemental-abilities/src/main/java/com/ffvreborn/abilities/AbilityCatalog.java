package com.ffvreborn.abilities;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/** Original spell and technique catalogue, deliberately independent from Minecraft classes. */
public final class AbilityCatalog {
    private static final List<AbilityDefinition> ALL = List.of(
        new AbilityDefinition("fire", "black_mage", 1, 2, 12, false, 32, "fire_burst"),
        new AbilityDefinition("ice", "black_mage", 3, 3, 15, false, 48, "frost_lance"),
        new AbilityDefinition("thunder", "black_mage", 5, 4, 18, false, 60, "storm_arc"),
        new AbilityDefinition("flare", "black_mage", 18, 18, 42, false, 180, "aether_flare"),
        new AbilityDefinition("cure", "white_mage", 1, 2, 10, false, 40, "mending_light"),
        new AbilityDefinition("regen", "white_mage", 6, 5, 22, false, 100, "verdant_regrowth"),
        new AbilityDefinition("haste", "time_mage", 4, 4, 18, false, 100, "quickening"),
        new AbilityDefinition("slow", "time_mage", 5, 4, 16, false, 80, "temporal_snare"),
        new AbilityDefinition("backstab", "thief", 2, 3, 8, false, 35, "shadow_strike"),
        new AbilityDefinition("chakra", "monk", 3, 4, 0, false, 80, "inner_focus"),
        new AbilityDefinition("jump", "dragoon", 4, 5, 12, false, 100, "skyfall"),
        new AbilityDefinition("shuriken", "ninja", 2, 3, 8, false, 28, "wind_cut")
    );
    private static final Map<String, AbilityDefinition> BY_ID = ALL.stream().collect(Collectors.toUnmodifiableMap(AbilityDefinition::id, Function.identity()));
    private AbilityCatalog() {}
    public static List<AbilityDefinition> all() { return ALL; }
    public static AbilityDefinition get(String id) {
        AbilityDefinition value = BY_ID.get(id);
        if (value == null) throw new IllegalArgumentException("Unknown ability: " + id);
        return value;
    }
}
