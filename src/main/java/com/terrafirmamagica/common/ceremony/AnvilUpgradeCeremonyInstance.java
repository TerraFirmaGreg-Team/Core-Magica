package com.terrafirmamagica.common.ceremony;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

import pokefenn.totemic.api.ceremony.CeremonyEffectContext;
import pokefenn.totemic.api.ceremony.CeremonyInstance;

public enum AnvilUpgradeCeremonyInstance implements CeremonyInstance {
    INSTANCE;

    private static final int RANGE = 8;
    private static final ResourceLocation COPPER_ANVIL = ResourceLocation.parse("tfc:metal/anvil/copper");
    private static final ResourceLocation BRONZE_BLOCK = ResourceLocation.parse("tfc:metal/block/bronze");
    private static final ResourceLocation BRONZE_ANVIL = ResourceLocation.parse("tfc:metal/anvil/bronze");

    @Override
    public void effect(Level level, BlockPos pos, CeremonyEffectContext context) {
        if (level.isClientSide())
            return;

        List<BlockPos> copperAnvils = findBlocks(level, pos, RANGE,
                s -> s.getBlock() == BuiltInRegistries.BLOCK.get(COPPER_ANVIL));
        List<BlockPos> bronzeBlocks = findBlocks(level, pos, RANGE,
                s -> s.getBlock() == BuiltInRegistries.BLOCK.get(BRONZE_BLOCK));

        if (copperAnvils.isEmpty() || bronzeBlocks.isEmpty())
            return;

        BlockPos anvilPos = copperAnvils.get(0);
        BlockPos bronzePos = bronzeBlocks.get(0);

        Block bronzeAnvil = BuiltInRegistries.BLOCK.get(BRONZE_ANVIL);
        if (bronzeAnvil == null)
            return;

        BlockState copperState = level.getBlockState(anvilPos);
        BlockState bronzeState = bronzeAnvil.defaultBlockState();

        if (copperState.hasProperty(HorizontalDirectionalBlock.FACING)
                && bronzeState.hasProperty(HorizontalDirectionalBlock.FACING)) {
            bronzeState = bronzeState.setValue(
                    HorizontalDirectionalBlock.FACING,
                    copperState.getValue(HorizontalDirectionalBlock.FACING));
        }

        level.setBlock(anvilPos, bronzeState, 3);
        level.setBlock(bronzePos, Blocks.AIR.defaultBlockState(), 3);
    }

    @Override
    public boolean canSelect(Level level, BlockPos pos, Entity initiator) {
        boolean hasAnvil = !findBlocks(level, pos, RANGE,
                s -> s.getBlock() == BuiltInRegistries.BLOCK.get(COPPER_ANVIL)).isEmpty();
        boolean hasBronzeBlock = !findBlocks(level, pos, RANGE,
                s -> s.getBlock() == BuiltInRegistries.BLOCK.get(BRONZE_BLOCK)).isEmpty();

        if (!hasAnvil || !hasBronzeBlock) {
            initiator.sendSystemMessage(
                    Component.translatable("tfm.ceremony.missing_materials"));
            return false;
        }
        return true;
    }

    private static List<BlockPos> findBlocks(Level level, BlockPos center, int radius,
            Predicate<BlockState> predicate) {
        List<BlockPos> found = new ArrayList<>();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    BlockPos p = center.offset(dx, dy, dz);
                    if (predicate.test(level.getBlockState(p))) {
                        found.add(p);
                    }
                }
            }
        }
        return found;
    }
}
