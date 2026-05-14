package com.terrafirmamagica.common.event;

import com.terrafirmamagica.TFMCore;

import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

@EventBusSubscriber(modid = TFMCore.MOD_ID)
public class TFMHexaliaEvents {

    @SubscribeEvent
    public static void modifyItemComponents(ModifyDefaultComponentsEvent event) {
        event.modify(ModItems.GHOSTVEIL.get(), builder -> builder.set(DataComponents.ATTRIBUTE_MODIFIERS,
                ItemAttributeModifiers.builder()
                        .add(Attributes.ARMOR,
                                new AttributeModifier(
                                        ResourceLocation.parse("tfm:ghostveil_armor"),
                                        2.0,
                                        AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.BODY)
                        .build()));
    }
}
