package com.terrafirmamagica.compat.emi;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;

@EmiEntrypoint
public class TFMEmiPlugin implements EmiPlugin {

    public static final EmiRecipeCategory MOB_SPAWN_INFO = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath("tfm", "mob_spawn_info"),
            EmiStack.of(Items.ZOMBIE_SPAWN_EGG));

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(MOB_SPAWN_INFO);
        registry.addWorkstation(MOB_SPAWN_INFO, EmiStack.of(Items.ZOMBIE_SPAWN_EGG));

        for (MobSpawnData data : MobSpawnData.ALL) {
            // Fallback so there is no crash if the mod isn't loaded
            if (data.entityType() == EntityType.PIG && !data.entityId().getPath().equals("pig")) {
                continue;
            }
            registry.addRecipe(new MobSpawnInfoRecipe(data));
        }
    }
}
