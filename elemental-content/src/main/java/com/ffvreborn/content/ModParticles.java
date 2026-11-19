package com.ffvreborn.content;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
public final class ModParticles {
    public static final DeferredRegister<SimpleParticleType> TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, FinalFantasyVReborn.MOD_ID);
    public static final DeferredHolder<SimpleParticleType, SimpleParticleType> AETHER_FLARE = TYPES.register("aether_flare", () -> new SimpleParticleType(false));
    public static final DeferredHolder<SimpleParticleType, SimpleParticleType> FROST_SHARD = TYPES.register("frost_shard", () -> new SimpleParticleType(false));
    public static final DeferredHolder<SimpleParticleType, SimpleParticleType> RUNE_SPARK = TYPES.register("rune_spark", () -> new SimpleParticleType(false));
    private ModParticles() {}
}
