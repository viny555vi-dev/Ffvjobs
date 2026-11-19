package com.ffvreborn.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import java.util.TreeMap;

/** Persistent, server-authoritative progression earned by defeating creatures. */
public record PlayerProgression(String activeJob, Map<String, Long> xpByJob,
        Map<String, Integer> levelsByJob, Map<String, Integer> abpByJob, int mana) {
    public static final com.mojang.serialization.MapCodec<PlayerProgression> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.STRING.optionalFieldOf("active_job", "freelancer").forGetter(PlayerProgression::activeJob),
        Codec.unboundedMap(Codec.STRING, Codec.LONG).optionalFieldOf("xp", Map.of()).forGetter(PlayerProgression::xpByJob),
        Codec.unboundedMap(Codec.STRING, Codec.INT).optionalFieldOf("levels", Map.of("freelancer", 1)).forGetter(PlayerProgression::levelsByJob),
        Codec.unboundedMap(Codec.STRING, Codec.INT).optionalFieldOf("abp", Map.of()).forGetter(PlayerProgression::abpByJob),
        Codec.INT.optionalFieldOf("mana", 100).forGetter(PlayerProgression::mana)
    ).apply(instance, PlayerProgression::new));

    public PlayerProgression {
        if (activeJob == null || !activeJob.matches("[a-z0-9_]+") || mana < 0 || mana > 500) throw new IllegalArgumentException("Invalid player progression");
        xpByJob = Map.copyOf(xpByJob);
        levelsByJob = Map.copyOf(levelsByJob);
        abpByJob = Map.copyOf(abpByJob);
    }
    public static PlayerProgression fresh() { return new PlayerProgression("freelancer", Map.of(), Map.of("freelancer", 1), Map.of(), 100); }
    public int level(String job) { return Math.max(1, levelsByJob.getOrDefault(job, 1)); }
    public int abp(String job) { return abpByJob.getOrDefault(job, 0); }
    public long xp(String job) { return xpByJob.getOrDefault(job, 0L); }
    public PlayerProgression withJob(String job) {
        var levels = new TreeMap<>(levelsByJob); levels.putIfAbsent(job, 1);
        return new PlayerProgression(job, xpByJob, levels, abpByJob, mana);
    }
    public PlayerProgression awardKillXp(int amount) {
        if (amount < 0) throw new IllegalArgumentException("XP cannot be negative");
        var xp = new TreeMap<>(xpByJob);
        var levels = new TreeMap<>(levelsByJob);
        var abp = new TreeMap<>(abpByJob);
        long total = Math.addExact(xp.getOrDefault(activeJob, 0L), amount);
        int level = levels.getOrDefault(activeJob, 1);
        while (level < 50) {
            long threshold = 100L + (long) level * level * 35L;
            if (total < threshold) break;
            total -= threshold;
            level++;
            abp.merge(activeJob, 2 + level / 5, Math::addExact);
        }
        xp.put(activeJob, total);
        levels.put(activeJob, level);
        return new PlayerProgression(activeJob, xp, levels, abp, mana);
    }
    public PlayerProgression withMana(int value) {
        return new PlayerProgression(activeJob, xpByJob, levelsByJob, abpByJob, Math.max(0, Math.min(500, value)));
    }
}
