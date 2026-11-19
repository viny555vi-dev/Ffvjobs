package com.ffvreborn.core;
import java.util.*;
public final class PlayerRpgData {
    private final UUID playerId;
    private final Map<String, JobProgress> progress = new TreeMap<>();
    private final Set<String> learnedAbilities = new TreeSet<>();
    private String activeJob = "freelancer";
    private int mana = 100, maxMana = 100;
    public PlayerRpgData(UUID playerId) { this.playerId = Objects.requireNonNull(playerId); progress.put("freelancer", new JobProgress()); }
    public UUID playerId() { return playerId; }
    public String activeJob() { return activeJob; }
    public int mana() { return mana; }
    public int maxMana() { return maxMana; }
    public Set<String> learnedAbilities() { return Collections.unmodifiableSet(learnedAbilities); }
    public Map<String, JobProgress> progress() { return Collections.unmodifiableMap(progress); }
    public JobProgress progressFor(String job) { return progress.computeIfAbsent(job, k -> new JobProgress()); }
    public void setActiveJob(String job) { activeJob = Objects.requireNonNull(job); progressFor(job); }
    public boolean learn(String ability) { return learnedAbilities.add(ability); }
    public void setMana(int value) { mana = Math.max(0, Math.min(maxMana, value)); }
    public void setMaxMana(int value) { if (value < 1) throw new IllegalArgumentException(); maxMana = value; mana = Math.min(mana, maxMana); }
}
