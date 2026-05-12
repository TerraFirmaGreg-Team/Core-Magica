package com.terrafirmamagica.common.entity.baldeagle;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TFMBaldEagleModel<T extends TFMBaldEagle> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("totemic", "bald_eagle"), "main");

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final ModelPart tail;

    public TFMBaldEagleModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.leftLeg = root.getChild("leftLeg");
        this.rightLeg = root.getChild("rightLeg");
        this.leftWing = root.getChild("leftWing");
        this.rightWing = root.getChild("rightWing");
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(10, 5).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, false), PartPose.offset(0.0F, 15.0F, -2.76F));
        head.addOrReplaceChild("headTop", CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, false), PartPose.offset(0.0F, -2.0F, -1.0F));
        head.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(2, 0).addBox(-0.5F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, false), PartPose.offset(-0.5F, -0.5F, -1.5F));
        PartDefinition beak = head.addOrReplaceChild("beak", CubeListBuilder.create().texOffs(0, 3).addBox(-0.5F, 0.1F, -3.3F, 2.0F, 2.0F, 3.0F, false),
                PartPose.offsetAndRotation(-0.5F, -1.65F, -1.65F, 0.0F, 0.006457718F, 0.0F));
        beak.addOrReplaceChild("beakTip", CubeListBuilder.create().texOffs(2, 8).addBox(-0.5F, 1.2F, -3.35F, 2.0F, 1.0F, 1.0F, false), PartPose.offset(0.0F, 0.15F, 0.05F));
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(8, 10).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 7.0F, 3.0F, false),
                PartPose.offsetAndRotation(0.0F, 16.0F, -3.0F, 0.49375364F, 0.0F, 0.0F));
        root.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(8, 20).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, false), PartPose.offset(-1.0F, 22.0F, -1.05F));
        root.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(12, 20).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, false), PartPose.offset(1.0F, 22.0F, -1.05F));
        root.addOrReplaceChild("leftWing", CubeListBuilder.create().texOffs(0, 10).addBox(-0.5F, 0.0F, -1.5F, 1.0F, 6.0F, 3.0F, false),
                PartPose.offsetAndRotation(-1.5F, 16.94F, -2.76F, -0.6981317F, -(float) Math.PI, 0.08726646F));
        root.addOrReplaceChild("rightWing", CubeListBuilder.create().texOffs(20, 10).addBox(-0.5F, 0.0F, -1.5F, 1.0F, 6.0F, 3.0F, false),
                PartPose.offsetAndRotation(1.5F, 16.94F, -2.76F, -0.6981317F, -(float) Math.PI, -0.08726646F));
        root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 20).addBox(-1.5F, -0.2F, -1.0F, 3.0F, 5.0F, 1.0F, false),
                PartPose.offsetAndRotation(0.0F, 21.07F, 1.16F, 1.0831513F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 32, 32);
    }

    public ModelPart root() {
        return this.root;
    }

    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.setupAnim(pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
    }

    private void setupAnim(float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.head.xRot = pHeadPitch * ((float) Math.PI / 180F);
        this.head.yRot = pNetHeadYaw * ((float) Math.PI / 180F);
        this.head.zRot = 0.0F;
        this.head.x = 0.0F;
        this.body.x = 0.0F;
        this.tail.x = 0.0F;
        this.rightWing.x = -1.5F;
        this.leftWing.x = 1.5F;
        float f2 = pAgeInTicks * 0.3F;
        this.head.y = 15.69F + f2;
        this.tail.xRot = 1.015F + Mth.cos(pLimbSwing * 0.6662F) * 0.3F * pLimbSwingAmount;
        this.tail.y = 21.07F + f2;
        this.body.y = 16.5F + f2;
        this.leftWing.zRot = -0.0873F - pAgeInTicks;
        this.leftWing.y = 16.94F + f2;
        this.rightWing.zRot = 0.0873F + pAgeInTicks;
        this.rightWing.y = 16.94F + f2;
        this.leftLeg.y = 22.0F + f2;
        this.rightLeg.y = 22.0F + f2;
    }
}
