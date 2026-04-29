package com.terrafirmamagica.common.rite;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.terrafirmamagica.TFMCore;
import net.favouriteless.enchanted.api.circle_magic.RiteFactory;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.Rite;
import net.minecraft.resources.ResourceLocation;

public record PreserveFoodRiteFactory(double radius, int maxItems, int duration) implements RiteFactory {

    public static final MapCodec<PreserveFoodRiteFactory> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.DOUBLE.optionalFieldOf("radius", 3.0).forGetter(PreserveFoodRiteFactory::radius),
            Codec.INT.optionalFieldOf("max_items", 16).forGetter(PreserveFoodRiteFactory::maxItems),
            Codec.INT.optionalFieldOf("duration", 100).forGetter(PreserveFoodRiteFactory::duration)
    ).apply(inst, PreserveFoodRiteFactory::new));

    public static final ResourceLocation ID = TFMCore.id("preserve_food");

    @Override
    public Rite create(Rite.BaseRiteParams baseParams, Rite.RiteParams params) {
        return new PreserveFoodRite(baseParams, params, radius, maxItems, duration);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}