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

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public class ScriptedModelsMain implements ModInitializer {

    public static Map<UUID, EntityData> EntityData = new HashMap<>();
    public static int MaximumScriptLineCount = 0;
    public static int CompressThreshold = -1;

    @Override
    public void onInitialize() {
        handle_config();
        ServerNetwork.register();
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
        public static Identifier request_entitys = new Identifier("scriptedmodels", "request_entitys");

        // Updates the servers database with the new entity script
        public static Identifier changed_script = new Identifier("scriptedmodels", "new_scripts");

        // Forces a client to update an entity's script
        public static Identifier recive_script = new Identifier("scriptedmodels", "recive_script");

        // Remove's a script from an entity
        public static Identifier remove_script = new Identifier("scriptedmodels", "remove_script");

        // Resets the entity's scripts
        public static Identifier reset_entity = new Identifier("scriptedmodels", "reset_entity");
        
        // Sends a message to the client
        public static Identifier error = new Identifier("scriptedmodels", "error");
    }

}
