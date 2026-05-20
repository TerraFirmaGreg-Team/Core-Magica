package com.terrafirmamagica.common.data.blocks;

import static com.eerussianguy.firmalife.common.blocks.FLBlocks.*;

import java.util.Map;

import com.eerussianguy.firmalife.common.blockentities.BarrelPressBlockEntity;
import com.eerussianguy.firmalife.common.blockentities.FLBlockEntities;
import com.eerussianguy.firmalife.common.blocks.*;
import com.google.gson.JsonObject;
import com.terrafirmamagica.TFMCore;
import com.terrafirmamagica.common.data.TFMBlockEntities;
import com.terrafirmamagica.common.data.TFMWood;
import com.terrafirmamagica.utils.ModelUtils;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.devices.BarrelBlock;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.items.BarrelBlockItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

@SuppressWarnings({ "removal", "unused" })
public class TFMBlocks_Wood {
    public static final Map<TFMWood, Map<Wood.BlockType, BlockEntry<? extends Block>>> WOODS = new Object2ObjectOpenHashMap<>();

    public static final Map<TFMWood, BlockEntry<? extends Block>> FOOD_SHELVES = new Object2ObjectOpenHashMap<>();
    public static final Map<TFMWood, BlockEntry<? extends Block>> HANGERS = new Object2ObjectOpenHashMap<>();
    public static final Map<TFMWood, BlockEntry<? extends Block>> JARBNETS = new Object2ObjectOpenHashMap<>();
    public static final Map<TFMWood, BlockEntry<? extends Block>> KEGS = new Object2ObjectOpenHashMap<>();
    public static final Map<TFMWood, BlockEntry<? extends Block>> KEG_SUBS = new Object2ObjectOpenHashMap<>();
    public static final Map<TFMWood, BlockEntry<? extends Block>> WINE_SHELVES = new Object2ObjectOpenHashMap<>();
    public static final Map<TFMWood, BlockEntry<? extends Block>> STOMPING_BARRELS = new Object2ObjectOpenHashMap<>();
    public static final Map<TFMWood, BlockEntry<? extends Block>> BARREL_PRESSES = new Object2ObjectOpenHashMap<>();

    public static void init() {
        TFMWood.registerBlockSetTypes();
        for (TFMWood value : TFMWood.VALUES) {
            registerWood(value);

            FOOD_SHELVES.put(value, foodShelf(value));
            HANGERS.put(value, hanger(value));
            JARBNETS.put(value, jarbnet(value));
            BlockEntry<? extends Block> kegSub = kegSub(value);
            KEGS.put(value, kegCore(value, kegSub));
            KEG_SUBS.put(value, kegSub);
            WINE_SHELVES.put(value, wineShelf(value));
            STOMPING_BARRELS.put(value, stompingBarrel(value));
            BARREL_PRESSES.put(value, barrelPress(value));
        }
    }

    private static void registerWood(TFMWood wood) {
        Map<Wood.BlockType, BlockEntry<? extends Block>> blocks = new Object2ObjectOpenHashMap<>();

        blocks.put(Wood.BlockType.LOG_FENCE, logFence(wood));
        blocks.put(Wood.BlockType.TOOL_RACK, toolRack(wood));
        blocks.put(Wood.BlockType.WORKBENCH, workbench(wood));
        blocks.put(Wood.BlockType.LOOM, loom(wood));
        blocks.put(Wood.BlockType.SLUICE, sluice(wood));
        blocks.put(Wood.BlockType.BARREL, barrel(wood));
        blocks.put(Wood.BlockType.LECTERN, lectern(wood));
        blocks.put(Wood.BlockType.SCRIBING_TABLE, scribingTable(wood));
        blocks.put(Wood.BlockType.SEWING_TABLE, sewingTable(wood));
        blocks.put(Wood.BlockType.SHELF, shelf(wood));
        blocks.put(Wood.BlockType.BOOKSHELF, bookshelf(wood));

        WOODS.put(wood, blocks);
    }

