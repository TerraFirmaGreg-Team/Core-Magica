package com.terrafirmamagica.common.entity.baldeagle;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TFMBaldEagleRenderer extends MobRenderer<TFMBaldEagle, TFMBaldEagleModel<TFMBaldEagle>> {
    private static final ResourceLocation BALD_EAGLE_TEXTURE = ResourceLocation.fromNamespaceAndPath("totemic", "textures/entity/bald_eagle.png");

    public TFMBaldEagleRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new TFMBaldEagleModel(ctx.bakeLayer(TFMBaldEagleModel.LAYER_LOCATION)), 0.4F);
    }

    public ResourceLocation getTextureLocation(TFMBaldEagle entity) {
        return BALD_EAGLE_TEXTURE;
    }

    public float getBob(TFMBaldEagle pLivingBase, float pPartialTicks) {
        float f = Mth.lerp(pPartialTicks, pLivingBase.oFlap, pLivingBase.flap);
        float f1 = Mth.lerp(pPartialTicks, pLivingBase.oFlapSpeed, pLivingBase.flapSpeed);
        return (Mth.sin(f) + 1.0F) * f1;
    }
}
