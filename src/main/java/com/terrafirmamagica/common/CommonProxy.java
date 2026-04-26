package com.terrafirmamagica.common;

import com.terrafirmamagica.common.data.TFMBlocks;
import com.terrafirmamagica.common.data.TFMCreativeTab;
import com.terrafirmamagica.world.feature.TFMFeatures;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import static com.terrafirmamagica.TFMCore.REGISTRATE;

public class CommonProxy {
    private static IEventBus modBus;

    public static void init(final IEventBus modBus) {
        CommonProxy.modBus = modBus;
        modBus.register(CommonProxy.class);
        REGISTRATE.registerEventListeners(modBus);
        TFMCreativeTab.init();
    }

    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        TFMFeatures.register(modBus);
        TFMBlocks.init();
    }

    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent event) {

    }
}
