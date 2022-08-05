package me.pixeldots.scriptedmodels.platform.network;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.pixeldots.scriptedmodels.ScriptedModels;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("scriptedmodels")
public class ScriptedModelsMain {

    public static Map<UUID, EntityData> EntityData = new HashMap<>();
    public static int MaximumScriptLineCount = 0;
    public static int CompressThreshold = -1;

    public static String ProVer = "1"; // Protocol_Version

    public ScriptedModelsMain() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);

        ScriptedModels client = new ScriptedModels();
        modEventBus.addListener(client::onClientSetup);
        MinecraftForge.EVENT_BUS.register(client);
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
        //handle_config(event);
        ServerNetwork.register(0);
        ClientNetwork.register(3);
    }

    /*public void handle_config(final FMLCommonSetupEvent event) {
        Pair<SMConfig, ForgeConfigSpec> CONFIG = new ForgeConfigSpec.Builder()
            .configure(SMConfig::new);

        CONFIG.getValue().save();
        ModLoadingContext.get().registerConfig(Type.COMMON, CONFIG.getValue());
    }*/
    
    public static class EntityData {
        public String script;
        public Map<Integer, String> parts = new HashMap<>();
    }

    public static class NetworkIdentifyers {
        // Requests all entity scripts
        public static ResourceLocation request_entitys = new ResourceLocation("scriptedmodels", "request_entitys");

        // Updates the servers database with the new entity script
        public static ResourceLocation changed_script = new ResourceLocation("scriptedmodels", "new_scripts");

        // Forces a client to update an entity's script
        public static ResourceLocation recive_script = new ResourceLocation("scriptedmodels", "recive_script");

        // Remove's a script from an entity
        public static ResourceLocation remove_script = new ResourceLocation("scriptedmodels", "remove_script");

        // Resets the entity's scripts
        public static ResourceLocation reset_entity = new ResourceLocation("scriptedmodels", "reset_entity");
        
        // player connection identifier
        public static ResourceLocation connection = new ResourceLocation("scriptedmodels", "connection");

        // Sends a message to the client
        public static ResourceLocation error = new ResourceLocation("scriptedmodels", "error");
    }

}
