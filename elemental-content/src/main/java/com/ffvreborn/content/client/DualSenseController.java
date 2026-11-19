package com.ffvreborn.content.client;

import com.ffvreborn.content.GamepadMapping;

import java.nio.IntBuffer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
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
        if (minecraft.player == null) {
            clear(minecraft.options);
            return;
        }
        wasConnected = true;
        Options options = minecraft.options;
        GamepadMapping.State input = GamepadMapping.map(
            axis(SDLGamepad.SDL_GAMEPAD_AXIS_LEFTX), axis(SDLGamepad.SDL_GAMEPAD_AXIS_LEFTY),
            axis(SDLGamepad.SDL_GAMEPAD_AXIS_RIGHTX), axis(SDLGamepad.SDL_GAMEPAD_AXIS_RIGHTY),
            axis(SDLGamepad.SDL_GAMEPAD_AXIS_LEFT_TRIGGER), axis(SDLGamepad.SDL_GAMEPAD_AXIS_RIGHT_TRIGGER),
            button(SDLGamepad.SDL_GAMEPAD_BUTTON_SOUTH), button(SDLGamepad.SDL_GAMEPAD_BUTTON_EAST),
            button(SDLGamepad.SDL_GAMEPAD_BUTTON_WEST), button(SDLGamepad.SDL_GAMEPAD_BUTTON_NORTH),
            button(SDLGamepad.SDL_GAMEPAD_BUTTON_TOUCHPAD));
        set(options.keyUp, input.up());
        set(options.keyDown, input.down());
        set(options.keyLeft, input.left());
        set(options.keyRight, input.right());
        set(options.keyJump, input.jump());
        set(options.keySprint, input.sprint());
        set(options.keyAttack, input.attack());
        set(options.keyUse, input.use());
        set(options.keyTogglePerspective, input.toggleCamera());
        if (Math.abs(input.yawDelta()) + Math.abs(input.pitchDelta()) > 0.02f) {
            minecraft.player.setYRot(minecraft.player.getYRot() + input.yawDelta());
            minecraft.player.setXRot(net.minecraft.util.Mth.clamp(
                minecraft.player.getXRot() + input.pitchDelta(), -89.0f, 89.0f));
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
        return net.minecraft.util.Mth.clamp(value, -1.0f, 1.0f);
    }
    private static boolean button(int button) { return pad != 0L && SDLGamepad.SDL_GetGamepadButton(pad, button); }
    private static void set(KeyMapping mapping, boolean pressed) { KeyMapping.set(mapping.getKey(), pressed); }
    private static void clear(Options o) {
        set(o.keyUp,false); set(o.keyDown,false); set(o.keyLeft,false); set(o.keyRight,false);
        set(o.keyJump,false); set(o.keySprint,false); set(o.keyAttack,false); set(o.keyUse,false);
        set(o.keyTogglePerspective,false);
    }
}
