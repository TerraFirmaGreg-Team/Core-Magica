package com.terrafirmamagica.common.data.blocks;

import java.util.Map;

import com.terrafirmamagica.TFMCore;
import com.terrafirmamagica.common.data.TFMWood;
import com.terrafirmamagica.utils.ModelUtils;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.dries007.tfc.common.blocks.wood.Wood;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

@SuppressWarnings({ "unused" })
public class TFMBlocks_Wood {
    public static final Map<TFMWood, Map<Wood.BlockType, BlockEntry<? extends Block>>> WOODS = new Object2ObjectOpenHashMap<>();

    public static void init() {
        TFMWood.registerBlockSetTypes();
        for (TFMWood value : TFMWood.VALUES) {
            registerWood(value);
        }
    }

    private static void registerWood(TFMWood wood) {
        Map<Wood.BlockType, BlockEntry<? extends Block>> blocks = new Object2ObjectOpenHashMap<>();

        if (wood.generateWood) {
            // Used for entirely new wood types
        }

        blocks.put(Wood.BlockType.LOG_FENCE, logFence(wood));

        WOODS.put(wood, blocks);
    }

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
}
