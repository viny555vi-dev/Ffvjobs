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

/** Quadruped beast rig with independent gait, jaw, spine and tail motion. */
public final class FFVBeastModel extends EntityModel<LivingEntityRenderState> {
    private final ModelPart body, head, snout, frontLeft, frontRight, rearLeft, rearRight, tail, mane;
    public FFVBeastModel(ModelPart root) {
        super(root);
        body=root.getChild("body"); head=root.getChild("head"); snout=root.getChild("snout");
        frontLeft=root.getChild("front_left"); frontRight=root.getChild("front_right");
        rearLeft=root.getChild("rear_left"); rearRight=root.getChild("rear_right");
        tail=root.getChild("tail"); mane=root.getChild("mane");
    }
    public static LayerDefinition createLayer() {
        MeshDefinition mesh=new MeshDefinition(); PartDefinition root=mesh.getRoot();
        root.addOrReplaceChild("body",CubeListBuilder.create().texOffs(0,0).addBox(-5,-4,-7,10,8,14),PartPose.offset(0,12,0));
        root.addOrReplaceChild("head",CubeListBuilder.create().texOffs(0,24).addBox(-4,-4,-4,8,7,8),PartPose.offset(0,9,-8));
        root.addOrReplaceChild("snout",CubeListBuilder.create().texOffs(32,24).addBox(-3,-2,-4,6,4,4),PartPose.offset(0,11,-11));
        root.addOrReplaceChild("front_left",CubeListBuilder.create().texOffs(40,0).addBox(-1.5f,0,-1.5f,3,7,3),PartPose.offset(3.5f,15,-5));
        root.addOrReplaceChild("front_right",CubeListBuilder.create().texOffs(40,0).mirror().addBox(-1.5f,0,-1.5f,3,7,3),PartPose.offset(-3.5f,15,-5));
        root.addOrReplaceChild("rear_left",CubeListBuilder.create().texOffs(40,0).addBox(-1.5f,0,-1.5f,3,7,3),PartPose.offset(3.5f,15,5));
        root.addOrReplaceChild("rear_right",CubeListBuilder.create().texOffs(40,0).mirror().addBox(-1.5f,0,-1.5f,3,7,3),PartPose.offset(-3.5f,15,5));
        root.addOrReplaceChild("tail",CubeListBuilder.create().texOffs(52,0).addBox(-1.5f,0,-1.5f,3,10,3),PartPose.offset(0,10,7));
        root.addOrReplaceChild("mane",CubeListBuilder.create().texOffs(32,34).addBox(-5,-2,-4,10,4,8),PartPose.offset(0,9,-5));
        return LayerDefinition.create(mesh,64,64);
    }
    @Override public void setupAnim(LivingEntityRenderState state) {
        float gait=state.walkAnimationPos*0.95f, amount=Math.min(1.0f,state.walkAnimationSpeed);
        float idle=state.ageInTicks*0.09f;
        head.xRot=state.xRot*Mth.DEG_TO_RAD+Mth.sin(idle)*0.035f;
        head.yRot=state.yRot*Mth.DEG_TO_RAD;
        snout.xRot=head.xRot*0.45f;
        body.xRot=0.035f+amount*0.06f;
        frontLeft.xRot=Mth.cos(gait)*0.95f*amount;
        rearRight.xRot=frontLeft.xRot;
        frontRight.xRot=Mth.cos(gait+Mth.PI)*0.95f*amount;
        rearLeft.xRot=frontRight.xRot;
        tail.xRot=0.18f+Mth.sin(idle*1.5f)*0.25f;
        tail.yRot=Mth.sin(idle)*0.28f;
        mane.yRot=Mth.sin(idle*0.7f)*0.04f;
    }
}
