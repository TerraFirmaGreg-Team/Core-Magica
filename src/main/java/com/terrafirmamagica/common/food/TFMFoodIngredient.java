package com.terrafirmamagica.common.food;

import com.terrafirmamagica.TFMCore;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class TFMFoodIngredient {

    public static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.INGREDIENT_TYPES, TFMCore.MOD_ID);

    public static final DeferredHolder<IngredientType<?>, IngredientType<FreshIngredient>> FRESH =
            INGREDIENT_TYPES.register("fresh", () -> new IngredientType<>(FreshIngredient.CODEC));

    public static void register(IEventBus eventBus) {
        INGREDIENT_TYPES.register(eventBus);
    }
}

