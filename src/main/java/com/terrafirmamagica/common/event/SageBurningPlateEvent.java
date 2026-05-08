package com.terrafirmamagica.common.event;

import com.terrafirmamagica.TFMCore;
import net.joefoxe.hexerei.Hexerei;
import net.joefoxe.hexerei.block.custom.SageBurningPlate;
import net.joefoxe.hexerei.config.HexConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;

// Remove when Hexerei fixes the Event that is right now disabled

@EventBusSubscriber(modid = TFMCore.MOD_ID)
public class SageBurningPlateEvent {

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onPositionCheck(MobSpawnEvent.PositionCheck e) {
        if (e.getResult() == MobSpawnEvent.PositionCheck.Result.FAIL) return;

        Level world = e.getLevel().isClientSide() ? null :
                e.getLevel() instanceof Level l ? l : null;
        if (world == null) return;
        if (e.getSpawnType() != MobSpawnType.NATURAL) return;
        if (HexConfig.SAGE_BURNING_PLATE_RANGE.get() == 0) return;
        if (!e.getEntity().getType().getCategory().equals(MobCategory.MONSTER)) return;
        if (Hexerei.sageBurningPlateTileList.isEmpty()) return;

        for (BlockPos pos : Hexerei.sageBurningPlateTileList) {
            float dist = (float) Math.sqrt(e.getEntity().distanceToSqr(pos.getCenter()));
            if (dist < HexConfig.SAGE_BURNING_PLATE_RANGE.get() + 1) {
                BlockState state = world.getBlockState(pos);
                if (state.getBlock() instanceof SageBurningPlate
                        && state.getValue(SageBurningPlate.LIT)) {
                    e.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
                    return;
                }
            }
        }
    }
}