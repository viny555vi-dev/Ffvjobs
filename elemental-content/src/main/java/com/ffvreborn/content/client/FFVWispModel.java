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

/** Floating aether core with orbiting halo and animated spectral ribbons. */
public final class FFVWispModel extends EntityModel<LivingEntityRenderState> {
    private final ModelPart core, halo, ribbonA, ribbonB, ribbonC;
    public FFVWispModel(ModelPart root) {
        super(root); core=root.getChild("core"); halo=root.getChild("halo");
        ribbonA=root.getChild("ribbon_a"); ribbonB=root.getChild("ribbon_b"); ribbonC=root.getChild("ribbon_c");
    }
    public static LayerDefinition createLayer() {
        MeshDefinition mesh=new MeshDefinition(); PartDefinition root=mesh.getRoot();
        root.addOrReplaceChild("core",CubeListBuilder.create().texOffs(0,0).addBox(-4,-4,-4,8,8,8),PartPose.offset(0,11,0));
        root.addOrReplaceChild("halo",CubeListBuilder.create().texOffs(0,18).addBox(-7,-1,-7,14,2,14),PartPose.offset(0,10,0));
        root.addOrReplaceChild("ribbon_a",CubeListBuilder.create().texOffs(28,0).addBox(-1,-1,-1,2,12,2),PartPose.offset(4,12,0));
        root.addOrReplaceChild("ribbon_b",CubeListBuilder.create().texOffs(28,0).addBox(-1,-1,-1,2,12,2),PartPose.offset(-4,12,0));
        root.addOrReplaceChild("ribbon_c",CubeListBuilder.create().texOffs(36,0).addBox(-1,-1,-1,2,10,2),PartPose.offset(0,14,4));
        return LayerDefinition.create(mesh,64,64);
    }
    @Override public void setupAnim(LivingEntityRenderState state) {
        float t=state.ageInTicks*0.065f;
        core.y=11+Mth.sin(t)*1.1f; core.yRot=t*0.75f;
        halo.y=10+Mth.sin(t+0.8f)*0.55f; halo.yRot=-t*1.15f;
        ribbonA.zRot=Mth.sin(t*1.7f)*0.48f; ribbonA.xRot=0.22f+Mth.cos(t)*0.25f;
        ribbonB.zRot=-Mth.sin(t*1.7f+1.2f)*0.48f; ribbonB.xRot=-0.22f+Mth.sin(t)*0.25f;
        ribbonC.zRot=Mth.cos(t*1.25f)*0.35f; ribbonC.yRot=Mth.sin(t)*0.42f;
    }
}
