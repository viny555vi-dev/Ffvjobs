package com.ffvreborn.core;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
/** Persistent, server-authoritative progression earned by defeating creatures. */
public record PlayerProgression(String activeJob, Map<String, Long> xpByJob,
        Map<String, Integer> levelsByJob, Map<String, Integer> abpByJob, int mana, List<String> learnedAbilities) {
    public PlayerProgression {
        if (activeJob == null || !activeJob.matches("[a-z0-9_]+") || mana < 0 || mana > 500) throw new IllegalArgumentException("Invalid player progression");
        xpByJob = Map.copyOf(xpByJob); levelsByJob = Map.copyOf(levelsByJob);
        abpByJob = Map.copyOf(abpByJob); learnedAbilities = List.copyOf(learnedAbilities);
    }
    public static PlayerProgression fresh() { return new PlayerProgression("freelancer", Map.of(), Map.of("freelancer", 1), Map.of(), 100, List.of()); }
    public int level(String job) { return Math.max(1, levelsByJob.getOrDefault(job, 1)); }
    public int abp(String job) { return abpByJob.getOrDefault(job, 0); }
    public long xp(String job) { return xpByJob.getOrDefault(job, 0L); }
    public boolean hasAbility(String id) { return learnedAbilities.contains(id); }
    public PlayerProgression withJob(String job) {
        var levels = new TreeMap<>(levelsByJob); levels.putIfAbsent(job, 1);
        return new PlayerProgression(job, xpByJob, levels, abpByJob, mana, learnedAbilities);
    }
    public PlayerProgression awardKillXp(int amount) {
        if (amount < 0) throw new IllegalArgumentException("XP cannot be negative");
        var xp = new TreeMap<>(xpByJob); var levels = new TreeMap<>(levelsByJob); var abp = new TreeMap<>(abpByJob);
        long total = Math.addExact(xp.getOrDefault(activeJob, 0L), amount);
        int level = levels.getOrDefault(activeJob, 1);
        while (level < 50) {
            long threshold = 100L + (long) level * level * 35L;
            if (total < threshold) break;
            total -= threshold; level++;
            abp.merge(activeJob, 2 + level / 5, Math::addExact);
        }
        xp.put(activeJob, total); levels.put(activeJob, level);
        return new PlayerProgression(activeJob, xp, levels, abp, mana, learnedAbilities);
    }
    public PlayerProgression learnAbility(String id, int requiredLevel, int abpCost) {
        if (id == null || !id.matches("[a-z0-9_]+") || requiredLevel < 1 || abpCost < 0) throw new IllegalArgumentException("Invalid ability");
        if (hasAbility(id)) throw new IllegalStateException("Ability already learned");
        if (level(activeJob) < requiredLevel) throw new IllegalStateException("Job level too low");
        var abp = new TreeMap<>(abpByJob);
        int balance = abp.getOrDefault(activeJob, 0);
        if (balance < abpCost) throw new IllegalStateException("Not enough ABP");
        abp.put(activeJob, balance - abpCost);
        var learned = new ArrayList<>(learnedAbilities); learned.add(id);
        return new PlayerProgression(activeJob, xpByJob, levelsByJob, abp, mana, learned);
    }
    public PlayerProgression spendMana(int cost) {
        if (cost < 0) throw new IllegalArgumentException("Mana cost cannot be negative");
        if (mana < cost) throw new IllegalStateException("Insufficient mana");
        return new PlayerProgression(activeJob, xpByJob, levelsByJob, abpByJob, mana - cost, learnedAbilities);
    }
    public PlayerProgression withMana(int value) {
        return new PlayerProgression(activeJob, xpByJob, levelsByJob, abpByJob, Math.max(0, Math.min(500, value)), learnedAbilities);
    }
}
