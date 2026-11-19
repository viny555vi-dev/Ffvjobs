package com.ffvreborn.content.client;

import com.ffvreborn.content.FinalFantasyVReborn;
import com.ffvreborn.content.ModEntities;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = FinalFantasyVReborn.MOD_ID, value = Dist.CLIENT)
public final class ClientEntityRegistration {
    public static final ModelLayerLocation MONSTER_LAYER = new ModelLayerLocation(
        Identifier.fromNamespaceAndPath(FinalFantasyVReborn.MOD_ID, "bestiary_creature"), "main");
    private ClientEntityRegistration() {}
    @SubscribeEvent public static void layers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MONSTER_LAYER, FFVMonsterModel::createLayer);
    }
    @SubscribeEvent public static void renderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CINDER_WOLF.get(), c -> new FFVMonsterRenderer(c, texture("cinder_wolf")));
        event.registerEntityRenderer(ModEntities.MIRE_HAG.get(), c -> new FFVMonsterRenderer(c, texture("mire_hag")));
        event.registerEntityRenderer(ModEntities.AETHER_WISP.get(), c -> new FFVMonsterRenderer(c, texture("aether_wisp")));
        event.registerEntityRenderer(ModEntities.PYREWING.get(), c -> new FFVMonsterRenderer(c, texture("cinder_wolf")));
        event.registerEntityRenderer(ModEntities.FROSTMane.get(), c -> new FFVMonsterRenderer(c, texture("aether_wisp")));
        event.registerEntityRenderer(ModEntities.TERRAGORN.get(), c -> new FFVMonsterRenderer(c, texture("mire_hag")));
    }
    private static Identifier texture(String name) {
        return Identifier.fromNamespaceAndPath(FinalFantasyVReborn.MOD_ID, "textures/entity/" + name + ".png");
    }
}
