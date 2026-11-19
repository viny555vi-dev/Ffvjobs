package com.ffvreborn.content;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {
    public static final DeferredRegister.Entities ENTITIES = DeferredRegister.createEntities(FinalFantasyVReborn.MOD_ID);
    public static final DeferredHolder<EntityType<?>, EntityType<FFVMonster>> CINDER_WOLF =
        ENTITIES.registerEntityType("cinder_wolf", FFVMonster::new, MobCategory.MONSTER, b -> b.sized(0.85f, 1.15f).clientTrackingRange(8).updateInterval(3));
    public static final DeferredHolder<EntityType<?>, EntityType<FFVMonster>> MIRE_HAG =
        ENTITIES.registerEntityType("mire_hag", FFVMonster::new, MobCategory.MONSTER, b -> b.sized(0.8f, 1.9f).clientTrackingRange(8).updateInterval(3));
    public static final DeferredHolder<EntityType<?>, EntityType<FFVMonster>> AETHER_WISP =
        ENTITIES.registerEntityType("aether_wisp", FFVMonster::new, MobCategory.MONSTER, b -> b.sized(0.75f, 1.2f).clientTrackingRange(10).updateInterval(2));
    private ModEntities() {}
    public static void register(IEventBus bus) {
        ENTITIES.register(bus);
        bus.addListener(ModEntities::attributes);
    }
    private static void attributes(EntityAttributeCreationEvent event) {
        event.put(CINDER_WOLF.get(), attributes(24, 4.5, 0.31).build());
        event.put(MIRE_HAG.get(), attributes(34, 6.0, 0.22).build());
        event.put(AETHER_WISP.get(), attributes(18, 3.0, 0.38).build());
    }
    private static AttributeSupplier.Builder attributes(double health, double damage, double speed) {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, health)
            .add(Attributes.ATTACK_DAMAGE, damage).add(Attributes.MOVEMENT_SPEED, speed)
            .add(Attributes.FOLLOW_RANGE, 28.0);
    }
}
