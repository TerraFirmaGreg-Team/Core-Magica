package com.terrafirmamagica.common;

import com.terrafirmamagica.TFMCore;
import com.terrafirmamagica.common.entity.baldeagle.TFMBaldEagleModel;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@EventBusSubscriber(modid = TFMCore.MOD_ID)
public class CommonEventListener {

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {

    }

    @SubscribeEvent
    public static void onEntityLayerRegister(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TFMBaldEagleModel.LAYER_LOCATION, TFMBaldEagleModel::createLayer);
    }
}
