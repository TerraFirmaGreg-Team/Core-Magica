package com.terrafirmamagica.common.entity.buffalo;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TFMBuffaloModel<T extends TFMBuffalo> extends AgeableListModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("totemic", "buffalo"), "main");

    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;

    public TFMBuffaloModel(ModelPart root) {
        super(true, 10.0F, 2.0F);
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.tail = root.getChild("tail");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 45).addBox(-4.5F, -7.0F, -7.0F, 9.0F, 7.0F, 9.0F, true),
                PartPose.offsetAndRotation(0.0F, 6.0F, -7.0F, ((float) Math.PI / 2F), 0.0F, 0.0F));
        head.addOrReplaceChild("hornbase1", CubeListBuilder.create().texOffs(52, 0).addBox(-7.5F, -4.0F, -1.0F, 4.0F, 2.0F, 2.0F, true), PartPose.ZERO);
        head.addOrReplaceChild("hornbase2", CubeListBuilder.create().texOffs(52, 0).addBox(3.5F, -4.0F, -1.0F, 4.0F, 2.0F, 2.0F, false), PartPose.ZERO);
        head.addOrReplaceChild("horn1", CubeListBuilder.create().texOffs(52, 4).addBox(6.5F, -4.0F, 0.0F, 2.0F, 2.0F, 4.0F, false), PartPose.ZERO);
        head.addOrReplaceChild("horn2", CubeListBuilder.create().texOffs(52, 10).addBox(5.5F, -4.0F, 2.0F, 2.0F, 2.0F, 3.0F, false), PartPose.ZERO);
        head.addOrReplaceChild("horn3", CubeListBuilder.create().texOffs(52, 15).addBox(5.5F, -4.0F, 5.0F, 1.0F, 1.0F, 1.0F, false), PartPose.ZERO);
        head.addOrReplaceChild("horn4", CubeListBuilder.create().texOffs(52, 4).addBox(-8.5F, -4.0F, 0.0F, 2.0F, 2.0F, 4.0F, true), PartPose.ZERO);
        head.addOrReplaceChild("horn5", CubeListBuilder.create().texOffs(52, 10).addBox(-7.5F, -4.0F, 2.0F, 2.0F, 2.0F, 3.0F, true), PartPose.ZERO);
        head.addOrReplaceChild("horn6", CubeListBuilder.create().texOffs(52, 15).addBox(-6.5F, -4.0F, 5.0F, 1.0F, 1.0F, 1.0F, true), PartPose.ZERO);
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -10.0F, -9.0F, 12.0F, 10.0F, 13.0F, true),
                PartPose.offsetAndRotation(0.0F, 7.0F, 2.0F, 1.4835298F, 0.0F, 0.0F));
        body.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 23).addBox(-5.5F, 0.0F, -8.5F, 11.0F, 10.0F, 12.0F, true), PartPose.ZERO);
        body.addOrReplaceChild("udder", CubeListBuilder.create().texOffs(28, 46).addBox(-3.5F, 4.0F, -9.5F, 7.0F, 5.0F, 1.0F, true), PartPose.ZERO);
        PartDefinition tail = root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(28, 53).addBox(-1.0F, 0.0F, -9.5F, 2.0F, 1.0F, 8.0F, true),
                PartPose.offsetAndRotation(0.0F, 7.0F, 11.5F, ((float) Math.PI / 2F), 0.0F, 0.0F));
        tail.addOrReplaceChild("tailhairs", CubeListBuilder.create().texOffs(35, 62).addBox(-1.0F, 0.0F, -10.5F, 2.0F, 1.0F, 1.0F, true), PartPose.ZERO);
        PartDefinition leg1 = root.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(46, 18).addBox(-1.0F, 0.0F, -3.0F, 4.0F, 11.0F, 5.0F, false),
                PartPose.offsetAndRotation(4.0F, 10.0F, -5.0F, 0.13962634F, 0.0F, 0.0F));
        leg1.addOrReplaceChild("hoof1", CubeListBuilder.create().texOffs(46, 34).addBox(-0.5F, 8.0F, 0.0F, 3.0F, 6.0F, 3.0F, false), PartPose.rotation(-0.13962634F, 0.0F, 0.0F));
        PartDefinition leg2 = root.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(46, 18).addBox(-3.0F, 0.0F, -3.0F, 4.0F, 11.0F, 5.0F, true),
                PartPose.offsetAndRotation(-4.0F, 10.0F, -5.0F, 0.13962634F, 0.0F, 0.0F));
        leg2.addOrReplaceChild("hoof2", CubeListBuilder.create().texOffs(46, 34).addBox(-2.5F, 8.0F, 0.0F, 3.0F, 6.0F, 3.0F, true), PartPose.rotation(-0.13962634F, 0.0F, 0.0F));
        PartDefinition leg3 = root.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(48, 43).addBox(-1.0F, 0.0F, -3.0F, 4.0F, 8.0F, 4.0F, false), PartPose.offset(4.0F, 10.0F, 10.0F));
        leg3.addOrReplaceChild("hoof3", CubeListBuilder.create().texOffs(48, 55).addBox(-0.5F, 8.0F, -2.0F, 3.0F, 6.0F, 3.0F, false), PartPose.ZERO);
        PartDefinition leg4 = root.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(48, 43).addBox(-3.0F, 0.0F, -3.0F, 4.0F, 8.0F, 4.0F, true), PartPose.offset(-4.0F, 10.0F, 10.0F));
        leg4.addOrReplaceChild("hoof4", CubeListBuilder.create().texOffs(48, 55).addBox(-2.5F, 8.0F, -2.0F, 3.0F, 6.0F, 3.0F, true), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 64);
    }

    protected Iterable<ModelPart> headParts() {
        return List.of(this.head);
    }

    protected Iterable<ModelPart> bodyParts() {
        return List.of(this.body, this.tail, this.leg1, this.leg2, this.leg3, this.leg4);
    }

    public void setupAnim(T buffalo, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float legSpeed = 0.6662F;
        float legFactor = 1.4F;
        this.head.xRot = headPitch * ((float) Math.PI / 180F) + ((float) Math.PI / 2F);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount + 0.13962634F;
        this.leg2.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount + 0.13962634F;
        this.leg3.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    public void renderToBuffer(PoseStack ps, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        ps.pushPose();
        ps.translate(0.0F, -0.75F, 0.0F);
        ps.scale(1.5F, 1.5F, 1.5F);
        super.renderToBuffer(ps, buffer, packedLight, packedOverlay, color);
        ps.popPose();
    }
}
