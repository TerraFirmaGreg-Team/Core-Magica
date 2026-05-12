package com.terrafirmamagica.common;

import com.terrafirmamagica.common.data.*;
import com.terrafirmamagica.common.rite.PreserveFoodRiteFactory;
import com.terrafirmamagica.world.feature.TFMFeatures;
import net.favouriteless.enchanted.api.circle_magic.RiteFactoryRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import static com.terrafirmamagica.TFMCore.REGISTRATE;

public class CommonInit {
    private static IEventBus modBus;

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
    }

    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent event) {
        RiteFactoryRegistry.get().register(
                PreserveFoodRiteFactory.ID,
                PreserveFoodRiteFactory.CODEC
        );
    }
}
