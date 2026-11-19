package com.ffvreborn.content;

import java.util.UUID;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

/** Temporary allied summon. It hunts hostile monsters and never targets its summoner. */
public final class FFVSummon extends Monster {
    private UUID summoner;
    public FFVSummon(EntityType<? extends Monster> type, Level level) { super(type, level); }
    public void bindSummoner(UUID owner) { this.summoner = owner; setPersistenceRequired(); }
    @Override protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.25, true));
        goalSelector.addGoal(7, new RandomStrollGoal(this, 0.9));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Monster.class, 10, true, false,
            candidate -> candidate != this && !(candidate instanceof FFVSummon)));
    }
    @Override public boolean isAlliedTo(Entity entity) {
        return (summoner != null && summoner.equals(entity.getUUID())) || entity instanceof FFVSummon || super.isAlliedTo(entity);
    }
    @Override public void tick() {
        super.tick();
        if (tickCount > 1800) discard();
    }
}
