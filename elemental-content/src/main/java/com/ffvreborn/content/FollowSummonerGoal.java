package com.ffvreborn.content;

import java.util.EnumSet;
import java.util.UUID;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

/** Keeps an allied summon near its summoner when it is not actively fighting. */
public final class FollowSummonerGoal extends Goal {
    private final FFVSummon summon;
    private final double speed;
    private Player owner;
    private int recalc;
    public FollowSummonerGoal(FFVSummon summon, double speed) {
        this.summon = summon;
        this.speed = speed;
        setFlags(EnumSet.of(Flag.MOVE));
    }
    @Override public boolean canUse() {
        UUID ownerId = summon.summonerId();
        if (ownerId == null || summon.getTarget() != null) return false;
        owner = summon.level().getPlayerByUUID(ownerId);
        return owner != null && owner.isAlive() && summon.distanceToSqr(owner) > 16.0;
    }
    @Override public boolean canContinueToUse() {
        return owner != null && owner.isAlive() && summon.getTarget() == null && summon.distanceToSqr(owner) > 9.0;
    }
    @Override public void start() { recalc = 0; }
    @Override public void tick() {
        if (owner != null && --recalc <= 0) {
            recalc = 10;
            summon.getNavigation().moveTo(owner, speed);
        }
    }
    @Override public void stop() { owner = null; summon.getNavigation().stop(); }
}
