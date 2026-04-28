package com.terrafirmamagica.mixins.common.hexalia;

import net.astralya.hexalia.block.custom.SmallCauldronBlock;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.dries007.tfc.common.items.TFCItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SmallCauldronBlock.class, remap = false)
public class SmallCauldronBlockMixin {

    @Inject(
            method = "tryIgniteWithFlintAndSteel",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void tfm$allowTFCIgnition(ItemStack stack, BlockState state, Level level,
                                      net.minecraft.core.BlockPos pos, Player player,
                                      InteractionHand hand,
                                      CallbackInfoReturnable<ItemInteractionResult> cir) {

        boolean isTFCIgniter = stack.is(TFCItems.FLINT_AND_PYRITE.get())
                || stack.is(TFCItems.FIRESTARTER.get());

        if (!isTFCIgniter) return;

        if (state.getValue(SmallCauldronBlock.WATERLOGGED) || state.getValue(SmallCauldronBlock.LIT)) {
            cir.setReturnValue(ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION);
            return;
        }

        if (level.isClientSide) {
            cir.setReturnValue(ItemInteractionResult.SUCCESS);
            return;
        }

        level.setBlock(pos, state.setValue(SmallCauldronBlock.LIT, true), 3);
        stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
        cir.setReturnValue(ItemInteractionResult.CONSUME);
    }
}