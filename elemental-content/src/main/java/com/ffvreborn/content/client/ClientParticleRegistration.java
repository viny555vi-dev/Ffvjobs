package com.ffvreborn.content.client;
import com.ffvreborn.content.FinalFantasyVReborn;
import com.ffvreborn.content.ModParticles;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.QuadParticleGroup;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleGroupsEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
@EventBusSubscriber(modid = FinalFantasyVReborn.MOD_ID, value = Dist.CLIENT)
public final class ClientParticleRegistration {
    private static final ParticleRenderType AETHER_GROUP = new ParticleRenderType("FFV_AETHER", "FFV-AETHER");
    private static final ParticleRenderType FROST_GROUP = new ParticleRenderType("FFV_FROST", "FFV-FROST");
    private static final ParticleRenderType RUNE_GROUP = new ParticleRenderType("FFV_RUNE", "FFV-RUNE");
    private ClientParticleRegistration() {}
    @SubscribeEvent public static void registerGroups(RegisterParticleGroupsEvent event) {
        event.register(AETHER_GROUP, engine -> new QuadParticleGroup(engine, AETHER_GROUP));
        event.register(FROST_GROUP, engine -> new QuadParticleGroup(engine, FROST_GROUP));
        event.register(RUNE_GROUP, engine -> new QuadParticleGroup(engine, RUNE_GROUP));
    }
    @SubscribeEvent public static void registerProviders(RegisterParticleProvidersEvent event) {
        event.registerSpecial(ModParticles.AETHER_FLARE.get(), (type, level, x, y, z, dx, dy, dz, random) ->
            new AetherParticle(level, x, y, z, dx, dy, dz, Blocks.MAGMA_BLOCK.defaultBlockState(), AETHER_GROUP));
        event.registerSpecial(ModParticles.FROST_SHARD.get(), (type, level, x, y, z, dx, dy, dz, random) ->
            new AetherParticle(level, x, y, z, dx, dy, dz, Blocks.BLUE_ICE.defaultBlockState(), FROST_GROUP));
        event.registerSpecial(ModParticles.RUNE_SPARK.get(), (type, level, x, y, z, dx, dy, dz) ->
            new AetherParticle(level, x, y, z, dx, dy, dz, Blocks.AMETHYST_BLOCK.defaultBlockState(), RUNE_GROUP));
    }
}
