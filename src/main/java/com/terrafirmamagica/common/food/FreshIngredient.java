package com.terrafirmamagica.common.food;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;

import java.util.Arrays;
import java.util.stream.Stream;

public record FreshIngredient(Ingredient inner) implements ICustomIngredient {

    public static final MapCodec<FreshIngredient> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(FreshIngredient::inner)
    ).apply(inst, FreshIngredient::new));

    @Override
    public boolean test(ItemStack stack) {
        if (!inner.test(stack)) return false;
        if (!FoodCapability.has(stack)) return true;
        return !FoodCapability.isRotten(stack);
    }

    @Override
    public Stream<ItemStack> getItems() {
        return Arrays.stream(inner.getItems());
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return TFMFoodIngredient.FRESH.get();
    }
}