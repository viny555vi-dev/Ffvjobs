package com.ffvreborn.content.client;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
final class AetherParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    AetherParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz,
        SpriteSet sprites, float red, float green, float blue, float size) {
        super(level, x, y, z, dx, dy, dz);
        this.sprites = sprites;
        this.rCol = red; this.gCol = green; this.bCol = blue;
        this.quadSize = size;
        this.lifetime = 18 + this.random.nextInt(8);
        this.gravity = -0.015f;
        this.friction = 0.91f;
        this.hasPhysics = false;
        this.setSpriteFromAge(sprites);
    }
    @Override public void tick() {
        super.tick();
        this.setSpriteFromAge(sprites);
        this.alpha = Math.max(0.0f, 1.0f - (float) this.age / this.lifetime);
        this.quadSize *= 1.012f;
    }
    @Override public ParticleRenderType getRenderType() { return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT; }
}
