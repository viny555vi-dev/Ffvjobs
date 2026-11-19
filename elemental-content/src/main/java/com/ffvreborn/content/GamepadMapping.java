package com.ffvreborn.content;

import net.minecraft.util.Mth;

/** Pure SDL gamepad mapping, kept separate so its input contract can be tested without hardware. */
public final class GamepadMapping {
    public record State(boolean up, boolean down, boolean left, boolean right,
        boolean jump, boolean sprint, boolean attack, boolean use, boolean toggleCamera,
        float yawDelta, float pitchDelta) {}
    private GamepadMapping() {}
    public static State map(float leftX, float leftY, float rightX, float rightY,
        float leftTrigger, float rightTrigger, boolean south, boolean east,
        boolean west, boolean north, boolean touchpad) {
        float lx = axis(leftX), ly = axis(leftY);
        float rx = deadzone(rightX), ry = deadzone(rightY);
        return new State(ly < -0.24f, ly > 0.24f, lx < -0.24f, lx > 0.24f,
            south, east, west || leftTrigger(rightTrigger), north || leftTrigger(leftTrigger),
            touchpad, -rx * 3.2f, ry * 2.7f);
    }
    private static float axis(float value) {
        return Math.abs(value) < 0.16f ? 0.0f : Mth.clamp(value, -1.0f, 1.0f);
    }
    private static float deadzone(float value) {
        return Math.abs(value) < 0.12f ? 0.0f : Mth.clamp(value, -1.0f, 1.0f);
    }
    private static boolean leftTrigger(float value) { return value > 0.35f; }
}
