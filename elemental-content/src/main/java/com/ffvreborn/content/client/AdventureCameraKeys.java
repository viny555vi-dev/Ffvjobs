package com.ffvreborn.content.client;

import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

/** Dedicated toggle for the Reborn adventure camera. */
@EventBusSubscriber(value = Dist.CLIENT, modid = "ffv_reborn")
public final class AdventureCameraKeys {
    public static final KeyMapping TOGGLE_CAMERA = new KeyMapping(
        "key.ffv_reborn.toggle_camera", net.minecraft.client.InputType.KEYSYM, GLFW.GLFW_KEY_V,
        KeyMapping.Category.register(Identifier.fromNamespaceAndPath("ffv_reborn", "camera")));
    private AdventureCameraKeys() {}
    @SubscribeEvent public static void register(RegisterKeyMappingsEvent event) {
        event.registerCategory(TOGGLE_CAMERA.getCategory());
        event.register(TOGGLE_CAMERA);
    }
    @SubscribeEvent public static void tick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        while (TOGGLE_CAMERA.consumeClick()) {
            CameraType current = minecraft.options.getCameraType();
            minecraft.options.setCameraType(current.isFirstPerson() ? CameraType.THIRD_PERSON_BACK : CameraType.FIRST_PERSON);
        }
    }
}