    // TFC
    private static BlockEntry<Block> logFence(TFMWood wood) {
        var logFenceBlock = Wood.BlockType.LOG_FENCE.create(wood).get();
        return TFMCore.REGISTRATE.block("wood/log_fence/" + wood.serializedName, p -> logFenceBlock)
                .blockstate((ctx, prov) -> {
                    prov.models().withExistingParent(ctx.getName() + "_inventory", ResourceLocation.fromNamespaceAndPath("tfc", "block/wood/log_fence/inventory"))
                            .texture("log", wood.logTexture)
                            .texture("planks", wood.plankTexture);

                    ModelFile modelSide = prov.models().withExistingParent(ctx.getName() + "_side", ResourceLocation.withDefaultNamespace("block/fence_side"))
                            .texture("texture", wood.plankTexture);

                    ModelFile modelPost = prov.models().withExistingParent(ctx.getName() + "_post", ResourceLocation.withDefaultNamespace("block/fence_post"))
                            .texture("texture", wood.logTexture);

                    prov.getMultipartBuilder(ctx.getEntry()).part().modelFile(modelPost).addModel().end()
                            .part().modelFile(modelSide).uvLock(true).addModel().condition(BlockStateProperties.NORTH, true).end()
                            .part().modelFile(modelSide).rotationY(90).uvLock(true).addModel().condition(BlockStateProperties.EAST, true).end()
                            .part().modelFile(modelSide).rotationY(180).uvLock(true).addModel().condition(BlockStateProperties.SOUTH, true).end()
                            .part().modelFile(modelSide).rotationY(270).uvLock(true).addModel().condition(BlockStateProperties.WEST, true).end();
                })
                .tag(TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("fences")))
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .item(BlockItem::new).model(ModelUtils.blockItemModel(TFMCore.id("block/wood/log_fence/" + wood.serializedName + "_inventory")))
                .tag(TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace("fences"))).build()
                .register();
    }

    private static BlockEntry<Block> toolRack(TFMWood wood) {
        var toolRackBlock = Wood.BlockType.TOOL_RACK.create(wood).get();
        return TFMCore.REGISTRATE.block("wood/tool_rack/" + wood.serializedName, p -> toolRackBlock)
                .blockstate((ctx, prov) -> {
                    ModelFile model = prov.models().withExistingParent(ctx.getName(), ResourceLocation.fromNamespaceAndPath("tfc", "block/tool_rack"))
                            .texture("texture", wood.plankTexture)
                            .texture("particle", wood.plankTexture);

                    ModelUtils.cardinalBlockInverted(prov.getVariantBuilder(ctx.getEntry()), model);
                })
                .tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("tfc", "tool_racks")))
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .onRegister(block -> {
                    TFMBlockEntities.addValidBEBlock(TFCBlockEntities.TOOL_RACK, block);
                })
                .item(BlockItem::new)
                .tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("tfc", "tool_racks"))).build()
                .register();
    }

    private static BlockEntry<Block> workbench(TFMWood wood) {
        var workbenchBlock = Wood.BlockType.WORKBENCH.create(wood).get();
        return TFMCore.REGISTRATE.block("wood/workbench/" + wood.serializedName, p -> workbenchBlock)
                .blockstate((ctx, prov) -> {
                    ResourceLocation path = TFMCore.id("block/wood/workbench/" + wood.serializedName);
                    prov.simpleBlock(ctx.getEntry(), prov.models().cube(ctx.getName(), wood.plankTexture, path.withSuffix("_top"), path.withSuffix("_front"),
                            path.withSuffix("_side"), path.withSuffix("_side"), path.withSuffix("_front"))
                            .texture("particle", path.withSuffix("_front")));
                })
                .tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("tfc", "workbenches")))
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .item(BlockItem::new)
                .tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("tfc", "workbenches"))).build()
                .register();
    }

    private static BlockEntry<Block> loom(TFMWood wood) {
        var loomBlock = Wood.BlockType.LOOM.create(wood).get();
        return TFMCore.REGISTRATE.block("wood/loom/" + wood.serializedName, p -> loomBlock)
                .blockstate((ctx, prov) -> {
                    ModelFile model = prov.models().withExistingParent(ctx.getName(), ResourceLocation.fromNamespaceAndPath("tfc", "block/loom"))
                            .texture("texture", wood.plankTexture)
                            .texture("particle", wood.plankTexture);

                    ModelUtils.cardinalBlockInverted(prov.getVariantBuilder(ctx.getEntry()), model);
                })
                .tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("tfc", "looms")))
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .onRegister(block -> {
                    TFMBlockEntities.addValidBEBlock(TFCBlockEntities.LOOM, block);
                })
                .item(BlockItem::new)
                .tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("tfc", "looms"))).build()
                .register();

    }

    private static BlockEntry<Block> sluice(TFMWood wood) {
        var sluiceBlock = Wood.BlockType.SLUICE.create(wood).get();
        return TFMCore.REGISTRATE.block("wood/sluice/" + wood.serializedName, p -> sluiceBlock)
                .blockstate((ctx, prov) -> {

                    ModelFile sluiceUpper = prov.models().withExistingParent("wood/sluice/" + wood.serializedName + "_upper", ResourceLocation.fromNamespaceAndPath("tfc", "block/sluice_upper"))
                            .texture("texture", TFMCore.id("block/wood/sheet/" + wood.serializedName));
                    ModelFile sluiceLower = prov.models().withExistingParent("wood/sluice/" + wood.serializedName + "_lower", ResourceLocation.fromNamespaceAndPath("tfc", "block/sluice_lower"))
                            .texture("texture", TFMCore.id("block/wood/sheet/" + wood.serializedName));

                    var builder = prov.getVariantBuilder(ctx.getEntry());

                    ModelUtils.forEachCardinalDirection(builder, sluiceLower, b -> b.with(TFCBlockStateProperties.UPPER, false));
                    ModelUtils.forEachCardinalDirection(builder, sluiceUpper, b -> b.with(TFCBlockStateProperties.UPPER, true));
                })
                .addLayer(() -> RenderType::cutout)
                .tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("tfc", "sluices")))
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .onRegister(block -> {
                    TFMBlockEntities.addValidBEBlock(TFCBlockEntities.SLUICE, block);
                })
                .item(BlockItem::new).model(ModelUtils.blockItemModel(TFMCore.id("block/wood/sluice/" + wood.serializedName + "_lower")))
                .tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("tfc", "sluices"))).build()
                .register();
    }

    private static BlockEntry<BarrelBlock> barrel(TFMWood wood) {
        var barrelBlock = Wood.BlockType.BARREL.create(wood).get();
        return TFMCore.REGISTRATE.block("wood/barrel/" + wood.serializedName, p -> (BarrelBlock) barrelBlock)
                .blockstate((ctx, prov) -> {

                    ModelFile barrel = prov.models().withExistingParent("wood/barrel/" + wood.serializedName, ResourceLocation.fromNamespaceAndPath("tfc", "block/barrel"))
                            .texture("particle", wood.plankTexture)
                            .texture("planks", wood.plankTexture)
                            .texture("sheet", TFMCore.id("block/wood/sheet/" + wood.serializedName));

                    ModelFile barrelSide = prov.models().withExistingParent("wood/barrel/" + wood.serializedName + "_side", ResourceLocation.fromNamespaceAndPath("tfc", "block/barrel_side"))
                            .texture("particle", wood.plankTexture)
                            .texture("planks", wood.plankTexture)
                            .texture("sheet", TFMCore.id("block/wood/sheet/" + wood.serializedName));

                    ModelFile barrelSideRack = prov.models()
                            .withExistingParent("wood/barrel/" + wood.serializedName + "_side_rack", ResourceLocation.fromNamespaceAndPath("tfc", "block/barrel_side_rack"))
                            .texture("particle", wood.plankTexture)
                            .texture("planks", wood.plankTexture)
                            .texture("sheet", TFMCore.id("block/wood/sheet/" + wood.serializedName));

                    ModelFile sealedBarrel = prov.models().withExistingParent("wood/barrel_sealed/" + wood.serializedName, ResourceLocation.fromNamespaceAndPath("tfc", "block/barrel_sealed"))
                            .texture("particle", wood.plankTexture)
                            .texture("planks", wood.plankTexture)
                            .texture("sheet", TFMCore.id("block/wood/sheet/" + wood.serializedName));

                    ModelFile sealedBarrelSide = prov.models()
                            .withExistingParent("wood/barrel_sealed/" + wood.serializedName + "_side", ResourceLocation.fromNamespaceAndPath("tfc", "block/barrel_side_sealed"))
                            .texture("particle", wood.plankTexture)
                            .texture("planks", wood.plankTexture)
                            .texture("sheet", TFMCore.id("block/wood/sheet/" + wood.serializedName));

                    ModelFile sealedBarrelSideRack = prov.models()
                            .withExistingParent("wood/barrel_sealed/" + wood.serializedName + "_side_rack", ResourceLocation.fromNamespaceAndPath("tfc", "block/barrel_side_sealed_rack"))
                            .texture("particle", wood.plankTexture)
                            .texture("planks", wood.plankTexture)
                            .texture("sheet", TFMCore.id("block/wood/sheet/" + wood.serializedName));

                    var builder = prov.getVariantBuilder(ctx.getEntry());

                    buildBarrelBlockStateEntry(builder, Direction.UP, 0, barrel, barrel, sealedBarrel, sealedBarrel);
                    buildBarrelBlockStateEntry(builder, Direction.EAST, 0, barrelSide, barrelSideRack, sealedBarrelSide, sealedBarrelSideRack);
                    buildBarrelBlockStateEntry(builder, Direction.WEST, 180, barrelSide, barrelSideRack, sealedBarrelSide, sealedBarrelSideRack);
                    buildBarrelBlockStateEntry(builder, Direction.SOUTH, 90, barrelSide, barrelSideRack, sealedBarrelSide, sealedBarrelSideRack);
                    buildBarrelBlockStateEntry(builder, Direction.NORTH, 270, barrelSide, barrelSideRack, sealedBarrelSide, sealedBarrelSideRack);
                })
                .addLayer(() -> RenderType::cutout)
                .tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("tfc", "barrels")))
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .onRegister(block -> {
                    TFMBlockEntities.addValidBEBlock(TFCBlockEntities.BARREL, block);
                })
                .item(BarrelBlockItem::new)
                .tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("tfc", "barrels"))).build()
                .register();
    }

    private static void buildBarrelBlockStateEntry(VariantBlockStateBuilder builder, Direction facing, int y, ModelFile barrel, ModelFile rack, ModelFile sealed, ModelFile sealedRack) {
        builder.partialState().with(TFCBlockStateProperties.FACING_NOT_DOWN, facing).with(TFCBlockStateProperties.SEALED, false).with(TFCBlockStateProperties.RACK, false).modelForState().rotationY(y)
                .modelFile(barrel).addModel()
                .partialState().with(TFCBlockStateProperties.FACING_NOT_DOWN, facing).with(TFCBlockStateProperties.SEALED, true).with(TFCBlockStateProperties.RACK, false).modelForState().rotationY(y)
                .modelFile(sealed).addModel()
                .partialState().with(TFCBlockStateProperties.FACING_NOT_DOWN, facing).with(TFCBlockStateProperties.SEALED, false).with(TFCBlockStateProperties.RACK, true).modelForState().rotationY(y)
                .modelFile(rack).addModel()
                .partialState().with(TFCBlockStateProperties.FACING_NOT_DOWN, facing).with(TFCBlockStateProperties.SEALED, true).with(TFCBlockStateProperties.RACK, true).modelForState().rotationY(y)
                .modelFile(sealedRack).addModel();
    }

    private static BlockEntry<Block> lectern(TFMWood wood) {
        var lecternBlock = Wood.BlockType.LECTERN.create(wood).get();
        return TFMCore.REGISTRATE.block("wood/lectern/" + wood.serializedName, p -> lecternBlock)
                .blockstate((ctx, prov) -> {

                    var path = "block/wood/lectern/" + wood.serializedName + "/";
                    ModelFile model = prov.models().withExistingParent(ctx.getName(), ResourceLocation.withDefaultNamespace("block/lectern"))
                            .texture("bottom", wood.plankTexture)
                            .texture("base", TFMCore.id(path + "base"))
                            .texture("front", TFMCore.id(path + "front"))
                            .texture("sides", TFMCore.id(path + "sides"))
                            .texture("top", TFMCore.id(path + "top"))
                            .texture("particle", TFMCore.id(path + "sides"));

                    ModelUtils.cardinalBlock(prov.getVariantBuilder(ctx.getEntry()), model);
                })
                .addLayer(() -> RenderType::cutout)
                .tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("tfc", "lecterns")))
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .onRegister(block -> {
                    TFMBlockEntities.addValidBEBlock(TFCBlockEntities.LECTERN, block);
                })
                .item(BlockItem::new)
                .tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("tfc", "lecterns"))).build()
                .register();

    }

    private static BlockEntry<Block> scribingTable(TFMWood wood) {
        var scribingTableBlock = Wood.BlockType.SCRIBING_TABLE.create(wood).get();
        return TFMCore.REGISTRATE.block("wood/scribing_table/" + wood.serializedName, p -> scribingTableBlock)
                .blockstate((ctx, prov) -> {
                    var model = prov.models().withExistingParent(ctx.getName(), ResourceLocation.fromNamespaceAndPath("tfc", "block/scribing_table"))
                            .texture("top", TFMCore.id("block/wood/scribing_table/" + wood.serializedName))
                            .texture("leg", wood.logTexture)
                            .texture("side", wood.plankTexture)
                            .texture("misc", ResourceLocation.fromNamespaceAndPath("tfc", "block/wood/scribing_table/scribing_paraphernalia"))
                            .texture("particle", wood.plankTexture);

                    ModelUtils.cardinalBlock(prov.getVariantBuilder(ctx.getEntry()), model);
                })
                .addLayer(() -> RenderType::cutout)
                .tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("tfc", "scribing_tables")))
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .item(BlockItem::new)
                .tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("tfc", "scribing_tables"))).build()
                .register();

    }

    private static BlockEntry<Block> sewingTable(TFMWood wood) {
        var sewingTableBlock = Wood.BlockType.SEWING_TABLE.create(wood).get();
        return TFMCore.REGISTRATE.block("wood/sewing_table/" + wood.serializedName, p -> sewingTableBlock)
                .blockstate((ctx, prov) -> {
                    var model = prov.models().withExistingParent(ctx.getName(), ResourceLocation.fromNamespaceAndPath("tfc", "block/sewing_table"))
                            .texture("0", wood.logTexture)
                            .texture("1", wood.plankTexture);

                    ModelUtils.cardinalBlock(prov.getVariantBuilder(ctx.getEntry()), model);
                })
                .addLayer(() -> RenderType::cutout)
                .tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("tfc", "sewing_tables")))
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .item(BlockItem::new)
                .tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("tfc", "sewing_tables"))).build()
                .register();
    }

    private static BlockEntry<Block> shelf(TFMWood wood) {
        var shelfBlock = Wood.BlockType.SHELF.create(wood).get();
        return TFMCore.REGISTRATE.block("wood/shelf/" + wood.serializedName, p -> shelfBlock)
                .blockstate((ctx, prov) -> {
                    var model = prov.models().withExistingParent(ctx.getName(), ResourceLocation.fromNamespaceAndPath("tfc", "block/wood/shelf"))
                            .texture("0", wood.plankTexture);

                    ModelUtils.cardinalBlock(prov.getVariantBuilder(ctx.getEntry()), model);
                })
                .tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("tfc", "shelves")))
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .onRegister(block -> {
                    TFMBlockEntities.addValidBEBlock(TFCBlockEntities.SHELF, block);
                })
                .item(BlockItem::new)
                .tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("tfc", "shelves"))).build()
                .register();

    }

    private static BlockEntry<Block> bookshelf(TFMWood wood) {
        var bookshelfBlock = Wood.BlockType.BOOKSHELF.create(wood).get();
        return TFMCore.REGISTRATE.block("wood/bookshelf/" + wood.serializedName, p -> bookshelfBlock)
                .blockstate((ctx, prov) -> {
                    prov.models()
                            .withExistingParent("wood/bookshelf/" + wood.serializedName + "_inventory", ResourceLocation.withDefaultNamespace("block/chiseled_bookshelf_inventory"))
                            .texture("top", TFMCore.id("block/wood/bookshelf/" + wood.serializedName + "_top"))
                            .texture("side", TFMCore.id("block/wood/bookshelf/" + wood.serializedName + "_side"))
                            .texture("front", TFMCore.id("block/wood/bookshelf/" + wood.serializedName + "_empty"));

                    ModelFile base = prov.models()
                            .withExistingParent("wood/bookshelf/" + wood.serializedName, ResourceLocation.withDefaultNamespace("block/chiseled_bookshelf"))
                            .texture("top", TFMCore.id("block/wood/bookshelf/" + wood.serializedName + "_top"))
                            .texture("side", TFMCore.id("block/wood/bookshelf/" + wood.serializedName + "_side"));

                    var builder = prov.getMultipartBuilder(ctx.getEntry());

                    BooleanProperty[] slots = new BooleanProperty[] {
                            BlockStateProperties.CHISELED_BOOKSHELF_SLOT_0_OCCUPIED,
                            BlockStateProperties.CHISELED_BOOKSHELF_SLOT_1_OCCUPIED,
                            BlockStateProperties.CHISELED_BOOKSHELF_SLOT_2_OCCUPIED,
                            BlockStateProperties.CHISELED_BOOKSHELF_SLOT_3_OCCUPIED,
                            BlockStateProperties.CHISELED_BOOKSHELF_SLOT_4_OCCUPIED,
                            BlockStateProperties.CHISELED_BOOKSHELF_SLOT_5_OCCUPIED
                    };
                    String[] vertical = { "top", "top", "top", "bottom", "bottom", "bottom" };
                    String[] horizontal = { "left", "mid", "right", "left", "mid", "right" };

                    for (Direction dir : Direction.Plane.HORIZONTAL) {
                        int rot = switch (dir) {
                            case EAST -> 90;
                            case SOUTH -> 180;
                            case WEST -> 270;
                            default -> 0;
                        };

                        builder.part()
                                .modelFile(base)
                                .rotationY(rot)
                                .uvLock(true)
                                .addModel()
                                .condition(BlockStateProperties.HORIZONTAL_FACING, dir);

                        for (int i = 0; i < slots.length; i++)
                            for (boolean occupied : new boolean[] { false, true }) {
                                String state = occupied ? "occupied" : "empty";

                                ModelFile model = prov.models()
                                        .withExistingParent(
                                                ctx.getName() + "_" + state + "_" + vertical[i] + "_" + horizontal[i],
                                                "block/chiseled_bookshelf_" + state + "_slot_" + vertical[i] + "_" + horizontal[i])
                                        .texture("texture", TFMCore.id("block/wood/bookshelf/" + wood.serializedName + "_" + state));

                                builder.part()
                                        .modelFile(model)
                                        .rotationY(rot)
                                        .addModel()
                                        .condition(BlockStateProperties.HORIZONTAL_FACING, dir)
                                        .condition(slots[i], occupied);
                            }
                    }
                })
                .tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("tfc", "bookshelves")))
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .onRegister(block -> {
                    TFMBlockEntities.addValidBEBlock(TFCBlockEntities.BOOKSHELF, block);
                })
                .item(BlockItem::new).model(ModelUtils.blockItemModel(TFMCore.id("block/wood/bookshelf/" + wood.serializedName + "_inventory")))
                .tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("tfc", "bookshelves"))).build()
                .register();
    }

    // Firmalife
    private static class FirmaCustomLoader extends CustomLoaderBuilder<BlockModelBuilder> {

        private final ResourceLocation parentBlock;

        public static FirmaCustomLoader get(ResourceLocation loaderId, ResourceLocation parentBlock, BlockModelBuilder parent, ExistingFileHelper existingFileHelper) {
            return new FirmaCustomLoader(loaderId, parentBlock, parent, existingFileHelper);
        }

        protected FirmaCustomLoader(ResourceLocation loaderId, ResourceLocation parentBlock, BlockModelBuilder parent, ExistingFileHelper existingFileHelper) {
            super(loaderId, parent, existingFileHelper, true);
            this.parentBlock = parentBlock;
        }

        @Override
        public JsonObject toJson(JsonObject json) {
            super.toJson(json);
            var obj = new JsonObject();
            obj.addProperty("parent", parentBlock.toString());
            json.add("base", obj);
            return json;
        }
    }

    private static BlockEntry<FoodShelfBlock> foodShelf(TFMWood wood) {
        return TFMCore.REGISTRATE.block("wood/food_shelf/" + wood.serializedName, p -> new FoodShelfBlock(shelfProperties().mapColor(wood.woodColor())))
                .blockstate((ctx, prov) -> {
                    prov.models().withExistingParent(ctx.getName(), ResourceLocation.fromNamespaceAndPath("firmalife", "block/food_shelf_base"))
                            .texture("wood", wood.plankTexture);

                    var dynamicModel = prov.models().getBuilder("wood/food_shelf/" + wood.serializedName + "_dynamic")
                            .customLoader((t, existing) -> FirmaCustomLoader.get(ResourceLocation.fromNamespaceAndPath("firmalife", "food_shelf"),
                                    TFMCore.id("block/wood/food_shelf/" + wood.serializedName), t,
                                    existing))
                            .end();

                    ModelUtils.cardinalBlockInverted(prov.getVariantBuilder(ctx.getEntry()), dynamicModel);
                })
                .addLayer(() -> RenderType::cutout)
                .tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("firmalife", "food_shelves")))
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .onRegister(block -> {
                    TFMBlockEntities.addValidBEBlock(FLBlockEntities.FOOD_SHELF, block);
                })
                .item(BlockItem::new).model(ModelUtils.blockItemModel(TFMCore.id("block/wood/food_shelf/" + wood.serializedName)))
                .tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("firmalife", "food_shelves"))).build()
                .register();
    }

    private static BlockEntry<HangerBlock> hanger(TFMWood wood) {
        return TFMCore.REGISTRATE.block("wood/hanger/" + wood.serializedName, p -> new HangerBlock(hangerProperties().mapColor(wood.woodColor())))
                .blockstate((ctx, prov) -> {
                    prov.models().withExistingParent(ctx.getName(), ResourceLocation.fromNamespaceAndPath("firmalife", "block/hanger_base"))
                            .texture("wood", wood.plankTexture)
                            .texture("string", ResourceLocation.withDefaultNamespace("block/white_wool"));

                    var dynamicModel = prov.models().getBuilder("wood/hanger/" + wood.serializedName + "_dynamic")
                            .customLoader(
                                    (t, existing) -> FirmaCustomLoader.get(ResourceLocation.fromNamespaceAndPath("firmalife", "hanger"), TFMCore.id("block/wood/hanger/" + wood.serializedName), t,
                                            existing))
                            .end();

                    prov.simpleBlock(ctx.getEntry(), dynamicModel);
                })
                .addLayer(() -> RenderType::cutout)
                .tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("firmalife", "hangers")))
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .onRegister(block -> {
                    TFMBlockEntities.addValidBEBlock(FLBlockEntities.HANGER, block);
                })
                .item(BlockItem::new).model(ModelUtils.blockItemModel(TFMCore.id("block/wood/hanger/" + wood.serializedName)))
                .tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("firmalife", "hangers"))).build()
                .register();

    }

    private static BlockEntry<JarbnetBlock> jarbnet(TFMWood wood) {
        return TFMCore.REGISTRATE.block("wood/jarbnet/" + wood.serializedName, p -> new JarbnetBlock(jarbnetProperties().mapColor(wood.woodColor())))
                .blockstate((ctx, prov) -> {
                    prov.models().withExistingParent(ctx.getName(), ResourceLocation.fromNamespaceAndPath("firmalife", "block/jarbnet"))
                            .texture("planks", wood.plankTexture)
                            .texture("log", wood.logTexture)
                            .texture("sheet", TFMCore.id("block/wood/sheet/" + wood.serializedName));

                    prov.models().withExistingParent(ctx.getName() + "_shut", ResourceLocation.fromNamespaceAndPath("firmalife", "block/jarbnet_shut"))
                            .texture("planks", wood.plankTexture)
                            .texture("log", wood.logTexture)
                            .texture("sheet", TFMCore.id("block/wood/sheet/" + wood.serializedName));

                    var dynamicModel = prov.models().getBuilder("wood/jarbnet/" + wood.serializedName + "_dynamic")
                            .customLoader(
                                    (t, existing) -> FirmaCustomLoader.get(ResourceLocation.fromNamespaceAndPath("firmalife", "jarbnet"), TFMCore.id("block/wood/jarbnet/" + wood.serializedName), t,
                                            existing))
                            .end();

                    var dynamicModelShut = prov.models().getBuilder("wood/jarbnet/" + wood.serializedName + "_shut_dynamic")
                            .customLoader((t, existing) -> FirmaCustomLoader.get(ResourceLocation.fromNamespaceAndPath("firmalife", "jarbnet"),
                                    TFMCore.id("block/wood/jarbnet/" + wood.serializedName + "_shut"),
                                    t, existing))
                            .end();

                    var builder = prov.getVariantBuilder(ctx.getEntry());

                    ModelUtils.forEachCardinalDirection(builder, dynamicModel, b -> b.with(BlockStateProperties.OPEN, true));
                    ModelUtils.forEachCardinalDirection(builder, dynamicModelShut, b -> b.with(BlockStateProperties.OPEN, false));
                })
                .addLayer(() -> RenderType::cutout)
                .tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("firmalife", "jarbnets")))
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .onRegister(block -> {
                    TFMBlockEntities.addValidBEBlock(FLBlockEntities.JARBNET, block);
                })
                .item(BlockItem::new).model(ModelUtils.blockItemModel(TFMCore.id("block/wood/jarbnet/" + wood.serializedName)))
                .tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("firmalife", "jarbnets"))).build()
                .register();

    }

    private static BlockEntry<KegCoreBlock> kegCore(TFMWood wood, BlockEntry<? extends Block> kegSubBlock) {
        var properties = ExtendedProperties.of().mapColor(wood.woodColor()).sound(SoundType.WOOD)
                .noOcclusion().strength(10f).pushReaction(PushReaction.BLOCK).flammableLikeLogs().blockEntity(FLBlockEntities.KEG);

        return TFMCore.REGISTRATE.block("wood/keg/" + wood.serializedName, p -> new KegCoreBlock(properties, kegSubBlock))
                .blockstate((ctx, prov) -> {
                    var builder = prov.getVariantBuilder(ctx.getEntry());

                    kegTextures(prov.models().withExistingParent(ctx.getName() + "_item", ResourceLocation.fromNamespaceAndPath("firmalife", "block/big_barrel_item")), wood);

                    var unsealedModel = kegTextures(prov.models().withExistingParent(ctx.getName() + "_0_unsealed", ResourceLocation.fromNamespaceAndPath("firmalife", "block/big_barrel_0_unsealed")),
                            wood);

                    var sealedModel = kegTextures(prov.models().withExistingParent(ctx.getName() + "_0_sealed", ResourceLocation.fromNamespaceAndPath("firmalife", "block/big_barrel_0_sealed")), wood);
                    ;

                    ModelUtils.forEachCardinalDirection(builder, unsealedModel, b -> b.with(TFCBlockStateProperties.SEALED, false));
                    ModelUtils.forEachCardinalDirection(builder, sealedModel, b -> b.with(TFCBlockStateProperties.SEALED, true));
                })
                .tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("firmalife", "big_barrels")))
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .onRegister(block -> {
                    TFMBlockEntities.addValidBEBlock(FLBlockEntities.KEG, block);
                })
                .item(BlockItem::new).model(ModelUtils.blockItemModel(TFMCore.id("block/wood/keg/" + wood.serializedName + "_item")))
                .tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("firmalife", "big_barrels"))).build()
                .register();
    }

    private static BlockEntry<KegSubBlock> kegSub(TFMWood wood) {
        var properties = ExtendedProperties.of().mapColor(wood.woodColor()).sound(SoundType.WOOD)
                .noOcclusion().strength(10f).pushReaction(PushReaction.BLOCK).flammableLikeLogs().blockEntity(FLBlockEntities.KEG_SUB);

        return TFMCore.REGISTRATE.block("wood/keg_sub/" + wood.serializedName, p -> new KegSubBlock(properties))
                .blockstate((ctx, prov) -> {
                    var builder = prov.getVariantBuilder(ctx.getEntry());

                    for (int i = 1; i < 8; i++) {
                        var model = kegTextures(prov.models().withExistingParent(ctx.getName() + "_" + i, ResourceLocation.fromNamespaceAndPath("firmalife", "block/big_barrel_" + i)), wood);
                        int finalI = i;
                        ModelUtils.forEachCardinalDirection(builder, model, b -> b.with(FLStateProperties.BARREL_PART, finalI));
                    }

                })
                .tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("firmalife", "big_barrels")))
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .onRegister(block -> {
                    TFMBlockEntities.addValidBEBlock(FLBlockEntities.KEG_SUB, block);
                })
                .register();
    }

    private static BlockModelBuilder kegTextures(BlockModelBuilder builder, TFMWood wood) {
        return builder.texture("0", TFMCore.id("block/wood/big_barrel/" + wood.serializedName + "_3_side"))
                .texture("1", TFMCore.id("block/wood/big_barrel/" + wood.serializedName + "_0"))
                .texture("2", TFMCore.id("block/wood/big_barrel/" + wood.serializedName + "_0_side"))
                .texture("3", TFMCore.id("block/wood/big_barrel/" + wood.serializedName + "_1"))
                .texture("4", TFMCore.id("block/wood/big_barrel/" + wood.serializedName + "_1_side"))
                .texture("5", TFMCore.id("block/wood/big_barrel/" + wood.serializedName + "_2"))
                .texture("6", TFMCore.id("block/wood/big_barrel/" + wood.serializedName + "_2_side"))
                .texture("7", TFMCore.id("block/wood/big_barrel/" + wood.serializedName + "_3"))
                .texture("8", TFMCore.id("block/wood/big_barrel/" + wood.serializedName + "_3_top"))
                .texture("9", TFMCore.id("block/wood/big_barrel/" + wood.serializedName + "_0_top"))
                .texture("10", TFMCore.id("block/wood/big_barrel/" + wood.serializedName + "_1_top"))
                .texture("11", TFMCore.id("block/wood/big_barrel/" + wood.serializedName + "_2_top"))
                .texture("12", wood.logTexture);
    }

    private static BlockEntry<WineShelfBlock> wineShelf(TFMWood wood) {
        var properties = ExtendedProperties.of().mapColor(wood.woodColor()).sound(SoundType.WOOD).noOcclusion()
                .strength(4f).pushReaction(PushReaction.BLOCK).flammableLikeLogs().blockEntity(FLBlockEntities.WINE_SHELF);

        return TFMCore.REGISTRATE.block("wood/wine_shelf/" + wood.serializedName, p -> new WineShelfBlock(properties))
                .blockstate((ctx, prov) -> {

                    prov.models().withExistingParent(ctx.getName(), ResourceLocation.fromNamespaceAndPath("firmalife", "block/wine_shelf"))
                            .texture("0", wood.plankTexture)
                            .texture("2", TFMCore.id("block/wood/sheet/" + wood.serializedName))
                            .texture("3", wood.strippedLogTexture);

                    var dynamicModel = prov.models().getBuilder("wood/wine_shelf/" + wood.serializedName + "_dynamic")
                            .customLoader((t, existing) -> FirmaCustomLoader.get(ResourceLocation.fromNamespaceAndPath("firmalife", "wine_shelf"),
                                    TFMCore.id("block/wood/wine_shelf/" + wood.serializedName), t,
                                    existing))
                            .end();

                    ModelUtils.cardinalBlock(prov.getVariantBuilder(ctx.getEntry()), dynamicModel);

                })
                .tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("firmalife", "wine_shelves")))
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .onRegister(block -> {
                    TFMBlockEntities.addValidBEBlock(FLBlockEntities.WINE_SHELF, block);
                })
                .item(BlockItem::new).model(ModelUtils.blockItemModel(TFMCore.id("block/wood/wine_shelf/" + wood.serializedName)))
                .tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("firmalife", "wine_shelves"))).build()
                .register();
    }

    private static BlockEntry<StompingBarrelBlock> stompingBarrel(TFMWood wood) {
        var properties = ExtendedProperties.of().mapColor(wood.woodColor()).sound(SoundType.WOOD).noOcclusion().strength(4f)
                .pushReaction(PushReaction.BLOCK).flammableLikeLogs().blockEntity(FLBlockEntities.STOMPING_BARREL);

        return TFMCore.REGISTRATE.block("wood/stomping_barrel/" + wood.serializedName, p -> new StompingBarrelBlock(properties))
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(),
                        prov.models().withExistingParent(ctx.getName(), ResourceLocation.fromNamespaceAndPath("firmalife", "block/stomping_barrel"))
                                .texture("0", TFMCore.id("block/wood/sheet/" + wood.serializedName))))
                .addLayer(() -> RenderType::cutout)
                .tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("firmalife", "stomping_barrels")))
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .onRegister(block -> {
                    TFMBlockEntities.addValidBEBlock(FLBlockEntities.STOMPING_BARREL, block);
                })
                .item(BlockItem::new)
                .tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("firmalife", "stomping_barrels"))).build()
                .register();

    }

    private static BlockEntry<BarrelPressBlock> barrelPress(TFMWood wood) {
        var properties = ExtendedProperties.of().mapColor(wood.woodColor()).sound(SoundType.WOOD).noOcclusion()
                .strength(4f).pushReaction(PushReaction.BLOCK).flammableLikeLogs().blockEntity(FLBlockEntities.BARREL_PRESS).ticks(BarrelPressBlockEntity::tick);

        return TFMCore.REGISTRATE.block("wood/barrel_press/" + wood.serializedName, p -> new BarrelPressBlock(properties))
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(),
                        prov.models().withExistingParent(ctx.getName(), ResourceLocation.fromNamespaceAndPath("firmalife", "block/barrel_press"))
                                .texture("0", TFMCore.id("block/wood/sheet/" + wood.serializedName))))
                .addLayer(() -> RenderType::cutout)
                .tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("firmalife", "barrel_presses")))
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .onRegister(block -> {
                    TFMBlockEntities.addValidBEBlock(FLBlockEntities.BARREL_PRESS, block);
                })
                .item(BlockItem::new)
                .tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("firmalife", "barrel_presses"))).build()
                .register();

    }
}
