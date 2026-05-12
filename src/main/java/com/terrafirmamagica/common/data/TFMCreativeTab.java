package com.terrafirmamagica.common.data;

import static com.terrafirmamagica.TFMCore.REGISTRATE;

import com.terrafirmamagica.TFMRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unused")
public class TFMCreativeTab {
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> TFM = REGISTRATE.defaultCreativeTab("tfm",
            builder -> builder
                    .displayItems(new RegistrateDisplayItemsGenerator("tfm", REGISTRATE))
                    .title(Component.translatable("tfm.creative_tab.tfg"))
                    .build())
            .register();

    public static void init() {
    }

    public record RegistrateDisplayItemsGenerator(String name, TFMRegistrate registrate) implements CreativeModeTab.DisplayItemsGenerator {
        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
            var tab = registrate.get(name, Registries.CREATIVE_MODE_TAB);
            for (var entry : registrate.getAll(Registries.BLOCK)) {
                Block block = entry.get();
                var stack = new ItemStack(block, 1);

                if (registrate.isInCreativeTab(entry, tab))
                    continue;
                if (entry.getId().getNamespace().equals("tfm") && !stack.isEmpty())
                    output.accept(block);
            }
            for (var entry : registrate.getAll(Registries.ITEM)) {
                if (registrate.isInCreativeTab(entry, tab))
                    continue;
                Item item = entry.get();
                var stack = new ItemStack(item, 1);
                if (item instanceof BlockItem)
                    continue;
                if (entry.getId().getNamespace().equals("tfm"))
                    output.accept(stack);
            }
        }
    }
}
