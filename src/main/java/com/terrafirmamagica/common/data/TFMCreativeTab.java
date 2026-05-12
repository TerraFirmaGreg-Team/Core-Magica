package com.terrafirmamagica.common.data;

import static com.terrafirmamagica.TFMCore.REGISTRATE;

import org.jetbrains.annotations.NotNull;

import com.terrafirmamagica.TFMRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;

import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;

@SuppressWarnings("unused")
public class TFMCreativeTab {
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> TFM = REGISTRATE
            .defaultCreativeTab("tfm",
                    builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("tfm", REGISTRATE))
                            .icon(() -> new ItemStack(TFCItems.FOOD.get(Food.POTATO).get()))
                            .title(Component.translatable("tfm.creative_tab.tfm"))
                            .build())
            .register();

    public static void init() {
    }

    public record RegistrateDisplayItemsGenerator(String name, TFMRegistrate registrate) implements CreativeModeTab.DisplayItemsGenerator {

        @Override
        public void accept(@NotNull CreativeModeTab.ItemDisplayParameters itemDisplayParameters,
                @NotNull CreativeModeTab.Output output) {
            var tab = registrate.get(name, Registries.CREATIVE_MODE_TAB);
            for (var entry : registrate.getAll(Registries.ITEM)) {
                if (!registrate.isInCreativeTab(entry, tab))
                    continue;
                Item item = entry.get();
                var stack = new ItemStack(item, 1);
                output.accept(item);
            }
        }
    }
}
