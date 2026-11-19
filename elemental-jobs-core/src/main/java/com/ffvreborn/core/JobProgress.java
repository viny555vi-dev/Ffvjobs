package com.ffvreborn.core;
public final class JobProgress {
    private int level = 1;
    private long xp;
    private int abp;
    public int level() { return level; }
    public long xp() { return xp; }
    public int abp() { return abp; }
    public int addXp(long amount) {
        if (amount < 0) throw new IllegalArgumentException("XP cannot be negative");
        xp = Math.addExact(xp, amount);
        int gained = 0;
        while (level < 50 && xp >= xpForNextLevel(level)) {
            xp -= xpForNextLevel(level);
            level++;
            gained++;
            abp = Math.addExact(abp, 2 + level / 5);
        }
        return gained;
    }
    public void addAbp(int amount) {
        if (amount < 0) throw new IllegalArgumentException("ABP cannot be negative");
        abp = Math.addExact(abp, amount);
    }
    public boolean spendAbp(int amount) {
        if (amount < 0) throw new IllegalArgumentException("ABP cannot be negative");
        if (abp < amount) return false;
        abp -= amount;
        return true;
    }
    public void restore(int level, long xp, int abp) {
        if (level < 1 || level > 50 || xp < 0 || abp < 0) throw new IllegalArgumentException("Invalid saved job progress");
        this.level = level; this.xp = xp; this.abp = abp;
    }
    public static long xpForNextLevel(int currentLevel) {
        if (currentLevel < 1 || currentLevel >= 50) return Long.MAX_VALUE;
        return 100L + (long) currentLevel * currentLevel * 35L;
    }
}
