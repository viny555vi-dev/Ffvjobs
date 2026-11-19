package com.ffvreborn.content.client;

import java.nio.IntBuffer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.lwjgl.sdl.SDLGamepad;

/** Native SDL3 gamepad bridge. Supports DualSense directly and DualSenseX XInput emulation. */
@EventBusSubscriber(value = Dist.CLIENT, modid = "ffv_reborn")
public final class DualSenseController {
    private static long pad;
    private static int deviceId = -1;
    private static boolean wasConnected;
    private DualSenseController() {}

    @SubscribeEvent
    public static void tick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        SDLGamepad.SDL_UpdateGamepads();
        acquire();
        if (pad == 0L || !SDLGamepad.SDL_GamepadConnected(pad)) {
            clear(minecraft.options);
            wasConnected = false;
            return;
        }
        if (minecraft.player == null || minecraft.screen != null) {
            clear(minecraft.options);
            return;
        }
        wasConnected = true;
        Options options = minecraft.options;
        float x = axis(SDLGamepad.SDL_GAMEPAD_AXIS_LEFTX);
        float y = axis(SDLGamepad.SDL_GAMEPAD_AXIS_LEFTY);
        set(options.keyUp, y < -0.24f);
        set(options.keyDown, y > 0.24f);
        set(options.keyLeft, x < -0.24f);
        set(options.keyRight, x > 0.24f);
        boolean south = button(SDLGamepad.SDL_GAMEPAD_BUTTON_SOUTH);
        boolean east = button(SDLGamepad.SDL_GAMEPAD_BUTTON_EAST);
        boolean west = button(SDLGamepad.SDL_GAMEPAD_BUTTON_WEST);
        boolean north = button(SDLGamepad.SDL_GAMEPAD_BUTTON_NORTH);
        float leftTrigger = axis(SDLGamepad.SDL_GAMEPAD_AXIS_LEFT_TRIGGER);
        float rightTrigger = axis(SDLGamepad.SDL_GAMEPAD_AXIS_RIGHT_TRIGGER);
        set(options.keyJump, south);
        set(options.keySprint, east);
        set(options.keyAttack, west || rightTrigger > 0.35f);
        set(options.keyUse, north || leftTrigger > 0.35f);
        set(options.keyTogglePerspective, button(SDLGamepad.SDL_GAMEPAD_BUTTON_TOUCHPAD));
        float rx = deadzone(axis(SDLGamepad.SDL_GAMEPAD_AXIS_RIGHTX));
        float ry = deadzone(axis(SDLGamepad.SDL_GAMEPAD_AXIS_RIGHTY));
        if (Math.abs(rx) + Math.abs(ry) > 0.02f) {
            minecraft.player.setYRot(minecraft.player.getYRot() - rx * 3.2f);
            minecraft.player.setXRot(Mth.clamp(minecraft.player.getXRot() + ry * 2.7f, -89.0f, 89.0f));
        }
    }

    private static void acquire() {
        if (pad != 0L && SDLGamepad.SDL_GamepadConnected(pad)) return;
        pad = 0L;
        deviceId = -1;
        IntBuffer ids = SDLGamepad.SDL_GetGamepads();
        if (ids == null) return;
        for (int i = 0; i < ids.remaining(); i++) {
            int id = ids.get(i);
            if (SDLGamepad.SDL_IsGamepad(id)) {
                long opened = SDLGamepad.SDL_OpenGamepad(id);
                if (opened != 0L) { pad = opened; deviceId = id; return; }
            }
        }
    }
    private static float axis(int axis) {
        if (pad == 0L) return 0.0f;
        float value = SDLGamepad.SDL_GetGamepadAxis(pad, axis) / 32767.0f;
        return Math.abs(value) < 0.16f ? 0.0f : Mth.clamp(value, -1.0f, 1.0f);
    }
    private static float deadzone(float value) { return Math.abs(value) < 0.12f ? 0.0f : value; }
    private static boolean button(int button) { return pad != 0L && SDLGamepad.SDL_GetGamepadButton(pad, button); }
    private static void set(KeyMapping mapping, boolean pressed) { KeyMapping.set(mapping.getKey(), pressed); }
    private static void clear(Options o) {
        set(o.keyUp,false); set(o.keyDown,false); set(o.keyLeft,false); set(o.keyRight,false);
        set(o.keyJump,false); set(o.keySprint,false); set(o.keyAttack,false); set(o.keyUse,false);
        set(o.keyTogglePerspective,false);
    }
}
