package me.pixeldots.scriptedmodels.platform.network;

import java.util.UUID;

import com.google.gson.Gson;

import me.pixeldots.scriptedmodels.ScriptedModels;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain.EntityData;
import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain.NetworkIdentifyers;
import me.pixeldots.scriptedmodels.script.Interpreter;
import me.pixeldots.scriptedmodels.script.ScriptedEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;

@Environment(EnvType.CLIENT)
public class ClientNetwork {

    public static boolean shouldCompressBytes(String script) {
        return NetworkUtils.shouldCompressBytes(script);
    }
    public static byte[] getBytes(String script) {
        return NetworkUtils.getBytes(script);
    }

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(NetworkIdentifyers.request_entitys, (client, handler, buf, responseSender) -> {
            int count = buf.readInt();

            for (int k = 0; k < count; k++) {
                ScriptedEntity scripted = new ScriptedEntity();
                
                UUID uuid = buf.readUuid();
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
        ClientPlayNetworking.registerGlobalReceiver(NetworkIdentifyers.recive_script, (client, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
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

        ClientPlayNetworking.registerGlobalReceiver(NetworkIdentifyers.error, (client, handler, buf, responseSender) -> {
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
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);
        ClientPlayNetworking.send(NetworkIdentifyers.reset_entity, buf);
    }

    public static void remove_script(UUID uuid, int part_id) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);
        buf.writeInt(part_id);
        ClientPlayNetworking.send(NetworkIdentifyers.remove_script, buf);
    }

    public static void changed_script(UUID uuid, int part_id, String script, boolean compress) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);
        buf.writeInt(part_id);
        buf.writeByteArray(getBytes(script));
        buf.writeBoolean(shouldCompressBytes(script));
        ClientPlayNetworking.send(NetworkIdentifyers.changed_script, buf);
    }

    public static void request_entitys() {
        ClientPlayNetworking.send(NetworkIdentifyers.request_entitys, PacketByteBufs.empty());
    }

    public static void connection() {
        request_entitys();
        ClientPlayNetworking.send(NetworkIdentifyers.connection, PacketByteBufs.empty());
    }
    
}
