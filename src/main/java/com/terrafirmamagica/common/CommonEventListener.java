package com.terrafirmamagica.common;

import com.terrafirmamagica.TFMCore;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@EventBusSubscriber(modid = TFMCore.MOD_ID)
public class CommonEventListener {

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {

    }
}
