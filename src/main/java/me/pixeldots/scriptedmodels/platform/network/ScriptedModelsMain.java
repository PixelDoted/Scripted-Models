package me.pixeldots.scriptedmodels.platform.network;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public class ScriptedModelsMain implements ModInitializer {

    public static Map<UUID, EntityData> EntityData = new HashMap<>();

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            ServerNetwork.register();
        } else {
            ClientNetwork.register();
        }
    }


    @Environment(EnvType.SERVER)
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
    }

}
