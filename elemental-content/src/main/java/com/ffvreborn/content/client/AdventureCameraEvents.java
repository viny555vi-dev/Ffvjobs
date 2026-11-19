package com.ffvreborn.content.client;

import net.minecraft.client.Camera;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.CalculateDetachedCameraDistanceEvent;
import net.neoforged.neoforge.client.event.CalculatePlayerTurnEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

/** Third-person adventure camera tuning: wider follow distance, restrained turn response and motion-aware framing. */
@EventBusSubscriber(value = Dist.CLIENT, modid = "ffv_reborn")
public final class AdventureCameraEvents {
    private AdventureCameraEvents() {}
    @SubscribeEvent public static void distance(CalculateDetachedCameraDistanceEvent event) {
        event.setDistance(4.35f);
    }
    @SubscribeEvent public static void turn(CalculatePlayerTurnEvent event) {
        event.setMouseSensitivity(event.getMouseSensitivity() * 0.92d);
        event.setCinematicCameraEnabled(true);
    }
    @SubscribeEvent public static void fov(ViewportEvent.ComputeFov event) {
        Camera camera = event.getCamera();
        if (camera.getEntity() instanceof Player player) {
            double speed = Math.sqrt(player.getDeltaMovement().horizontalDistanceSqr());
            event.setFOV(event.getFOV() + (float) Math.min(2.4d, speed * 3.2d));
        }
    }
    @SubscribeEvent public static void angles(ViewportEvent.ComputeCameraAngles event) {
        Camera camera = event.getCamera();
        if (camera.getEntity() instanceof Player player) {
            double speed = Math.sqrt(player.getDeltaMovement().horizontalDistanceSqr());
            float strideRoll = (float) Math.sin(player.tickCount * 0.18d) * (float) Math.min(0.65d, speed * 0.20d);
            event.setRoll(event.getRoll() + strideRoll);
        }
    }
}
