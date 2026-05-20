package com.terrafirmamagica.common.entity.buffalo;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unchecked")
public class TFMBuffaloRenderer extends MobRenderer<TFMBuffalo, TFMBuffaloModel<TFMBuffalo>> {
    private static final ResourceLocation BUFFALO_TEXTURE = ResourceLocation.fromNamespaceAndPath("totemic", "textures/entity/buffalo.png");

    public TFMBuffaloRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new TFMBuffaloModel(ctx.bakeLayer(TFMBuffaloModel.LAYER_LOCATION)), 0.75F);
    }

    public ResourceLocation getTextureLocation(TFMBuffalo buffalo) {
        return BUFFALO_TEXTURE;
    }
}
