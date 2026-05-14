package com.terrafirmamagica.common;

import static com.terrafirmamagica.TFMCore.REGISTRATE;

import com.terrafirmamagica.TFMCore;
import com.terrafirmamagica.common.data.*;
import com.terrafirmamagica.common.data.blocks.TFMBlocks;
import com.terrafirmamagica.common.eidolon.TFMEidolonRegistry;
import com.terrafirmamagica.common.food.TFMFoodIngredient;
import com.terrafirmamagica.common.rite.PreserveFoodRiteFactory;
import com.terrafirmamagica.world.feature.TFMFeatures;

import net.favouriteless.enchanted.api.circle_magic.RiteFactoryRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

public class CommonInit {
    private static IEventBus modBus;

    static {
        TFMCore.REGISTRATE.creativeModeTab(() -> TFMCreativeTab.TFM);
    }

    public static void init(final IEventBus modBus) {
        CommonInit.modBus = modBus;
        modBus.register(CommonInit.class);
        REGISTRATE.registerEventListeners(modBus);

        TFMCreativeTab.init();
    }

    private static boolean didRunRegistration = false;

    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        if (didRunRegistration) {
            return;
        }
        didRunRegistration = true;

        TFMBlocks.init();
        TFMItems.init();
        TFMEntities.init();
        TFMBlockEntities.init();
        TFMFeatures.register(modBus);
        TFMFoodIngredient.register(modBus);
        TFMFoodTraits.register(modBus);
        TFMCeremonies.CEREMONIES.register(modBus);
    }

    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent event) {
        RiteFactoryRegistry.get().register(
                PreserveFoodRiteFactory.ID,
                PreserveFoodRiteFactory.CODEC);

        TFMEidolonRegistry.init();
    }
}
