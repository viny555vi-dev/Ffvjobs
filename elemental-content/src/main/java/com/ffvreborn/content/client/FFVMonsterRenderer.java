package com.ffvreborn.content.client;

import net.minecraft.world.entity.monster.Monster;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public final class FFVMonsterRenderer extends LivingEntityRenderer<Monster, LivingEntityRenderState, FFVMonsterModel> {
    private final Identifier texture;
    public FFVMonsterRenderer(EntityRendererProvider.Context context, Identifier texture) {
        super(context, new FFVMonsterModel(context.bakeLayer(ClientEntityRegistration.MONSTER_LAYER)), 0.48f);
        this.texture = texture;
    }
    @Override public Identifier getTextureLocation(LivingEntityRenderState state) { return texture; }
    @Override public LivingEntityRenderState createRenderState() { return new LivingEntityRenderState(); }
}
