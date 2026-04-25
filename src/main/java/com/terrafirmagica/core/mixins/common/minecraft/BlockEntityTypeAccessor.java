package com.terrafirmagica.core.mixins.common.minecraft;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin({ BlockEntityType.class })
public interface BlockEntityTypeAccessor {
    @Accessor("validBlocks")
    Set<Block> tfm$getValidBlocks();

    @Accessor("validBlocks")
    @Mutable
    void tfm$setValidBlocks(Set<Block> var1);
}
