package com.terrafirmamagica.common.ceremony;

import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.plant.ITallPlant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import pokefenn.totemic.api.ceremony.CeremonyEffectContext;
import pokefenn.totemic.api.ceremony.CeremonyInstance;

import java.util.ArrayList;
import java.util.List;

public enum EvergreenSunflowersCeremonyInstance implements CeremonyInstance {
    INSTANCE;

    private static final int RANGE = 8;
    private static final TagKey<Block> GENERAL_PLANTS = TagKey.create(
            BuiltInRegistries.BLOCK.key(),
            ResourceLocation.fromNamespaceAndPath("tfm", "general_plants")
    );

    @Override
    public void effect(Level level, BlockPos pos, CeremonyEffectContext context) {
        if (level.isClientSide()) return;

        List<BlockPos> plants = findTaggedBlocks(level, pos, RANGE);
        if (plants.isEmpty()) return;

        for (BlockPos plantPos : plants) {
            BlockState state = level.getBlockState(plantPos);

            if (state.hasProperty(TFCBlockStateProperties.TALL_PLANT_PART)) {
                BlockPos above = plantPos.above();
                BlockState aboveState = level.getBlockState(above);
                if (aboveState.hasProperty(TFCBlockStateProperties.TALL_PLANT_PART)
                        && aboveState.getValue(TFCBlockStateProperties.TALL_PLANT_PART) == ITallPlant.Part.UPPER) {
                    level.setBlock(above, Blocks.AIR.defaultBlockState(), 2 | 16);
                }
            }

            level.setBlock(plantPos, Blocks.DANDELION.defaultBlockState(), 3);
        }
    }

    @Override
    public boolean canSelect(Level level, BlockPos pos, Entity initiator) {
        boolean hasPlants = !findTaggedBlocks(level, pos, RANGE).isEmpty();

        if (!hasPlants) {
            initiator.sendSystemMessage(
                    Component.translatable("tfm.ceremony.evergreen_sunflowers.missing_plants"));
            return false;
        }
        return true;
    }

    private static List<BlockPos> findTaggedBlocks(Level level, BlockPos center, int radius) {
        List<BlockPos> found = new ArrayList<>();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    BlockPos p = center.offset(dx, dy, dz);
                    BlockState state = level.getBlockState(p);
                    if (!state.is(GENERAL_PLANTS)) continue;

                    if (state.hasProperty(TFCBlockStateProperties.TALL_PLANT_PART)
                            && state.getValue(TFCBlockStateProperties.TALL_PLANT_PART) == ITallPlant.Part.UPPER) {
                        continue;
                    }

                    found.add(p);
                }
            }
        }
        return found;
    }
}