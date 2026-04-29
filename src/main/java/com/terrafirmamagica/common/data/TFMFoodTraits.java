package com.terrafirmamagica.common.data;

import com.terrafirmamagica.TFMCore;
import net.dries007.tfc.common.component.food.FoodTrait;
import net.dries007.tfc.common.component.food.FoodTraits;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TFMFoodTraits {

    public static final DeferredRegister<FoodTrait> TRAITS =
            DeferredRegister.create(FoodTraits.KEY, TFMCore.MOD_ID);

    public static final DeferredHolder<FoodTrait, FoodTrait> MAGIC_PRESERVED =
            TRAITS.register("preserved", () -> new FoodTrait(() -> 0.05, "tfm.tooltip.food_trait.magic_preserved"));

    public static final DeferredHolder<FoodTrait, FoodTrait> ARCANE_SHELVED =
            TRAITS.register("arcane_shelved", () -> new FoodTrait(() -> 0.1, "tfm.tooltip.food_trait.arcane_shelved"));

    public static void register(IEventBus eventBus) {
        TRAITS.register(eventBus);
    }
}