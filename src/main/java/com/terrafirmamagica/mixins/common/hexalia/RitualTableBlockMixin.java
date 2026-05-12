package com.terrafirmamagica.mixins.common.hexalia;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.astralya.hexalia.block.custom.RitualBrazierBlock;
import net.astralya.hexalia.block.custom.RitualTableBlock;
import net.astralya.hexalia.block.entity.custom.RitualBrazierBlockEntity;
import net.astralya.hexalia.block.entity.custom.RitualTableBlockEntity;
import net.astralya.hexalia.recipe.RitualTableRecipe;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blocks.crop.CropBlock;
import net.dries007.tfc.common.blocks.crop.DoubleCropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = RitualTableBlock.class, remap = false)
public class RitualTableBlockMixin {

    private static final Method METHOD_FIND_MATCH;

    static {
        try {
            METHOD_FIND_MATCH = RitualTableBlock.class.getDeclaredMethod("findMatch",
                    Level.class, ItemStack.class, List.class, RitualTableBlockEntity.class);
            METHOD_FIND_MATCH.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("[TFM] RitualTableBlockMixin method not found", e);
        }
    }

    // Custom TFC values lower than base mod because TFC crops are much slower
    private static final int TFM_REQUIRED_CROPS = 4;
    private static final int TFM_CROP_RADIUS = 12;

    @Inject(method = "findFullyGrownCrops", at = @At("HEAD"), cancellable = true)
    private void tfm$findFullyGrownCrops(Level level, BlockPos center, int required, int radius,
            CallbackInfoReturnable<List<BlockPos>> cir) {
        List<BlockPos> found = new ArrayList<>();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                BlockPos p = center.offset(dx, 0, dz);
                BlockState s = level.getBlockState(p);

                if (s.getBlock() instanceof net.minecraft.world.level.block.CropBlock crop && crop.isMaxAge(s)) {
                    found.add(p);
                } else if (s.getBlock() instanceof CropBlock) {
                    if (level.getBlockEntity(p) instanceof CropBlockEntity cropBE && cropBE.getGrowth() >= 1.0f) {
                        found.add(p);
                    }
                }

                if (found.size() >= required) {
                    cir.setReturnValue(found);
                    return;
                }
            }
        }

        cir.setReturnValue(found);
    }

    @Inject(method = "tryStartRitual", at = @At("HEAD"), cancellable = true)
    private void tfm$tryStartRitual(Level level, BlockPos pos, Player player,
            RitualTableBlockEntity tableBE,
            CallbackInfoReturnable<Boolean> cir) {
        try {
            ItemStack tableItem = tableBE.getItem(0);
            if (tableItem.isEmpty()) {
                tfm$fail(level, pos, player, "message.hexalia.ritual.missing_ingredients");
                cir.setReturnValue(true);
                return;
            }

            BlockPos[] offsets = { pos.north(2), pos.south(2), pos.east(2), pos.west(2) };
            List<RitualBrazierBlockEntity> filled = new ArrayList<>();
            for (BlockPos bp : offsets) {
                if (level.getBlockEntity(bp) instanceof RitualBrazierBlockEntity b && !b.getStoredItem().isEmpty()) {
                    filled.add(b);
                }
            }

            Object matchObj = METHOD_FIND_MATCH.invoke(this, level, tableItem, filled, tableBE);
            if (matchObj == null) {
                tfm$fail(level, pos, player, "message.hexalia.ritual.wrong_recipe");
                cir.setReturnValue(true);
                return;
            }

            Class<?> matchClass = matchObj.getClass();
            Field recipeField = matchClass.getDeclaredField("recipe");
            Field usedBraziersField = matchClass.getDeclaredField("usedBraziers");
            recipeField.setAccessible(true);
            usedBraziersField.setAccessible(true);

            @SuppressWarnings("unchecked")
            List<RitualBrazierBlockEntity> usedBraziers = (List<RitualBrazierBlockEntity>) usedBraziersField.get(matchObj);

            for (RitualBrazierBlockEntity b : usedBraziers) {
                BlockState bs = level.getBlockState(b.getBlockPos());
                if (!bs.hasProperty(RitualBrazierBlock.SALTED) || !bs.getValue(RitualBrazierBlock.SALTED)) {
                    tfm$fail(level, pos, player, "message.hexalia.ritual.missing_salt");
                    cir.setReturnValue(true);
                    return;
                }
            }

            List<BlockPos> grownCrops = tfm$findCrops(level, pos, TFM_REQUIRED_CROPS, TFM_CROP_RADIUS);
            if (grownCrops.size() < TFM_REQUIRED_CROPS) {
                tfm$fail(level, pos, player, "message.hexalia.ritual.invalid_crops");
                cir.setReturnValue(true);
                return;
            }

            RitualTableRecipe recipe = (RitualTableRecipe) recipeField.get(matchObj);
            int duration = usedBraziers.size() * 40;
            tableBE.startTransformation(recipe.getResultItem(level.registryAccess()).copy(), duration, usedBraziers);
            tableBE.setGrownCropPositions(grownCrops);

            level.playSound(null, pos, SoundEvents.CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundSource.BLOCKS, 0.8f, 0.5f);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.8f, 0.5f);

            cir.setReturnValue(true);

        } catch (Exception e) {
            throw new RuntimeException("[TFM] tfm$tryStartRitual error", e);
        }
    }

    private List<BlockPos> tfm$findCrops(Level level, BlockPos center, int required, int radius) {
        List<BlockPos> found = new ArrayList<>();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                BlockPos p = center.offset(dx, 0, dz);
                BlockState s = level.getBlockState(p);

                if (s.getBlock() instanceof net.minecraft.world.level.block.CropBlock crop && crop.isMaxAge(s)) {
                    found.add(p);
                } else if (s.getBlock() instanceof CropBlock) {
                    if (level.getBlockEntity(p) instanceof CropBlockEntity cropBE && cropBE.getGrowth() >= 1.0f) {
                        if (s.getBlock() instanceof DoubleCropBlock
                                && s.getValue(DoubleCropBlock.PART) == DoubleCropBlock.Part.TOP) {
                            continue;
                        }
                        found.add(p);
                    }
                }

                if (found.size() >= required)
                    return found;
            }
        }
        return found;
    }

    private void tfm$fail(Level level, BlockPos pos, Player player, String key) {
        if (!level.isClientSide)
            player.displayClientMessage(Component.translatable(key), true);
        level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 0.4f, 0.6f);
    }
}
