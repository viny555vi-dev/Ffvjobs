package com.ffvreborn.content.client;
import com.ffvreborn.content.FinalFantasyVReborn;
import com.ffvreborn.content.ModParticles;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
@EventBusSubscriber(modid = FinalFantasyVReborn.MOD_ID, value = Dist.CLIENT)
public final class ClientParticleRegistration {
    private ClientParticleRegistration() {}
    @SubscribeEvent public static void register(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.AETHER_FLARE.get(), sprites -> (type, level, x, y, z, dx, dy, dz) ->
            new AetherParticle(level, x, y, z, dx, dy, dz, sprites, 1.0f, 0.56f, 0.16f, 0.34f));
        event.registerSpriteSet(ModParticles.FROST_SHARD.get(), sprites -> (type, level, x, y, z, dx, dy, dz) ->
            new AetherParticle(level, x, y, z, dx, dy, dz, sprites, 0.36f, 0.82f, 1.0f, 0.26f));
        event.registerSpriteSet(ModParticles.RUNE_SPARK.get(), sprites -> (type, level, x, y, z, dx, dy, dz) ->
            new AetherParticle(level, x, y, z, dx, dy, dz, sprites, 0.72f, 0.48f, 1.0f, 0.22f));
    }
}
