package com.terrafirmamagica.common.ritual;

import java.util.List;

import com.terrafirmamagica.common.data.TFMFoodTraits;

import net.dries007.tfc.common.component.food.FoodCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import dev.sterner.witchery.content.block.ritual.GoldenChalkBlockEntity;
import dev.sterner.witchery.content.recipe.ritual.RitualRecipe;
import dev.sterner.witchery.core.api.Ritual;

public class TFMPreserveFoodRitual extends Ritual {

    public TFMPreserveFoodRitual() {
        super("preserve_food");
    }

    @Override
    public void onEndRitual(Level level, BlockPos pos, GoldenChalkBlockEntity blockEntity) {
        System.out.println("[TFM-DEBUG] onEndRitual ENTERED, level=" + level);
        if (!(level instanceof ServerLevel serverLevel)) {
            System.out.println("[TFM-DEBUG] level is NOT ServerLevel, aborting");
            return;
        }

        RitualRecipe recipe = blockEntity.getRitualRecipe();
        System.out.println("[TFM-DEBUG] recipe = " + recipe);
        if (recipe == null) {
            System.out.println("[TFM-DEBUG] recipe is NULL, aborting");
            return;
        }

        CompoundTag data = recipe.getRitualData();
        System.out.println("[TFM-DEBUG] ritualData = " + data);
        double radius = data.contains("radius") ? data.getDouble("radius") : 3.0;
        int maxItems = data.contains("maxItems") ? data.getInt("maxItems") : 16;
        System.out.println("[TFM-DEBUG] radius=" + radius + " maxItems=" + maxItems + " pos=" + pos);

        List<ItemEntity> items = level.getEntitiesOfClass(
                ItemEntity.class, new AABB(pos).inflate(radius));
        System.out.println("[TFM-DEBUG] found " + items.size() + " ItemEntity nearby");

        int remaining = maxItems;

        for (ItemEntity entity : items) {
            ItemStack stack = entity.getItem();
            boolean hasFood = FoodCapability.has(stack);
            boolean isRotten = hasFood && FoodCapability.isRotten(stack);
            System.out.println("[TFM-DEBUG] stack=" + stack + " hasFood=" + hasFood + " isRotten=" + isRotten);

            if (maxItems > 0 && remaining <= 0)
                break;

            if (hasFood && !isRotten) {

                if (maxItems > 0 && stack.getCount() > remaining) {
                    ItemStack preserved = stack.copyWithCount(remaining);
                    FoodCapability.applyTrait(preserved, TFMFoodTraits.MAGIC_PRESERVED);

                    stack.shrink(remaining);
                    entity.setItem(stack);

                    ItemEntity newEntity = new ItemEntity(level,
                            entity.getX(), entity.getY(), entity.getZ(), preserved);
                    level.addFreshEntity(newEntity);
                    remaining = 0;
                } else {
                    FoodCapability.applyTrait(stack, TFMFoodTraits.MAGIC_PRESERVED);
                    entity.setItem(stack);
                    remaining -= stack.getCount();
                }

                serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT,
                        entity.getX(), entity.getY(), entity.getZ(),
                        10, 0.3, 0.3, 0.3, 0.1);
            }
        }
    }
}
