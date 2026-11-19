package com.ffvreborn.content;

import java.util.List;
import java.util.random.RandomGenerator;

/** Shared drop probabilities for hostile creature loot. Values are deliberately server-side. */
public final class CombatLoot {
    public enum Drop { NONE, AETHER_SHARD, EMBERSTEEL_BLADE, TIDEGLASS_STAFF, WYRMFANG_SPEAR, VERDANT_LONGBOW }
    private static final List<Drop> RARE_WEAPONS = List.of(Drop.EMBERSTEEL_BLADE, Drop.TIDEGLASS_STAFF, Drop.WYRMFANG_SPEAR, Drop.VERDANT_LONGBOW);
    private CombatLoot() {}
    public static int xpForKill(float maxHealth, boolean hostile) {
        if (!hostile || maxHealth <= 0) return 0;
        return Math.max(8, Math.min(120, Math.round(maxHealth * 2.5f)));
    }
    public static Drop roll(RandomGenerator random, float luck) {
        float roll = random.nextFloat() - Math.max(0, Math.min(0.5f, luck)) * 0.08f;
        if (roll < 0.0125f) return RARE_WEAPONS.get(random.nextInt(RARE_WEAPONS.size()));
        if (roll < 0.105f) return Drop.AETHER_SHARD;
        return Drop.NONE;
    }
}
