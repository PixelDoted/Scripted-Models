package me.pixeldots.scriptedmodels.platform.network;

import java.util.UUID;

import me.pixeldots.scriptedmodels.platform.network.packets.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ClientNetwork {

    public static SimpleChannel changed_script = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("scriptedmodels", "c2s_changed_script"),
        () -> ScriptedModelsMain.ProVer, ScriptedModelsMain.ProVer::equals, ScriptedModelsMain.ProVer::equals);
    public static SimpleChannel request_entitys = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("scriptedmodels", "c2s_request_entitys"),
        () -> ScriptedModelsMain.ProVer, ScriptedModelsMain.ProVer::equals, ScriptedModelsMain.ProVer::equals);

    public static SimpleChannel reset_entity = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("scriptedmodels", "c2s_reset_entity"),
        () -> ScriptedModelsMain.ProVer, ScriptedModelsMain.ProVer::equals, ScriptedModelsMain.ProVer::equals);
    public static SimpleChannel remove_script = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("scriptedmodels", "c2s_remove_script"),
        () -> ScriptedModelsMain.ProVer, ScriptedModelsMain.ProVer::equals, ScriptedModelsMain.ProVer::equals);

    public static SimpleChannel connection = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("scriptedmodels", "c2s_connection"),
        () -> ScriptedModelsMain.ProVer, ScriptedModelsMain.ProVer::equals, ScriptedModelsMain.ProVer::equals);

    public static void register(int id) {
        changed_script.registerMessage(id++, C2S_changed_script.class, C2S_changed_script::encode, C2S_changed_script::new, C2S_changed_script::handle);
        request_entitys.registerMessage(id++, C2S_request_entitys.class, C2S_request_entitys::encode, C2S_request_entitys::new, C2S_request_entitys::handle);
        reset_entity.registerMessage(id++, C2S_reset_entity.class, C2S_reset_entity::encode, C2S_reset_entity::new, C2S_reset_entity::handle);
        remove_script.registerMessage(id++, C2S_remove_script.class, C2S_remove_script::encode, C2S_remove_script::new, C2S_remove_script::handle);
        connection.registerMessage(id++, C2S_connection.class, C2S_connection::encode, C2S_connection::new, C2S_connection::handle);
    }

    public static ModelPart getModelPart(int id, Iterable<ModelPart> parts) {
        int index = 0;
        for (ModelPart part : parts) {
            if (index == id) return part;
            index++;
        }
        return null;
    }

    public static void reset_entity(UUID uuid) {
        C2S_reset_entity packet = new C2S_reset_entity();
        packet.uuid = uuid;
        reset_entity.sendToServer(packet);
    }

    public static void remove_script(UUID uuid, int part_id) {
        C2S_remove_script packet = new C2S_remove_script();
        packet.uuid = uuid;
        packet.part_id = part_id;
        remove_script.sendToServer(packet);
    }

    public static void changed_script(UUID uuid, int part_id, String script, boolean compress) {
        C2S_changed_script packet = new C2S_changed_script();
        packet.uuid = uuid;
        packet.part_id = part_id;
        packet.byte_script = NetworkUtils.getBytes(script);
        packet.is_compressed = NetworkUtils.shouldCompressBytes(script);
        changed_script.sendToServer(packet);
    }

    public static void request_entitys() {
        request_entitys.sendToServer(new C2S_request_entitys());
    }

    public static void connection() {
        request_entitys();
        connection.sendToServer(new C2S_connection());
    }
    
}
