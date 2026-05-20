package com.terrafirmamagica.common.data;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.world.level.block.Block;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class TFMBlockEntities {
    public static void init() {
    }

    public static final Map<Supplier<?>, Set<Block>> beModification = new Object2ObjectOpenHashMap<>();

    public static void addValidBEBlock(Supplier<?> type, Block block) {
        beModification.computeIfAbsent(type, t -> new HashSet<>());
        beModification.get(type).add(block);
    }
}
