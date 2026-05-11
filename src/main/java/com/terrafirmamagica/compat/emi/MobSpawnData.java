package com.terrafirmamagica.compat.emi;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.SpawnEggItem;

import javax.annotation.Nullable;
import java.util.List;

public record MobSpawnData(
        ResourceLocation entityId,
        EntityType<?> entityType,
        String mod,
        SpawnType spawnType,
        List<String> seasons,
        @Nullable Integer minTemperature,
        @Nullable Integer maxTemperature,
        @Nullable Integer minGroundwater,
        @Nullable Integer maxGroundwater,
        @Nullable Integer minForest,
        @Nullable Integer maxForest,
        @Nullable Integer minHeight,
        @Nullable Integer maxHeight,
        @Nullable String biomeTag,
        @Nullable String biomeTagLabel,
        DayTime dayTime,
        boolean seeSky,
        boolean caveVariant
) {
    public enum SpawnType {
        SURFACE, CAVE, BOTH, WATER, SHORE
    }

    public enum DayTime {
        NIGHT,   // 13000-23000
        DAY,     // 0-12000
        ALL
    }

    @Nullable
    public SpawnEggItem getSpawnEgg() {
        return SpawnEggItem.byId(entityType);
    }

    // All mobs are manually saved here
    public static final List<MobSpawnData> ALL = List.of(

            // Vanilla
            new MobSpawnData(ResourceLocation.withDefaultNamespace("zombie"),
                    EntityType.ZOMBIE, "Vanilla", SpawnType.BOTH,
                    List.of("all"), null, null, null, null, 2, null, 70, 200,
                    null, null, DayTime.NIGHT, true, true),

            new MobSpawnData(ResourceLocation.withDefaultNamespace("skeleton"),
                    EntityType.SKELETON, "Vanilla", SpawnType.BOTH,
                    List.of("all"), -10, 10, null, null, null, 1, 65, 200,
                    null, null, DayTime.NIGHT, true, true),

            new MobSpawnData(ResourceLocation.withDefaultNamespace("creeper"),
                    EntityType.CREEPER, "Vanilla", SpawnType.SURFACE,
                    List.of("all"), null, null, null, null, null, null, 70, 200,
                    null, null, DayTime.NIGHT, true, false),

            new MobSpawnData(ResourceLocation.withDefaultNamespace("witch"),
                    EntityType.WITCH, "Vanilla", SpawnType.CAVE,
                    List.of("all"), null, null, null, null, 4, null, 30, 200,
                    null, null, DayTime.ALL, false, false),

            new MobSpawnData(ResourceLocation.withDefaultNamespace("spider"),
                    EntityType.SPIDER, "Vanilla", SpawnType.BOTH,
                    List.of("all"), null, null, null, null, null, null, 70, 200,
                    null, null, DayTime.NIGHT, true, true),

            // Mowzie's Mobs
            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("mowziesmobs", "foliaath"),
                    getType("mowziesmobs:foliaath"), "Mowzie's Mobs", SpawnType.SURFACE,
                    List.of("spring", "summer"), 15, null, 300, null, 3, null, 70, 220,
                    null, null, DayTime.ALL, false, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("mowziesmobs", "elokosa_howler"),
                    getType("mowziesmobs:elokosa_howler"), "Mowzie's Mobs", SpawnType.SURFACE,
                    List.of("spring", "summer", "fall"), 22, null, 300, null, 4, null, 70, 220,
                    null, null, DayTime.ALL, false, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("mowziesmobs", "umvuthana_raptor"),
                    getType("mowziesmobs:umvuthana_raptor"), "Mowzie's Mobs", SpawnType.SURFACE,
                    List.of("all"), 15, null, 0, 100, null, 1, 70, 220,
                    null, null, DayTime.ALL, false, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("mowziesmobs", "naga"),
                    getType("mowziesmobs:naga"), "Mowzie's Mobs", SpawnType.SURFACE,
                    List.of("all"), null, -5, null, null, null, null, 100, 220,
                    "tfm:mowzie/naga_biomes", "high_mountains", DayTime.ALL, false, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("mowziesmobs", "lantern"),
                    getType("mowziesmobs:lantern"), "Mowzie's Mobs", SpawnType.SURFACE,
                    List.of("fall", "winter"), 5, 20, 150, null, 3, null, 70, 200,
                    null, null, DayTime.ALL, false, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("mowziesmobs", "grottol"),
                    getType("mowziesmobs:grottol"), "Mowzie's Mobs", SpawnType.CAVE,
                    List.of("all"), null, null, null, 100, null, null, null, 30,
                    null, null, DayTime.ALL, false, false),

            // Iron's Spellbooks
            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "necromancer"),
                    getType("irons_spellbooks:necromancer"), "Iron's Spellbooks", SpawnType.SURFACE,
                    List.of("all"), null, -15, null, null, null, null, 70, 200,
                    null, null, DayTime.NIGHT, true, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "cultist"),
                    getType("irons_spellbooks:cultist"), "Iron's Spellbooks", SpawnType.SURFACE,
                    List.of("summer"), null, null, null, null, null, null, 70, 200,
                    "tfm:irons/cultist_biomes", "karst_cenotes", DayTime.NIGHT, true, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "priest"),
                    getType("irons_spellbooks:priest"), "Iron's Spellbooks", SpawnType.SURFACE,
                    List.of("spring"), null, null, null, null, null, null, 60, 200,
                    "tfm:irons/priest_biomes", "shores_beaches", DayTime.ALL, false, false),

            // Hexalia
            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("hexalia", "cacofey"),
                    getType("hexalia:cacofey"), "Hexalia", SpawnType.SURFACE,
                    List.of("winter"), null, -5, null, null, 3, null, 70, 200,
                    null, null, DayTime.ALL, false, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("hexalia", "silk_moth"),
                    getType("hexalia:silk_moth"), "Hexalia", SpawnType.SURFACE,
                    List.of("spring", "summer", "fall"), 5, 20, null, null, 2, null, 70, 200,
                    null, null, DayTime.ALL, false, false),

            // Eidolon
            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("eidolon_repraised", "wraith"),
                    getType("eidolon_repraised:wraith"), "Eidolon", SpawnType.SURFACE,
                    List.of("all"), null, -10, null, null, null, null, 70, 200,
                    null, null, DayTime.NIGHT, true, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("eidolon_repraised", "zombie_brute"),
                    getType("eidolon_repraised:zombie_brute"), "Eidolon", SpawnType.SURFACE,
                    List.of("all"), null, null, 300, null, 2, null, 70, 200,
                    null, null, DayTime.NIGHT, true, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("eidolon_repraised", "slimy_slug"),
                    getType("eidolon_repraised:slimy_slug"), "Eidolon", SpawnType.SURFACE,
                    List.of("all"), null, null, 400, null, null, null, 70, 200,
                    null, null, DayTime.ALL, false, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("eidolon_repraised", "raven"),
                    getType("eidolon_repraised:raven"), "Eidolon", SpawnType.SURFACE,
                    List.of("spring", "summer", "fall"), 5, 18, null, null, 2, null, 70, 200,
                    null, null, DayTime.DAY, false, false),

            // Hexerei
            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("hexerei", "crow"),
                    getType("hexerei:crow"), "Hexerei", SpawnType.SURFACE,
                    List.of("spring", "summer", "fall"), 0, 18, null, null, 2, null, 70, 200,
                    null, null, DayTime.DAY, false, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("hexerei", "owl"),
                    getType("hexerei:owl"), "Hexerei", SpawnType.SURFACE,
                    List.of("fall", "winter"), 0, 15, null, null, 3, null, 70, 200,
                    null, null, DayTime.NIGHT, false, false),

            // Born in Chaos
            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "decaying_zombie"),
                    getType("born_in_chaos_v1:decaying_zombie"), "Born in Chaos", SpawnType.BOTH,
                    List.of("fall", "winter"), null, null, 100, null, null, null, 70, 200,
                    null, null, DayTime.NIGHT, true, true),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "decrepit_skeleton"),
                    getType("born_in_chaos_v1:decrepit_skeleton"), "Born in Chaos", SpawnType.BOTH,
                    List.of("winter"), null, 5, null, 200, null, null, 70, 200,
                    null, null, DayTime.NIGHT, true, true),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "zombie_bruiser"),
                    getType("born_in_chaos_v1:zombie_bruiser"), "Born in Chaos", SpawnType.BOTH,
                    List.of("spring", "fall"), 5, 20, 150, null, null, null, 70, 200,
                    null, null, DayTime.NIGHT, true, true),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "zombie_clown"),
                    getType("born_in_chaos_v1:zombie_clown"), "Born in Chaos", SpawnType.BOTH,
                    List.of("all"), null, null, 50, null, null, null, 70, 200,
                    null, null, DayTime.NIGHT, true, true),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "zombie_lumberjack"),
                    getType("born_in_chaos_v1:zombie_lumberjack"), "Born in Chaos", SpawnType.SURFACE,
                    List.of("spring", "summer"), 10, null, 200, null, 3, null, 70, 200,
                    null, null, DayTime.NIGHT, true, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "zombie_fisherman"),
                    getType("born_in_chaos_v1:zombie_fisherman"), "Born in Chaos", SpawnType.SHORE,
                    List.of("spring", "summer", "fall"), 5, null, null, null, null, null, 55, 75,
                    "tfm:bic/river_shore_biomes", "rivers_shores", DayTime.NIGHT, true, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "barrel_zombie"),
                    getType("born_in_chaos_v1:barrel_zombie"), "Born in Chaos", SpawnType.SHORE,
                    List.of("spring", "summer"), 5, null, null, null, null, null, 55, 75,
                    "tfm:bic/river_shore_biomes", "rivers_shores", DayTime.NIGHT, true, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "skeleton_thrasher"),
                    getType("born_in_chaos_v1:skeleton_thrasher"), "Born in Chaos", SpawnType.BOTH,
                    List.of("winter"), null, 5, null, 150, null, null, 70, 200,
                    null, null, DayTime.NIGHT, true, true),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "skeleton_demoman"),
                    getType("born_in_chaos_v1:skeleton_demoman"), "Born in Chaos", SpawnType.BOTH,
                    List.of("summer"), 15, null, null, 200, null, null, 70, 200,
                    null, null, DayTime.ALL, true, true),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "siamese_skeletons"),
                    getType("born_in_chaos_v1:siamese_skeletons"), "Born in Chaos", SpawnType.BOTH,
                    List.of("summer"), 15, null, 50, null, null, null, 70, 200,
                    null, null, DayTime.NIGHT, true, true),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "baby_skeleton"),
                    getType("born_in_chaos_v1:baby_skeleton"), "Born in Chaos", SpawnType.BOTH,
                    List.of("all"), null, null, null, null, null, null, 70, 200,
                    null, null, DayTime.NIGHT, true, true),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "bonescaller"),
                    getType("born_in_chaos_v1:bonescaller"), "Born in Chaos", SpawnType.BOTH,
                    List.of("fall", "winter"), null, null, null, null, null, null, 70, 200,
                    null, null, DayTime.NIGHT, true, true),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "swarmer"),
                    getType("born_in_chaos_v1:swarmer"), "Born in Chaos", SpawnType.BOTH,
                    List.of("spring", "summer"), 10, null, 150, null, null, null, 70, 200,
                    null, null, DayTime.NIGHT, true, true),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "fallen_chaos_knight"),
                    getType("born_in_chaos_v1:fallen_chaos_knight"), "Born in Chaos", SpawnType.SURFACE,
                    List.of("all"), null, null, null, null, null, null, 70, 200,
                    null, null, DayTime.NIGHT, true, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "restless_spirit"),
                    getType("born_in_chaos_v1:restless_spirit"), "Born in Chaos", SpawnType.SURFACE,
                    List.of("fall", "winter"), null, null, null, null, null, null, 70, 200,
                    null, null, DayTime.NIGHT, true, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "spiritof_chaos"),
                    getType("born_in_chaos_v1:spiritof_chaos"), "Born in Chaos", SpawnType.SURFACE,
                    List.of("fall"), null, null, null, null, null, null, 70, 200,
                    null, null, DayTime.NIGHT, true, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "nightmare_stalker"),
                    getType("born_in_chaos_v1:nightmare_stalker"), "Born in Chaos", SpawnType.SURFACE,
                    List.of("fall", "winter"), null, null, null, null, null, null, 70, 200,
                    null, null, DayTime.NIGHT, true, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "dark_vortex"),
                    getType("born_in_chaos_v1:dark_vortex"), "Born in Chaos", SpawnType.SURFACE,
                    List.of("fall", "winter"), null, null, null, null, null, null, 70, 200,
                    null, null, DayTime.NIGHT, true, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "phantom_creeper"),
                    getType("born_in_chaos_v1:phantom_creeper"), "Born in Chaos", SpawnType.SURFACE,
                    List.of("fall", "winter"), null, null, null, null, null, null, 70, 200,
                    null, null, DayTime.NIGHT, true, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "spirit_guide"),
                    getType("born_in_chaos_v1:spirit_guide"), "Born in Chaos", SpawnType.SURFACE,
                    List.of("all"), null, null, null, null, null, null, 60, 200,
                    "tfm:bic/spirit_guide_biomes", "deserts_badlands", DayTime.NIGHT, true, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "dread_hound"),
                    getType("born_in_chaos_v1:dread_hound"), "Born in Chaos", SpawnType.BOTH,
                    List.of("fall", "winter"), null, null, null, null, null, 2, 70, 200,
                    null, null, DayTime.NIGHT, true, true),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "corpse_fly"),
                    getType("born_in_chaos_v1:corpse_fly"), "Born in Chaos", SpawnType.SURFACE,
                    List.of("spring", "summer"), 10, null, 200, null, null, null, 60, 200,
                    null, null, DayTime.NIGHT, true, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "bloody_gadfly"),
                    getType("born_in_chaos_v1:bloody_gadfly"), "Born in Chaos", SpawnType.SHORE,
                    List.of("summer"), 15, null, 250, null, null, null, 55, 80,
                    "tfm:bic/river_shore_biomes", "rivers_shores", DayTime.NIGHT, true, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "corpse_fish"),
                    getType("born_in_chaos_v1:corpse_fish"), "Born in Chaos", SpawnType.WATER,
                    List.of("spring", "summer", "fall"), 5, null, null, null, null, null, null, null,
                    "tfm:bic/aquatic_biomes", "rivers_lakes_oceans", DayTime.ALL, false, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "glutton_fish"),
                    getType("born_in_chaos_v1:glutton_fish"), "Born in Chaos", SpawnType.WATER,
                    List.of("all"), 5, null, null, null, null, null, null, null,
                    "tfm:bic/ocean_biomes", "oceans", DayTime.ALL, false, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "thornshell_crab"),
                    getType("born_in_chaos_v1:thornshell_crab"), "Born in Chaos", SpawnType.SHORE,
                    List.of("spring", "summer"), 10, null, null, null, null, null, 55, 72,
                    "tfm:bic/shore_biomes", "shores_beaches", DayTime.ALL, false, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "mother_spider"),
                    getType("born_in_chaos_v1:mother_spider"), "Born in Chaos", SpawnType.BOTH,
                    List.of("spring", "summer"), 15, null, 200, null, 4, null, 70, 200,
                    null, null, DayTime.NIGHT, true, true),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "seared_spirit"),
                    getType("born_in_chaos_v1:seared_spirit"), "Born in Chaos", SpawnType.SURFACE,
                    List.of("summer"), 20, null, null, 100, null, 1, 63, 200,
                    null, null, DayTime.NIGHT, true, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "krampus_henchman"),
                    getType("born_in_chaos_v1:krampus_henchman"), "Born in Chaos", SpawnType.SURFACE,
                    List.of("fall", "winter"), null, -5, null, null, null, null, 70, 220,
                    "tfm:bic/cold_biomes", "cold_regions", DayTime.NIGHT, true, false),

            // Variants and Ventures
            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("variantsandventures", "thicket"),
                    getType("variantsandventures:thicket"), "V&V", SpawnType.SURFACE,
                    List.of("spring", "summer"), 10, null, 200, null, 3, null, 70, 200,
                    null, null, DayTime.NIGHT, true, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("variantsandventures", "murk"),
                    getType("variantsandventures:murk"), "V&V", SpawnType.SURFACE,
                    List.of("fall", "winter"), null, 10, 350, null, 2, null, 60, 200,
                    null, null, DayTime.NIGHT, true, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("variantsandventures", "verdant"),
                    getType("variantsandventures:verdant"), "V&V", SpawnType.SURFACE,
                    List.of("spring", "summer"), 15, null, 250, null, 3, null, 70, 200,
                    null, null, DayTime.NIGHT, true, false),

            new MobSpawnData(ResourceLocation.fromNamespaceAndPath("variantsandventures", "gelid"),
                    getType("variantsandventures:gelid"), "V&V", SpawnType.SURFACE,
                    List.of("fall", "winter"), null, -5, null, null, null, null, 70, 220,
                    "tfm:bic/cold_biomes", "cold_regions", DayTime.NIGHT, true, false)
    );

    // Get EntityType without crash if mod isn't loaded
    @SuppressWarnings("unchecked")
    private static <T extends Entity> EntityType<T> getType(String id) {
        ResourceLocation rl = ResourceLocation.parse(id);
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(rl);
        return type != null ? (EntityType<T>) type : (EntityType<T>) EntityType.PIG;
    }
}