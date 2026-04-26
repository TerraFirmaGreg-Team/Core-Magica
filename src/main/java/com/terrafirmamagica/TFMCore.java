package com.terrafirmamagica;

import com.terrafirmamagica.client.ClientInit;
import com.terrafirmamagica.common.CommonInit;
import com.tterrag.registrate.util.RegistrateDistExecutor;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import org.jetbrains.annotations.ApiStatus;

@Mod(TFMCore.MOD_ID)
public class TFMCore {
    public static final String MOD_ID = "tfm";
    public static final String NAME = "TerraFirmaMagica-Core";
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    public static final TFMRegistrate REGISTRATE = TFMRegistrate.create(TFMCore.MOD_ID);

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    @ApiStatus.Internal
    public static IEventBus tfmModBus;

    public TFMCore(IEventBus modBus, ModContainer container) {
        TFMCore.tfmModBus = modBus;

        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        CommonInit.init(modBus);
        ClientInit.init(modBus);
    }
}
