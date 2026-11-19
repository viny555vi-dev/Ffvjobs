package com.ffvreborn.content;
import java.util.*;
import com.ffvreborn.core.PlayerRpgData;
public final class SummonService {
    public record Summon(String id, int manaCost, int cooldownTicks, int requiredLevel, String effect) {
        public Summon {
            if (id == null || !id.matches("[a-z0-9_]+") || manaCost < 0 || cooldownTicks < 0 ||
                requiredLevel < 1 || effect == null || effect.isBlank()) {
                throw new IllegalArgumentException("Invalid summon definition");
            }
        }
    }
    private final Map<String, Summon> summons = new TreeMap<>();
    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();
    public SummonService(Collection<Summon> definitions) {
        for (Summon summon : definitions) {
            if (summons.putIfAbsent(summon.id(), summon) != null) {
                throw new IllegalArgumentException("Duplicate summon: " + summon.id());
            }
        }
    }
    public String invoke(PlayerRpgData player, String summonId, long gameTick) {
        Summon summon = summons.get(summonId);
        if (summon == null) throw new IllegalArgumentException("Unknown summon: " + summonId);
        if (!"summoner".equals(player.activeJob())) throw new IllegalStateException("Summoner job required");
        if (player.progressFor("summoner").level() < summon.requiredLevel()) throw new IllegalStateException("Summoner level too low");
        long readyAt = cooldowns.getOrDefault(player.playerId(), Map.of()).getOrDefault(summonId, 0L);
        if (gameTick < readyAt) throw new IllegalStateException("Summon cooldown active");
        if (player.mana() < summon.manaCost()) throw new IllegalStateException("Insufficient mana");
        player.setMana(player.mana() - summon.manaCost());
        cooldowns.computeIfAbsent(player.playerId(), ignored -> new HashMap<>()).put(summonId, gameTick + summon.cooldownTicks());
        return summon.effect();
    }
}
