package com.terrafirmamagica;

import com.terrafirmamagica.client.ClientInit;
import com.terrafirmamagica.common.CommonInit;
import com.terrafirmamagica.common.ceremony.AnvilUpgradeCeremonyInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import pokefenn.totemic.api.TotemicAPI;
import pokefenn.totemic.api.ceremony.Ceremony;
import pokefenn.totemic.api.music.MusicInstrument;
import pokefenn.totemic.api.registry.RegistryAPI;

@Mod(TFMCore.MOD_ID)
public class TFMCore {
    public static final String MOD_ID = "tfm";
    public static final String NAME = "TerraFirmaMagica-Core";
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    public static final TFMRegistrate REGISTRATE = TFMRegistrate.create(TFMCore.MOD_ID);

    // Ceremony Totemic
    private static final DeferredRegister<Ceremony> CEREMONIES =
            DeferredRegister.create(RegistryAPI.CEREMONY_REGISTRY, MOD_ID);

    public static final DeferredHolder<Ceremony, Ceremony> ANVIL_UPGRADE =
            CEREMONIES.register("anvil_upgrade", () ->
                    new Ceremony(
                            2000,
                            20 * 20,
                            () -> AnvilUpgradeCeremonyInstance.INSTANCE,
                            () -> instrument("totemic:rattle"),
                            () -> instrument("totemic:eagle_bone_whistle")
                    )
            );

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    @ApiStatus.Internal
    public static IEventBus tfmModBus;

    public TFMCore(IEventBus modBus, ModContainer container) {
        TFMCore.tfmModBus = modBus;

        CEREMONIES.register(modBus);  // ← ligne clé

        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        CommonInit.init(modBus);
        ClientInit.init(modBus);
    }

    private static MusicInstrument instrument(String id) {
        return TotemicAPI.get().registry().instruments()
                .get(ResourceLocation.parse(id));
    }
}