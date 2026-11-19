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

/** Winged summon rig with independent wing beats, talons and aerial body pitch. */
public final class FFVWingedModel extends EntityModel<LivingEntityRenderState> {
    private final ModelPart torso, head, wingLeft, wingRight, legLeft, legRight, tail;
    public FFVWingedModel(ModelPart root) {
        super(root); torso=root.getChild("torso"); head=root.getChild("head");
        wingLeft=root.getChild("wing_left"); wingRight=root.getChild("wing_right");
        legLeft=root.getChild("leg_left"); legRight=root.getChild("leg_right"); tail=root.getChild("tail");
    }
    public static LayerDefinition createLayer() {
        MeshDefinition mesh=new MeshDefinition(); PartDefinition root=mesh.getRoot();
        root.addOrReplaceChild("torso",CubeListBuilder.create().texOffs(0,0).addBox(-3,-5,-2,6,10,4),PartPose.offset(0,12,0));
        root.addOrReplaceChild("head",CubeListBuilder.create().texOffs(0,16).addBox(-3,-3,-3,6,6,6),PartPose.offset(0,5,-1));
        root.addOrReplaceChild("wing_left",CubeListBuilder.create().texOffs(24,0).addBox(0,-1,-1,12,2,8),PartPose.offset(3,8,0));
        root.addOrReplaceChild("wing_right",CubeListBuilder.create().texOffs(24,0).mirror().addBox(-12,-1,-1,12,2,8),PartPose.offset(-3,8,0));
        root.addOrReplaceChild("leg_left",CubeListBuilder.create().texOffs(0,30).addBox(-1,0,-1,2,6,2),PartPose.offset(2,17,0));
        root.addOrReplaceChild("leg_right",CubeListBuilder.create().texOffs(0,30).mirror().addBox(-1,0,-1,2,6,2),PartPose.offset(-2,17,0));
        root.addOrReplaceChild("tail",CubeListBuilder.create().texOffs(8,30).addBox(-1,0,-1,2,9,2),PartPose.offset(0,12,2));
        return LayerDefinition.create(mesh,64,64);
    }
    @Override public void setupAnim(LivingEntityRenderState state) {
        float t=state.ageInTicks*0.42f, flap=Mth.sin(t)*0.72f;
        wingLeft.zRot=flap; wingRight.zRot=-flap;
        wingLeft.xRot=0.12f+Mth.cos(t)*0.14f; wingRight.xRot=-wingLeft.xRot;
        torso.xRot=-0.08f+Mth.sin(t*0.5f)*0.05f;
        head.xRot=state.xRot*Mth.DEG_TO_RAD; head.yRot=state.yRot*Mth.DEG_TO_RAD;
        legLeft.xRot=0.2f+Mth.sin(t)*0.15f; legRight.xRot=0.2f-Mth.sin(t)*0.15f;
        tail.xRot=0.3f+Mth.sin(t*0.8f)*0.2f;
    }
}
