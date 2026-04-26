package com.terrafirmamagica.common.data;

import com.terrafirmamagica.mixins.common.minecraft.BlockEntityTypeAccessor;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class TFMBlockEntities {
    public static void init() {
    }

    private static final Map<Supplier<?>, Set<Block>> beModification = new Object2ObjectOpenHashMap<>();

    public static void addValidBEBlock(Supplier<?> type, Block block) {
        beModification.computeIfAbsent(type, t -> new HashSet<>());
        beModification.get(type).add(block);
    }

    public static void finaliseBEModification() {
        for (var key : beModification.keySet()) {
            var beType = (BlockEntityTypeAccessor) key.get();
            Set<Block> blocks = new HashSet<>();
            blocks.addAll(beType.tfm$getValidBlocks());
            blocks.addAll(beModification.get(key));
            beType.tfm$setValidBlocks(blocks);
        }
    }
}
