package com.terrafirmagica.core.utils;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ModelUtils {

    public static <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelProvider> layeredItemModel(ResourceLocation... layers) {
        return (ctx, prov) -> {
            ItemModelBuilder ret = prov.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("item/generated"));
            for (int i = 0; i < layers.length; i++) {
                ret = ret.texture("layer" + i, layers[i]);
            }
        };
    }

    public static NonNullBiConsumer<DataGenContext<Item, BlockItem>, RegistrateItemModelProvider> blockItemModel(ResourceLocation blockModel) {
        return (ctx, prov) -> prov.withExistingParent(ctx.getName(), blockModel);
    }

    public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> blockVariantsRotated(ResourceLocation base) {
        return (ctx, prov) -> {
            var builder = prov.getVariantBuilder(ctx.get()).partialState();
            var model = prov.models().getExistingFile(base);
            builder.addModels(new ConfiguredModel(model),
                    new ConfiguredModel(model, 0, 90, false),
                    new ConfiguredModel(model, 0, 180, false),
                    new ConfiguredModel(model, 0, 270, false));
        };
    }

    public static void blockVariantsRotated(VariantBlockStateBuilder builder, ModelFile model) {
        builder.partialState().addModels(new ConfiguredModel(model),
                new ConfiguredModel(model, 0, 90, false),
                new ConfiguredModel(model, 0, 180, false),
                new ConfiguredModel(model, 0, 270, false));
    }

    public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> blockVariants(ResourceLocation... variants) {
        return (ctx, prov) -> {
            List<ModelFile.ExistingModelFile> models = Arrays.stream(variants).map(v -> prov.models().getExistingFile(v)).toList();

            var builder = prov.getVariantBuilder(ctx.get()).partialState();
            models.forEach(m -> builder.addModels(new ConfiguredModel(m)));
        };
    }

    public static void cardinalBlock(VariantBlockStateBuilder builder, ModelFile model) {
        builder.partialState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .modelForState().modelFile(model).addModel()
                .partialState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
                .modelForState().modelFile(model).rotationY(180).addModel()
                .partialState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST)
                .modelForState().modelFile(model).rotationY(270).addModel()
                .partialState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST)
                .modelForState().modelFile(model).rotationY(90).addModel();
    }

    public static void forEachCardinalDirection(VariantBlockStateBuilder builder, ModelFile model,
                                                Function<VariantBlockStateBuilder.PartialBlockstate, VariantBlockStateBuilder.PartialBlockstate> func) {
        var north = builder.partialState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH);
        func.apply(north).modelForState().modelFile(model).addModel();
        var south = builder.partialState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
        func.apply(south).modelForState().modelFile(model).rotationY(180).addModel();
        var east = builder.partialState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST);
        func.apply(east).modelForState().modelFile(model).rotationY(90).addModel();
        var west = builder.partialState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST);
        func.apply(west).modelForState().modelFile(model).rotationY(270).addModel();
    }

    // Thanks TFC
    public static void cardinalBlockInverted(VariantBlockStateBuilder builder, ModelFile model) {
        builder.partialState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .modelForState().modelFile(model).rotationY(180).addModel()
                .partialState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
                .modelForState().modelFile(model).addModel()
                .partialState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST)
                .modelForState().modelFile(model).rotationY(90).addModel()
                .partialState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST)
                .modelForState().modelFile(model).rotationY(270).addModel();
    }
}
