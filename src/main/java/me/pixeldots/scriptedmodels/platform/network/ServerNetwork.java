package me.pixeldots.scriptedmodels.platform.network;

import java.util.UUID;

import com.google.gson.Gson;

import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain.EntityData;
import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain.NetworkIdentifyers;
import net.minecraft.server.level.ServerPlayer;

public class ServerNetwork {

    public static boolean shouldCompressBytes(String script) {
        return NetworkUtils.shouldCompressBytes(script);
    }
    public static byte[] getBytes(String script) {
        return NetworkUtils.getBytes(script);
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(NetworkIdentifyers.request_entitys, (server, senderplayer, network, buf, sender) -> {
            PacketByteBuf buffer = PacketByteBufs.create();

            buffer.writeInt(ScriptedModelsMain.EntityData.size());
            for (UUID uuid : ScriptedModelsMain.EntityData.keySet()) {
                EntityData data = ScriptedModelsMain.EntityData.get(uuid);
                String entity_data = new Gson().toJson(data);

                buffer.writeUuid(uuid);
                buffer.writeBoolean(shouldCompressBytes(entity_data));
                buffer.writeByteArray(getBytes(entity_data));
            }

            ServerPlayNetworking.send(senderplayer, NetworkIdentifyers.request_entitys, buffer);
        });
        ServerPlayNetworking.registerGlobalReceiver(NetworkIdentifyers.changed_script, (server, senderplayer, network, buf, sender) -> {
            UUID uuid = buf.readUuid();
            int part_id = buf.readInt();

            byte[] byte_script = buf.readByteArray();
            boolean is_compressed = buf.readBoolean();

            String script = (is_compressed ? NetworkUtils.decompress_tostring(byte_script) : new String(byte_script));
            if (ScriptedModelsMain.MaximumScriptLineCount != 0 && script.split("\n").length >= ScriptedModelsMain.MaximumScriptLineCount) {
                send_error(senderplayer, "The sent script has too many lines");
                return;
            }
            
            PacketByteBuf buffer = PacketByteBufs.create();
            buffer.writeUuid(uuid);
            buffer.writeInt(part_id);
            buffer.writeByteArray(byte_script);
            buffer.writeBoolean(is_compressed);

            ScriptedModelsMain.EntityData.put(uuid, new EntityData());
            if (part_id == -1) ScriptedModelsMain.EntityData.get(uuid).script = script;
            else ScriptedModelsMain.EntityData.get(uuid).parts.put(part_id, script);
			
            for (ServerPlayerEntity receiver : server.getPlayerManager().getPlayerList()) {
                ServerPlayNetworking.send(receiver, NetworkIdentifyers.recive_script, buffer);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(NetworkIdentifyers.remove_script, (server, senderplayer, network, buf, sender) -> {
            UUID uuid = buf.readUuid();
            int part_id = buf.readInt();

            if (ScriptedModelsMain.EntityData.containsKey(uuid)) {
                if (part_id != -1) ScriptedModelsMain.EntityData.get(uuid).parts.remove(part_id);
                else ScriptedModelsMain.EntityData.get(uuid).script = "";
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(NetworkIdentifyers.reset_entity, (server, senderplayer, network, buf, sender) -> {
            UUID uuid = buf.readUuid();
            if (ScriptedModelsMain.EntityData.containsKey(uuid)) 
                ScriptedModelsMain.EntityData.remove(uuid);
        });
        ServerPlayNetworking.registerGlobalReceiver(NetworkIdentifyers.connection, (server, senderplayer, network, buf, sender) -> {
            PacketByteBuf buffer = PacketByteBufs.create();

            buffer.writeInt(1);
            EntityData data = ScriptedModelsMain.EntityData.get(senderplayer.getUuid());
            String entity_data = new Gson().toJson(data);

            buffer.writeUuid(senderplayer.getUuid());
            buffer.writeBoolean(shouldCompressBytes(entity_data));
            buffer.writeByteArray(getBytes(entity_data));
			
            for (ServerPlayerEntity receiver : server.getPlayerManager().getPlayerList()) {
                if (receiver == senderplayer) continue;
                ServerPlayNetworking.send(receiver, NetworkIdentifyers.request_entitys, buffer);
            }
        });
    }
    
    public static void send_error(ServerPlayer player, String s) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(s);
        ServerPlayNetworking.send(player, NetworkIdentifyers.error, buf);
    }

}
