package com.terrafirmamagica.compat.witchery;

import com.terrafirmamagica.common.ritual.TFMPreserveFoodRitual;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import dev.sterner.witchery.core.api.Ritual;
import dev.sterner.witchery.core.registry.WitcheryRitualRegistry;

public class TFMWitcheryRituals {
    private static final DeferredRegister<Ritual> RITUALS = DeferredRegister.create(WitcheryRitualRegistry.INSTANCE.getRITUAL_REGISTRY_KEY(), "tfm");

    public static final DeferredHolder<Ritual, TFMPreserveFoodRitual> PRESERVE_FOOD = RITUALS.register("preserve_food", TFMPreserveFoodRitual::new);

    public static void register(IEventBus modEventBus) {
        System.out.println("[TFM-DEBUG] Registering TFM Witchery rituals to: " + WitcheryRitualRegistry.INSTANCE.getRITUAL_REGISTRY_KEY());
        RITUALS.register(modEventBus);
        System.out.println("[TFM-DEBUG] PRESERVE_FOOD holder created, id will be: " + PRESERVE_FOOD.getId());
    }
}
