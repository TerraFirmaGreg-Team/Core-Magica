package com.terrafirmamagica.common.eidolon;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import alexthw.eidolon_repraised.api.research.Research;
import alexthw.eidolon_repraised.api.spells.SignSequence;
import alexthw.eidolon_repraised.api.spells.Spell;
import alexthw.eidolon_repraised.registries.Researches;
import alexthw.eidolon_repraised.registries.Signs;
import alexthw.eidolon_repraised.registries.Spells;

public class TFMEidolonRegistry {

    public static Spell TRANSFORM_SPELL;
    public static Research TRANSFORM_RESEARCH;

    public static void init() {

        EntityType<?> henchman = BuiltInRegistries.ENTITY_TYPE
                .getOptional(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "krampus_henchman"))
                .orElseThrow(() -> new IllegalStateException("born_in_chaos_v1:krampus_henchman not found"));

        EntityType<?> krampus = BuiltInRegistries.ENTITY_TYPE
                .getOptional(ResourceLocation.fromNamespaceAndPath("born_in_chaos_v1", "krampus"))
                .orElseThrow(() -> new IllegalStateException("born_in_chaos_v1:krampus not found"));

        TRANSFORM_SPELL = Spells.registerWithFallback(new TransformSpell(
                ResourceLocation.fromNamespaceAndPath("tfm", "transform"),
                350,
                henchman,
                krampus));
        TRANSFORM_SPELL.setSigns(new SignSequence(
                Signs.WINTER_SIGN,
                Signs.MAGIC_SIGN,
                Signs.DEATH_SIGN,
                Signs.MIND_SIGN,
                Signs.WICKED_SIGN));

        TRANSFORM_RESEARCH = Researches.register(
                new Research(ResourceLocation.fromNamespaceAndPath("tfm", "transform_research"), 5) {
                },
                henchman);

    }
}
