package com.terrafirmamagica.mixins.common.hexalia;

import static net.dries007.tfc.common.items.Powder.SALT;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.astralya.hexalia.block.custom.RitualBrazierBlock;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

@Mixin(value = RitualBrazierBlock.class, remap = false)
public abstract class RitualBrazierBlockMixin {

    @Redirect(method = "onRemove", at = @At(value = "NEW", target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack redirectSaltDrop(ItemLike item) {
        return new ItemStack(TFCItems.POWDERS.get(SALT).get());
    }
}
