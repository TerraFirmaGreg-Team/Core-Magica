package com.terrafirmamagica.mixins.common.hexalia;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.astralya.hexalia.block.entity.custom.RitualTableBlockEntity;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blocks.crop.CropBlock;
import net.dries007.tfc.common.blocks.crop.DoubleCropBlock;
import net.dries007.tfc.util.calendar.Calendars;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("deprecation")
@Mixin(value = RitualTableBlockEntity.class, remap = false)
public abstract class RitualTableBlockEntityMixin {
    @Unique
    private static final Field FIELD_GROWN_CROPS;

    @Unique
    private static final Field FIELD_TRANSFORM_TICKS_REMAINING;

    static {
        try {
            FIELD_GROWN_CROPS = RitualTableBlockEntity.class.getDeclaredField("grownCrops");
            FIELD_TRANSFORM_TICKS_REMAINING = RitualTableBlockEntity.class.getDeclaredField("transformTicksRemaining");

            FIELD_GROWN_CROPS.setAccessible(true);
            FIELD_TRANSFORM_TICKS_REMAINING.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("[TFM] Field not found", e);
        }
    }

    @Inject(method = "setItem", at = @At("TAIL"), remap = false)
    private void tfm$onSetItem(int slot, ItemStack stack, CallbackInfo ci) {
        try {
            if (slot != 0 || stack.isEmpty())
                return;
            if ((int) FIELD_TRANSFORM_TICKS_REMAINING.get(this) != 0)
                return;

            @SuppressWarnings("unchecked")
            List<BlockPos> grownCrops = (List<BlockPos>) FIELD_GROWN_CROPS.get(this);
            if (grownCrops == null || grownCrops.isEmpty())
                return;

            RitualTableBlockEntity be = (RitualTableBlockEntity) (Object) this;
            Level level = be.getLevel();
            if (level == null || level.isClientSide())
                return;

            for (BlockPos cropPos : grownCrops) {
                BlockState state = level.getBlockState(cropPos);
                if (state.getBlock() instanceof CropBlock) {
                    tfm$resetTFCCrop(level, cropPos, state);
                }
            }

            // Clear grownCrops to prevent re-triggering when placing an item on the altar
            FIELD_GROWN_CROPS.set(this, new ArrayList<>());

        } catch (IllegalAccessException e) {
            throw new RuntimeException("[TFM] tfm$onSetItem error", e);
        }
    }

    @Unique
    private static void tfm$resetTFCCrop(Level level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof DoubleCropBlock
                && state.getValue(DoubleCropBlock.PART) == DoubleCropBlock.Part.TOP) {
            return;
        }

        if (!(level.getBlockEntity(pos) instanceof CropBlockEntity cropBE))
            return;

        // Remove top block first to prevent it from dropping when the bottom is reset
        if (state.getBlock() instanceof DoubleCropBlock) {
            BlockPos above = pos.above();
            BlockState aboveState = level.getBlockState(above);
            if (aboveState.getBlock() instanceof DoubleCropBlock
                    && aboveState.getValue(DoubleCropBlock.PART) == DoubleCropBlock.Part.TOP) {
                level.setBlock(above, Blocks.AIR.defaultBlockState(), 2 | 16);
            }
        }

        cropBE.setGrowth(0.0f);
        cropBE.setLastGrowthTick(Calendars.SERVER.getTicks());
        cropBE.setLastCalendarUpdateTick(Calendars.SERVER.getTicks());
        cropBE.setChanged();

        if (state.getBlock() instanceof CropBlock cropBlock) {
            level.setBlock(pos, state.setValue(cropBlock.getAgeProperty(), 0), 2);
        }
    }
}
