package com.ffvreborn.content.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Shoulder offset applied after vanilla camera collision and zoom are resolved. */
@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow private boolean detached;
    @Shadow @Nullable private Entity entity;
    @Shadow protected abstract void move(float forwards, float up, float right);

    @Inject(method = "update", at = @At("TAIL"))
    private void ffv$applyShoulderOffset(DeltaTracker deltaTracker, CallbackInfo callback) {
        if (detached && entity instanceof Player) {
            move(0.0f, 0.0f, 0.46f);
        }
    }
}
