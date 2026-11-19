package com.ffvreborn.content.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

/** Hand-built skeletal creature model with procedural locomotion and idle animation. */
public final class FFVMonsterModel extends EntityModel<LivingEntityRenderState> {
    private final ModelPart head, torso, leftArm, rightArm, leftLeg, rightLeg, tail, crest;
    public FFVMonsterModel(ModelPart root) {
        super(root);
        head = root.getChild("head"); torso = root.getChild("torso");
        leftArm = root.getChild("left_arm"); rightArm = root.getChild("right_arm");
        leftLeg = root.getChild("left_leg"); rightLeg = root.getChild("right_leg");
        tail = root.getChild("tail"); crest = root.getChild("crest");
    }
    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 0).addBox(-4, -5, -3, 8, 10, 6), PartPose.offset(0, 12, 0));
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 18).addBox(-3, -3, -3, 6, 6, 6), PartPose.offset(0, 5, -1));
        root.addOrReplaceChild("crest", CubeListBuilder.create().texOffs(24, 18).addBox(-1, -5, -1, 2, 5, 2), PartPose.offset(0, 3, -1));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(28, 0).addBox(-1, -1, -1.5f, 3, 8, 3), PartPose.offset(4, 8, 0));
        root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(28, 0).mirror().addBox(-2, -1, -1.5f, 3, 8, 3), PartPose.offset(-4, 8, 0));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(40, 0).addBox(-1.5f, 0, -1.5f, 3, 7, 3), PartPose.offset(2, 17, 0));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(40, 0).mirror().addBox(-1.5f, 0, -1.5f, 3, 7, 3), PartPose.offset(-2, 17, 0));
        root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(48, 0).addBox(-1, 0, -1, 2, 9, 2), PartPose.offset(0, 13, 3));
        return LayerDefinition.create(mesh, 64, 64);
    }
    @Override public void setupAnim(LivingEntityRenderState state) {
        float walk = state.walkAnimationPos * 0.72f;
        float strength = Math.min(1.0f, state.walkAnimationSpeed);
        float idle = state.ageInTicks * 0.075f;
        head.xRot = state.xRot * Mth.DEG_TO_RAD + Mth.sin(idle) * 0.035f;
        head.yRot = state.yRot * Mth.DEG_TO_RAD;
        torso.xRot = 0.06f + strength * 0.10f;
        leftLeg.xRot = Mth.cos(walk) * 1.15f * strength;
        rightLeg.xRot = Mth.cos(walk + Mth.PI) * 1.15f * strength;
        leftArm.xRot = Mth.cos(walk + Mth.PI) * 0.85f * strength - 0.08f;
        rightArm.xRot = Mth.cos(walk) * 0.85f * strength - 0.08f;
        tail.xRot = 0.28f + Mth.sin(idle * 1.7f) * 0.22f + strength * 0.18f;
        tail.yRot = Mth.sin(idle) * 0.32f;
        crest.zRot = Mth.sin(idle * 1.3f) * 0.08f;
    }
}
