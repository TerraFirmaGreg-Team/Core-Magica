package com.terrafirmamagica.mixins.common.hexalia;

import net.astralya.hexalia.block.entity.custom.AstrylisBlockEntity;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blocks.crop.CropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(value = AstrylisBlockEntity.class, remap = false)
public class AstrylisBlockEntityMixin {

    @Inject(
            method = "applyBonemealToCropsAndSaplings",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void tfm$supportTFCCrops(ServerLevel level, BlockPos centerPos, CallbackInfo ci) {
        BlockPos.betweenClosedStream(centerPos.offset(-4, -2, -4), centerPos.offset(4, 2, 4)).forEach(pos -> {
            BlockState state = level.getBlockState(pos);

            if (level.getBlockEntity(pos) instanceof CropBlockEntity crop
                    && state.getBlock() instanceof CropBlock cropBlock) {
                float newGrowth = Math.min(1.0f, crop.getGrowth() + 0.05f); // 1.0 = Instant Maturity / 0.5 = 50% of growth / 0.1 = 10% / 0.01 = 1%
                crop.setGrowth(newGrowth);
                crop.setYield(Math.min(1.0f, crop.getYield() + 0.01f));
                crop.setChanged();
                int age = newGrowth >= 1.0f
                        ? cropBlock.getMaxAge()
                        : Mth.floor(newGrowth * cropBlock.getMaxAge());
                level.setBlockAndUpdate(pos, state.setValue(cropBlock.getAgeProperty(), age));
                level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        1, 0.2, 0.2, 0.2, 0.0);
            }
        });

        ci.cancel();

        BlockPos.betweenClosedStream(centerPos.offset(-4, -2, -4), centerPos.offset(4, 2, 4)).forEach(pos -> {
            BlockState state = level.getBlockState(pos);

            if (level.getBlockEntity(pos) instanceof CropBlockEntity) return;

            if (state.getBlock() instanceof BonemealableBlock bonemealableBlock &&
                    (state.is(BlockTags.CROPS) || state.is(BlockTags.SAPLINGS))) {
                if (bonemealableBlock.isValidBonemealTarget(level, pos, state)) {
                    bonemealableBlock.performBonemeal(level, level.random, pos, state);
                    level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            1, 0.2, 0.2, 0.2, 0.0);
                }
            }
        });
    }
}