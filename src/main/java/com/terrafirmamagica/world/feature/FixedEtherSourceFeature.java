package com.terrafirmamagica.world.feature;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.shapes.Shapes;

import it.mralxart.etheria.blocks.EtherSource;
import it.mralxart.etheria.registry.BlockRegistry;

public class FixedEtherSourceFeature extends Feature<NoneFeatureConfiguration> {
    private static final BlockState ETHER_SOURCE = ((EtherSource) BlockRegistry.ETHER_SOURCE.get()).defaultBlockState();
    private static final BlockState BLACKSTONE = Blocks.BLACKSTONE.defaultBlockState();

    public FixedEtherSourceFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        LevelAccessor world = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        BlockPos surfacePos = findSurface(world, origin);

        if (surfacePos == null) {
            return false;
        }

        world.setBlock(surfacePos, ETHER_SOURCE, 3);
        generateBlackstone(world, surfacePos.below(), random);
        return true;
    }

    private BlockPos findSurface(LevelAccessor world, BlockPos pos) {
        for (int y = world.getMaxBuildHeight(); y > world.getMinBuildHeight(); --y) {
            BlockPos checkPos = pos.atY(y);
            BlockState stateAt = world.getBlockState(checkPos);
            BlockState stateBelow = world.getBlockState(checkPos.below());

            if (stateAt.isAir()
                    && !stateBelow.isAir()
                    && stateBelow.getCollisionShape(world, checkPos.below()) != Shapes.empty()
                    && !stateBelow.getFluidState().is(FluidTags.WATER)
                    && !stateBelow.getFluidState().is(FluidTags.LAVA)
                    && !stateBelow.is(BlockTags.LEAVES)
                    && !stateBelow.is(BlockTags.LOGS)) {

                return checkPos;
            }
        }

        return null;
    }

    private void generateBlackstone(LevelAccessor world, BlockPos origin, RandomSource random) {
        int baseRadius = 2 + random.nextInt(3);
        int depth = 2 + random.nextInt(2);
        world.setBlock(origin, BLACKSTONE, 3);

        for (int dy = 0; dy <= depth; ++dy) {
            int currentRadius = baseRadius - dy;
            if (currentRadius <= 0)
                break;
            for (int dx = -currentRadius; dx <= currentRadius; ++dx) {
                for (int dz = -currentRadius; dz <= currentRadius; ++dz) {
                    BlockPos targetPos = origin.offset(dx, -dy, dz);
                    if (dx * dx + dz * dz <= currentRadius * currentRadius && random.nextFloat() < 0.5F) {
                        world.setBlock(targetPos, BLACKSTONE, 3);
                    }
                }
            }
        }
    }
}
