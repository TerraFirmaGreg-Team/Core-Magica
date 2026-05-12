package com.terrafirmamagica.common.data;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.terrafirmamagica.TFMCore;
import com.terrafirmamagica.common.data.blocks.TFMBlocks_Wood;

import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.calendar.Calendar;
import net.dries007.tfc.util.registry.RegistryWood;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;

public enum TFMWood implements RegistryWood {
    HEX_MAHOGANY(ResourceLocation.fromNamespaceAndPath("hexerei", "block/mahogany_planks"),
            ResourceLocation.fromNamespaceAndPath("hexerei", "block/mahogany_log"),
            ResourceLocation.fromNamespaceAndPath("hexerei", "block/stripped_mahogany_log"),
            MapColor.WOOD, 1f);

    public static final TFMWood[] VALUES = values();

    public final boolean generateWood;
    public final String serializedName;
    public final MapColor woodColor;
    public final MapColor barkColor;
    @Nullable
    public final TreeGrower tree;
    public final int daysToGrow;
    public final BlockSetType blockSet;
    public final WoodType woodType;
    public final int autumnIndex;
    public final ResourceLocation plankTexture;
    public final ResourceLocation logTexture;
    public final ResourceLocation strippedLogTexture;
    public final float saplingDropRate;

    TFMWood(MapColor woodColor, MapColor barkColor, int daysToGrow, float saplingDropRate) {
        this.serializedName = this.name().toLowerCase(Locale.ROOT);
        this.woodColor = woodColor;
        this.barkColor = barkColor;
        this.autumnIndex = 0;
        this.tree = new TreeGrower(TFMCore.id(this.serializedName).toString(),
                Optional.empty(),
                Optional.of(ResourceKey.create(Registries.CONFIGURED_FEATURE, TFMCore.id("tree/" + this.serializedName))),
                Optional.empty());
        this.daysToGrow = daysToGrow;
        this.blockSet = new BlockSetType(serializedName);
        this.woodType = new WoodType(TFMCore.id(serializedName).toString(), blockSet);
        this.saplingDropRate = saplingDropRate;
        this.generateWood = true;
        this.plankTexture = TFMCore.id("block/wood/planks/" + serializedName);
        this.logTexture = TFMCore.id("block/wood/log/" + serializedName);
        this.strippedLogTexture = TFMCore.id("block/wood/stripped_log/" + serializedName);
    }

    TFMWood(ResourceLocation plank, ResourceLocation log, ResourceLocation stripped_log, MapColor mapColor, float saplingDropRate) {
        this.serializedName = this.name().toLowerCase(Locale.ROOT);
        this.woodColor = mapColor;
        this.barkColor = mapColor;
        this.autumnIndex = 0;
        this.tree = new TreeGrower(TFMCore.id(this.serializedName).toString(),
                Optional.empty(),
                Optional.of(ResourceKey.create(Registries.CONFIGURED_FEATURE, TFMCore.id("tree/" + this.serializedName))),
                Optional.empty());
        ;
        this.daysToGrow = 0;
        this.blockSet = new BlockSetType(serializedName);
        this.woodType = new WoodType(TFMCore.id(serializedName).toString(), blockSet);
        this.saplingDropRate = saplingDropRate;
        this.generateWood = false;
        this.plankTexture = plank;
        this.logTexture = log;
        this.strippedLogTexture = stripped_log;
    }

    @Override
    public String getSerializedName() {
        return serializedName;
    }

    @Override
    public MapColor woodColor() {
        return woodColor;
    }

    @Override
    public MapColor barkColor() {
        return barkColor;
    }

    @Override
    public Supplier<Block> getBlock(Wood.BlockType type) {
        return () -> TFMBlocks_Wood.WOODS.get(this).get(type).get();
    }

    @Override
    public BlockSetType getBlockSet() {
        return blockSet;
    }

    @Override
    public WoodType getVanillaWoodType() {
        return woodType;
    }

    public TreeGrower tree() {
        return tree;
    }

    public Supplier<Integer> ticksToGrow() {
        return () -> daysToGrow() * Calendar.CALENDAR_TICKS_IN_DAY;
    }

    public int daysToGrow() {
        return defaultDaysToGrow();
    }

    @Override
    public int autumnIndex() {
        return autumnIndex;
    }

    public float getSaplingDropRate() {
        return saplingDropRate;
    }

    public int defaultDaysToGrow() {
        return daysToGrow;
    }

    public static void registerBlockSetTypes() {
        for (TFMWood wood : VALUES) {
            BlockSetType.register(wood.blockSet);
            WoodType.register(wood.woodType);
        }
    }
}
