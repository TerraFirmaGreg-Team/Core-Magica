package com.terrafirmamagica.common.rite;

import java.util.List;

import com.terrafirmamagica.common.data.TFMFoodTraits;

import net.dries007.tfc.common.component.food.FoodCapability;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.Rite;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class PreserveFoodRite extends Rite {

    private final double radius;
    private final int maxItems;
    private final int duration; // ticks

    public PreserveFoodRite(BaseRiteParams baseParams, RiteParams params, double radius, int maxItems, int duration) {
        super(baseParams, params);
        this.radius = radius;
        this.maxItems = maxItems;
        this.duration = duration;
    }

    @Override
    protected boolean onStart(RiteParams params) {
        level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE,
                SoundSource.MASTER, 1.0f, 0.5f);
        return true;
    }

    @Override
    protected boolean onTick(RiteParams params) {
        int ticks = params.ticks();

        double angle = ticks * 0.3;
        double height = (ticks / (double) duration) * 2.0;
        double spiralX = pos.getX() + 0.5 + Math.cos(angle) * 1.2;
        double spiralZ = pos.getZ() + 0.5 + Math.sin(angle) * 1.2;

        level.sendParticles(ParticleTypes.ENCHANT,
                spiralX, pos.getY() + height, spiralZ,
                3, 0.1, 0.1, 0.1, 0.02);

        if (ticks == duration / 2) {
            randomParticles(ParticleTypes.WITCH);
            level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE,
                    SoundSource.MASTER, 0.5f, 1.5f);
        }

        if (ticks >= duration - 1) {
            applyPreservation();
            return false;
        }

        return true;
    }

    @Override
    protected void onStop(RiteParams params) {
        randomParticles(ParticleTypes.TOTEM_OF_UNDYING);
        level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP,
                SoundSource.MASTER, 0.8f, 1.2f);
    }

    private void applyPreservation() {
        List<ItemEntity> items = level.getEntitiesOfClass(
                ItemEntity.class,
                new AABB(pos).inflate(radius));

        int remaining = maxItems;

        for (ItemEntity entity : items) {
            if (maxItems > 0 && remaining <= 0)
                break;

            var stack = entity.getItem();
            if (FoodCapability.has(stack) && !FoodCapability.isRotten(stack)) {

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

                level.sendParticles(ParticleTypes.ENCHANTED_HIT,
                        entity.getX(), entity.getY(), entity.getZ(),
                        10, 0.3, 0.3, 0.3, 0.1);
            }
        }
    }
}
