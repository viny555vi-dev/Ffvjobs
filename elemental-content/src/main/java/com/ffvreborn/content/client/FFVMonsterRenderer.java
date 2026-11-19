package com.ffvreborn.content.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

/** Shared renderer shell; each creature supplies its own hand-built animated rig. */
public final class FFVMonsterRenderer<M extends EntityModel<LivingEntityRenderState>>
    extends LivingEntityRenderer<Monster, LivingEntityRenderState, M> {
    private final Identifier texture;
    public FFVMonsterRenderer(EntityRendererProvider.Context context, M model, Identifier texture, float shadowRadius) {
        super(context, model, shadowRadius);
        this.texture = texture;
    }
    @Override public Identifier getTextureLocation(LivingEntityRenderState state) { return texture; }
    @Override public LivingEntityRenderState createRenderState() { return new LivingEntityRenderState(); }
}
