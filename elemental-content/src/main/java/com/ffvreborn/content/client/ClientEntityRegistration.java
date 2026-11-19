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
    public static final ModelLayerLocation HUMANOID_LAYER = layer("bestiary_humanoid");
    public static final ModelLayerLocation BEAST_LAYER = layer("bestiary_beast");
    public static final ModelLayerLocation WISP_LAYER = layer("bestiary_wisp");
    public static final ModelLayerLocation WINGED_LAYER = layer("bestiary_winged");
    private ClientEntityRegistration() {}
    private static ModelLayerLocation layer(String name) {
        return new ModelLayerLocation(Identifier.fromNamespaceAndPath(FinalFantasyVReborn.MOD_ID, name), "main");
    }
    @SubscribeEvent public static void layers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(HUMANOID_LAYER, FFVMonsterModel::createLayer);
        event.registerLayerDefinition(BEAST_LAYER, FFVBeastModel::createLayer);
        event.registerLayerDefinition(WISP_LAYER, FFVWispModel::createLayer);
        event.registerLayerDefinition(WINGED_LAYER, FFVWingedModel::createLayer);
    }
    @SubscribeEvent public static void renderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CINDER_WOLF.get(), c -> new FFVMonsterRenderer<>(c,
            new FFVBeastModel(c.bakeLayer(BEAST_LAYER)), texture("cinder_wolf"), 0.62f));
        event.registerEntityRenderer(ModEntities.MIRE_HAG.get(), c -> new FFVMonsterRenderer<>(c,
            new FFVMonsterModel(c.bakeLayer(HUMANOID_LAYER)), texture("mire_hag"), 0.52f));
        event.registerEntityRenderer(ModEntities.AETHER_WISP.get(), c -> new FFVMonsterRenderer<>(c,
            new FFVWispModel(c.bakeLayer(WISP_LAYER)), texture("aether_wisp"), 0.35f));
        event.registerEntityRenderer(ModEntities.PYREWING.get(), c -> new FFVMonsterRenderer<>(c,
            new FFVWingedModel(c.bakeLayer(WINGED_LAYER)), texture("cinder_wolf"), 0.5f));
        event.registerEntityRenderer(ModEntities.FROSTMANE.get(), c -> new FFVMonsterRenderer<>(c,
            new FFVBeastModel(c.bakeLayer(BEAST_LAYER)), texture("aether_wisp"), 0.62f));
        event.registerEntityRenderer(ModEntities.TERRAGORN.get(), c -> new FFVMonsterRenderer<>(c,
            new FFVMonsterModel(c.bakeLayer(HUMANOID_LAYER)), texture("mire_hag"), 0.72f));
    }
    private static Identifier texture(String name) {
        return Identifier.fromNamespaceAndPath(FinalFantasyVReborn.MOD_ID, "textures/entity/" + name + ".png");
    }
}
