package com.terrafirmamagica.client;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

public class ClientInit {
    private static IEventBus modBus;

    public static void init(final IEventBus modBus) {
        ClientInit.modBus = modBus;
        modBus.register(ClientInit.class);

    }

    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
    }
}
