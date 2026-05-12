package com.terrafirmamagica.world.feature;

import java.util.function.Supplier;

import com.terrafirmamagica.TFMCore;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TFMFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, TFMCore.MOD_ID);

    public static final Supplier<Feature<NoneFeatureConfiguration>> FIXED_ETHER_SOURCE = FEATURES.register("fixed_ether_source",
            () -> new FixedEtherSourceFeature(NoneFeatureConfiguration.CODEC));

    public static void register(IEventBus modEventBus) {
        FEATURES.register(modEventBus);
    }
}
