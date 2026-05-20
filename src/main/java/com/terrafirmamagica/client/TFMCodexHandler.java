package com.terrafirmamagica.client;

import java.lang.reflect.Field;
import java.util.List;

import com.terrafirmamagica.TFMCore;
import com.terrafirmamagica.common.eidolon.TFMEidolonRegistry;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;

import alexthw.eidolon_repraised.api.spells.Sign;
import alexthw.eidolon_repraised.codex.*;
import alexthw.eidolon_repraised.registries.Signs;

@OnlyIn(Dist.CLIENT)
public class TFMCodexHandler {

    @SubscribeEvent
    public static void onCodexPostInit(CodexEvents.PostInit event) {

        Chapter transformChapter = new CodexBuilder()
                .title("tfm.codex.transform.title")
                .chantPage("tfm.codex.transform.details", TFMEidolonRegistry.TRANSFORM_SPELL)
                .build();

        Page chantPage = transformChapter.get(0);
        if (chantPage instanceof ChantPage cp) {
            try {
                Field chantField = ChantPage.class.getDeclaredField("chant");
                chantField.setAccessible(true);
                chantField.set(cp, new Sign[] {
                        Signs.WINTER_SIGN,
                        Signs.MAGIC_SIGN,
                        Signs.DEATH_SIGN,
                        Signs.MIND_SIGN,
                        Signs.WICKED_SIGN
                });
            } catch (NoSuchFieldException | IllegalAccessException e) {
                TFMCore.LOGGER.error("Failed to set chant signs in ChantPage", e);
            }
        }

        IndexPage.ResearchLockedEntry transformEntry = new IndexPage.ResearchLockedEntry(
                transformChapter,
                new ItemStack(Items.BONE),
                TFMEidolonRegistry.TRANSFORM_RESEARCH);

        if (CodexChapters.SPELLS_INDEX != null) {
            Page page = CodexChapters.SPELLS_INDEX.get(1);
            if (page instanceof IndexPage indexPage) {
                try {
                    Field entriesField = IndexPage.class.getDeclaredField("entries");
                    entriesField.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    List<IndexPage.IndexEntry> entries = (List<IndexPage.IndexEntry>) entriesField.get(indexPage);
                    entries.add(transformEntry);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    TFMCore.LOGGER.error("Failed to add transform entry to Codex", e);
                }
            }
        }
    }
}
