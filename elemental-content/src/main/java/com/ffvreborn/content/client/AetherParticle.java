package com.ffvreborn.content.client;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.world.level.block.state.BlockState;
/** Terrain-backed custom quad particle assigned to one of the Reborn render groups. */
final class AetherParticle extends TerrainParticle {
    private final ParticleRenderType group;
    AetherParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz,
        BlockState visual, ParticleRenderType group) {
        super(level, x, y, z, dx, dy, dz, visual);
        this.group = group;
    }
    @Override public ParticleRenderType getGroup() { return group; }
}
