package com.terrafirmamagica.compat.hexalia;

import com.terrafirmamagica.TFMCore;
import net.astralya.hexalia.entity.ModEntities;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class ModSpawnPlacements {

    public static void register(RegisterSpawnPlacementsEvent event) {
        TFMCore.LOGGER.info("Registering spawn placements for Hexalia mobs");
        event.register(
                ModEntities.SILK_MOTH_ENTITY.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mob::checkMobSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        TFMCore.LOGGER.info("Registered silk_moth spawn placement: {}", ModEntities.SILK_MOTH_ENTITY.get());
        event.register(
                ModEntities.CACOFEY_ENTITY.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mob::checkMobSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        TFMCore.LOGGER.info("Registered cacofey spawn placement: {}", ModEntities.CACOFEY_ENTITY.get());
    }
}