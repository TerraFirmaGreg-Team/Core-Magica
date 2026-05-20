package com.terrafirmamagica.common.data;

import java.util.Objects;

import com.terrafirmamagica.TFMCore;
import com.terrafirmamagica.common.ceremony.AnvilUpgradeCeremonyInstance;
import com.terrafirmamagica.common.ceremony.EvergreenSunflowersCeremonyInstance;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import pokefenn.totemic.api.TotemicAPI;
import pokefenn.totemic.api.ceremony.Ceremony;
import pokefenn.totemic.api.music.MusicInstrument;
import pokefenn.totemic.api.registry.RegistryAPI;

public class TFMCeremonies {
    public static final DeferredRegister<Ceremony> CEREMONIES = DeferredRegister.create(RegistryAPI.CEREMONY_REGISTRY, TFMCore.MOD_ID);

    public static void init() {
    }

    public static final DeferredHolder<Ceremony, Ceremony> ANVIL_UPGRADE = CEREMONIES.register("anvil_upgrade", () -> new Ceremony(
            2000,
            20 * 20,
            () -> AnvilUpgradeCeremonyInstance.INSTANCE,
            () -> instrument("totemic:rattle"),
            () -> instrument("totemic:eagle_bone_whistle")));

    public static final DeferredHolder<Ceremony, Ceremony> EVERGREEN_SUNFLOWERS = CEREMONIES.register("evergreen", () -> new Ceremony(
            2000,
            20 * 20,
            () -> EvergreenSunflowersCeremonyInstance.INSTANCE,
            () -> instrument("totemic:rattle"),
            () -> instrument("totemic:flute")));

    private static MusicInstrument instrument(String id) {
        return Objects.requireNonNull(TotemicAPI.get().registry().instruments()
                .get(ResourceLocation.parse(id)));
    }
}
