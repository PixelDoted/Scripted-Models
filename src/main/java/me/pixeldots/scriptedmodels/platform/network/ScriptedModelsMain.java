package me.pixeldots.scriptedmodels.platform.network;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import me.pixeldots.scriptedmodels.ScriptedModels;
import me.pixeldots.scriptedmodels.platform.network.Packets.RequestEntitys;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod("scriptedmodels")
public class ScriptedModelsMain {

    public static Map<UUID, EntityData> EntityData = new HashMap<>();
    public static int MaximumScriptLineCount = 0;
    public static int CompressThreshold = -1;

    public static SimpleChannel SMPacket = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("scriptedmodels", "packet"),
            () -> "1",
            NetworkRegistry.ACCEPTVANILLA::equals,
            NetworkRegistry.ACCEPTVANILLA::equals);

    public ScriptedModelsMain() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(new ScriptedModels());
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
        handle_config();
        ServerNetwork.register();
        register_networking();
    }

    public void register_networking() {
        int id = 0;
        SMPacket.registerMessage(id++, RequestEntitys.class, RequestEntitys::encode, RequestEntitys::decode, RequestEntitys::new);
        SMPacket.registerMessage(id++, messageType, encoder, decoder, messageConsumer);
        SMPacket.registerMessage(id++, messageType, encoder, decoder, messageConsumer);
        SMPacket.registerMessage(id++, messageType, encoder, decoder, messageConsumer);
        SMPacket.registerMessage(id++, messageType, encoder, decoder, messageConsumer);
        SMPacket.registerMessage(id++, messageType, encoder, decoder, messageConsumer);
        SMPacket.registerMessage(id++, messageType, encoder, decoder, messageConsumer);
    }

    public void handle_config() {
        String config_path = FabricLoader.getInstance().getConfigDir().toFile().getAbsolutePath() + "/ScriptedModels.conf";
        if (Files.exists(Path.of(config_path))) {
            Properties properties = new Properties();
            try {
                properties.load(new FileReader(config_path));
                MaximumScriptLineCount = Integer.parseInt((String)properties.get("Server.MaximumScriptLineCount"));
            } catch (IOException | NumberFormatException e) {}
        } else {
            try {
                Properties properties = new Properties();
                properties.put("Server.MaximumScriptLineCount", String.valueOf(MaximumScriptLineCount));
                properties.store(new FileWriter(config_path), null);
            } catch (IOException | NumberFormatException e) {}
        }
    }
    
    public static class EntityData {
        public String script;
        public Map<Integer, String> parts = new HashMap<>();
    }

    public static class NetworkIdentifyers {
        // Requests all entity scripts
        public static SimpleChannel request_entitys = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("scriptedmodels", "request_entitys"),
            () -> "1",
            NetworkRegistry.ACCEPTVANILLA::equals,
            NetworkRegistry.ACCEPTVANILLA::equals);

        // Updates the servers database with the new entity script
        public static SimpleChannel changed_script = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("scriptedmodels", "new_scripts"),
            () -> "1",
            NetworkRegistry.ACCEPTVANILLA::equals,
            NetworkRegistry.ACCEPTVANILLA::equals);

        // Forces a client to update an entity's script
        public static SimpleChannel recive_script = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("scriptedmodels", "recive_script"),
            () -> "1",
            NetworkRegistry.ACCEPTVANILLA::equals,
            NetworkRegistry.ACCEPTVANILLA::equals);

        // Remove's a script from an entity
        public static SimpleChannel remove_script = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("scriptedmodels", "remove_script"),
            () -> "1",
            NetworkRegistry.ACCEPTVANILLA::equals,
            NetworkRegistry.ACCEPTVANILLA::equals);

        // Resets the entity's scripts
        public static SimpleChannel reset_entity = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("scriptedmodels", "reset_entity"),
            () -> "1",
            NetworkRegistry.ACCEPTVANILLA::equals,
            NetworkRegistry.ACCEPTVANILLA::equals);
        
        // player connection identifier
        public static SimpleChannel connection = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("scriptedmodels", "connection"),
            () -> "1",
            NetworkRegistry.ACCEPTVANILLA::equals,
            NetworkRegistry.ACCEPTVANILLA::equals);

        // Sends a message to the client
        public static SimpleChannel error = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("scriptedmodels", "error"),
            () -> "1",
            NetworkRegistry.ACCEPTVANILLA::equals,
            NetworkRegistry.ACCEPTVANILLA::equals);
    }

}
