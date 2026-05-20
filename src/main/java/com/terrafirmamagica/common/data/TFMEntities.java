package com.terrafirmamagica.common.data;

import com.terrafirmamagica.TFMCore;
import com.terrafirmamagica.common.entity.baldeagle.TFMBaldEagle;
import com.terrafirmamagica.common.entity.baldeagle.TFMBaldEagleRenderer;
import com.terrafirmamagica.common.entity.buffalo.TFMBuffalo;
import com.terrafirmamagica.common.entity.buffalo.TFMBuffaloRenderer;
import com.tterrag.registrate.util.entry.EntityEntry;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class TFMEntities {
    public static void init() {
    }

    public static final EntityEntry<TFMBuffalo> TFM_BUFFALO = TFMCore.REGISTRATE.entity("buffalo", TFMBuffalo::makeTFMBuffalo, MobCategory.CREATURE)
            .properties(p -> p.sized(1.35F, 1.95F).eyeHeight(1.85F).clientTrackingRange(10))
            .loot((prov, ctx) -> prov.add(ctx, new LootTable.Builder()))
            .attributes(TFMBuffalo::createMobAttributes)
            .renderer(() -> TFMBuffaloRenderer::new)
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TFMBuffalo::spawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .register();

    public static final EntityEntry<TFMBaldEagle> TFM_BALD_EAGLE = TFMCore.REGISTRATE.entity("bald_eagle", TFMBaldEagle::new, MobCategory.CREATURE)
            .properties(p -> p.sized(0.6F, 1.0F).eyeHeight(0.6F).clientTrackingRange(8))
            .loot((prov, ctx) -> prov.add(ctx, new LootTable.Builder()))
            .attributes(TFMBaldEagle::createMobAttributes)
            .renderer(() -> TFMBaldEagleRenderer::new)
            .register();

}
