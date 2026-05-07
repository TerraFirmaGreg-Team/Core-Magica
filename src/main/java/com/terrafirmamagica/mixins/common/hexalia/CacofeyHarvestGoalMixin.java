package com.terrafirmamagica.mixins.common.hexalia;

import net.astralya.hexalia.gameplay.cacofey.ai.CacofeyHarvestGoal;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blocks.crop.CropBlock;
import net.dries007.tfc.common.blocks.crop.DoubleCropBlock;
import net.dries007.tfc.util.calendar.Calendars;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(value = CacofeyHarvestGoal.class, remap = false)
public abstract class CacofeyHarvestGoalMixin {

    @Unique
    private static final Field TFM$FIELD_CACOFEY;

    @Unique
    private static final Field TFM$FIELD_CROP_POS;

    static {
        try {
            TFM$FIELD_CACOFEY = CacofeyHarvestGoal.class.getDeclaredField("cacofey");
            TFM$FIELD_CROP_POS = CacofeyHarvestGoal.class.getDeclaredField("cropPos");
            TFM$FIELD_CACOFEY.setAccessible(true);
            TFM$FIELD_CROP_POS.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("[TFM] CacofeyHarvestGoalMixin field not found", e);
        }
    }

    @Inject(method = "tickHarvest", at = @At("TAIL"), remap = false)
    private void tfm$resetTFCCrop(CallbackInfo ci) {
        try {
            net.minecraft.world.entity.TamableAnimal cacofey =
                    (net.minecraft.world.entity.TamableAnimal) TFM$FIELD_CACOFEY.get(this);
            BlockPos cropPos = (BlockPos) TFM$FIELD_CROP_POS.get(this);

            if (!(cacofey.level() instanceof ServerLevel level)) return;

            BlockState state = level.getBlockState(cropPos);

            if (state.getBlock() instanceof DoubleCropBlock
                    && state.getValue(DoubleCropBlock.PART) == DoubleCropBlock.Part.TOP) {
                return;
            }

            if (!(level.getBlockEntity(cropPos) instanceof CropBlockEntity cropBE)) return;

            if (state.getBlock() instanceof DoubleCropBlock) {
                BlockPos above = cropPos.above();
                BlockState aboveState = level.getBlockState(above);
                if (aboveState.getBlock() instanceof DoubleCropBlock
                        && aboveState.getValue(DoubleCropBlock.PART) == DoubleCropBlock.Part.TOP) {
                    level.setBlock(above, Blocks.AIR.defaultBlockState(), 2 | 16);
                }
            }

            cropBE.setGrowth(0.0f);
            cropBE.setLastGrowthTick(Calendars.SERVER.getTicks());
            cropBE.setChanged();

            if (state.getBlock() instanceof CropBlock cropBlock) {
                level.setBlock(cropPos, state.setValue(cropBlock.getAgeProperty(), 0), 2);
            }

        } catch (IllegalAccessException e) {
            throw new RuntimeException("[TFM] CacofeyHarvestGoalMixin ERROR", e);
        }
    }
}