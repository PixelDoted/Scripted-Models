package me.pixeldots.scriptedmodels.platform.network;

import java.util.UUID;

import com.google.gson.Gson;

import me.pixeldots.scriptedmodels.ScriptedModels;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain.EntityData;
import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain.NetworkIdentifyers;
import me.pixeldots.scriptedmodels.script.Interpreter;
import me.pixeldots.scriptedmodels.script.ScriptedEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

public class ClientNetwork {

    public static boolean shouldCompressBytes(String script) {
        return NetworkUtils.shouldCompressBytes(script);
    }
    public static byte[] getBytes(String script) {
        return NetworkUtils.getBytes(script);
    }

    public static void register() {
        Receiver.registerGlobalReceiver_Client(NetworkIdentifyers.request_entitys, (server, senderplayer, buf) -> {
            int count = buf.readInt();

            for (int k = 0; k < count; k++) {
                ScriptedEntity scripted = new ScriptedEntity();
                
                UUID uuid = buf.readUUID();
                LivingEntity entity = PlatformUtils.getLivingEntity(uuid);
                EntityModel<?> model = PlatformUtils.getModel(entity);
                if (model == null) return;

                boolean is_compressed = buf.readBoolean();
                byte[] bytes = buf.readByteArray();
                EntityData data = new Gson().fromJson((is_compressed ? NetworkUtils.decompress_tostring(bytes) : new String(bytes)), EntityData.class);
                
                if (data.script != null) scripted.global = Interpreter.compile(data.script.split("\n"));

                for (int part_id : data.parts.keySet()) {
                    ModelPart model_part;

                    if (part_id >= 100) model_part = getModelPart(part_id-100, PlatformUtils.getHeadParts(model));
                    else model_part = getModelPart(part_id, PlatformUtils.getBodyParts(model));

                    scripted.parts.put(model_part, Interpreter.compile(data.parts.get(part_id).split("\n")));
                }

                ScriptedModels.EntityScript.put(uuid, scripted);
            }
        });
        Receiver.registerGlobalReceiver_Client(NetworkIdentifyers.recive_script, (server, senderplayer, buf) -> {
            UUID uuid = buf.readUUID();
            int part_id = buf.readInt();
            byte[] byte_script = buf.readByteArray();
            boolean is_compressed = buf.readBoolean();

            String script = (is_compressed ? NetworkUtils.decompress_tostring(byte_script) : new String(byte_script));

            if (!ScriptedModels.EntityScript.containsKey(uuid)) ScriptedModels.EntityScript.put(uuid, new ScriptedEntity());
            if (part_id == -1) ScriptedModels.EntityScript.get(uuid).global = Interpreter.compile(script.split("\n"));
            else {
                LivingEntity entity = PlatformUtils.getLivingEntity(uuid);
                EntityModel<?> model = PlatformUtils.getModel(entity);
                if (model == null) return;

                ModelPart model_part;
                if (part_id >= 100) model_part = getModelPart(part_id-100, PlatformUtils.getHeadParts(model));
                else model_part = getModelPart(part_id, PlatformUtils.getBodyParts(model));

                if (!ScriptedModels.EntityScript.containsKey(uuid)) ScriptedModels.EntityScript.put(uuid, new ScriptedEntity());
                ScriptedModels.EntityScript.get(uuid).parts.put(model_part, Interpreter.compile(script.split("\n")));
            }
        });

        Receiver.registerGlobalReceiver_Client(NetworkIdentifyers.error, (server, senderplayer, buf) -> {
            String err = buf.readString();
            ScriptedModels.LOGGER.error(err);
        });
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
        Receiver packet = new Receiver(NetworkIdentifyers.reset_entity);
        packet.buf.writeUUID(uuid);
        Receiver.send_fromClient(packet);
    }

    public static void remove_script(UUID uuid, int part_id) {
        Receiver packet = new Receiver(NetworkIdentifyers.remove_script);
        packet.buf.writeUUID(uuid);
        packet.buf.writeInt(part_id);
        Receiver.send_fromClient(packet);
    }

    public static void changed_script(UUID uuid, int part_id, String script, boolean compress) {
        Receiver packet = new Receiver(NetworkIdentifyers.changed_script);
        packet.buf.writeUUID(uuid);
        packet.buf.writeInt(part_id);
        packet.buf.writeByteArray(getBytes(script));
        packet.buf.writeBoolean(shouldCompressBytes(script));
        Receiver.send_fromClient(packet);
    }

    public static void request_entitys() {
        Receiver.send_fromClient(new Receiver(NetworkIdentifyers.request_entitys));
    }

    public static void connection() {
        request_entitys();
        Receiver.send_fromClient(new Receiver(NetworkIdentifyers.connection));
    }
    
}
