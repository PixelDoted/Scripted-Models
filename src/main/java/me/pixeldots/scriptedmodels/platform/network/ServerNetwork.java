package me.pixeldots.scriptedmodels.platform.network;

import java.util.UUID;

import com.google.gson.Gson;

import io.netty.buffer.Unpooled;
import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain.EntityData;
import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain.NetworkIdentifyers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ServerNetwork {

    public static boolean shouldCompressBytes(String script) {
        return NetworkUtils.shouldCompressBytes(script);
    }
    public static byte[] getBytes(String script) {
        return NetworkUtils.getBytes(script);
    }

    public static void register() {
        Receiver.registerGlobalReceiver_Server(NetworkIdentifyers.request_entitys, (server, senderplayer, buf) -> {
            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());

            buffer.writeInt(ScriptedModelsMain.EntityData.size());
            for (UUID uuid : ScriptedModelsMain.EntityData.keySet()) {
                EntityData data = ScriptedModelsMain.EntityData.get(uuid);
                String entity_data = new Gson().toJson(data);

                buffer.writeUUID(uuid);
                buffer.writeBoolean(shouldCompressBytes(entity_data));
                buffer.writeByteArray(getBytes(entity_data));
            }

            Receiver packet = new Receiver(NetworkIdentifyers.request_entitys, buffer);
            Receiver.send_fromServer(senderplayer, packet);
        });
        Receiver.registerGlobalReceiver_Server(NetworkIdentifyers.changed_script, (server, senderplayer, buf) -> {
            UUID uuid = buf.readUUID();
            int part_id = buf.readInt();

            byte[] byte_script = buf.readByteArray();
            boolean is_compressed = buf.readBoolean();

            String script = (is_compressed ? NetworkUtils.decompress_tostring(byte_script) : new String(byte_script));
            if (ScriptedModelsMain.MaximumScriptLineCount != 0 && script.split("\n").length >= ScriptedModelsMain.MaximumScriptLineCount) {
                send_error(senderplayer, "The sent script has too many lines");
                return;
            }
            
            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
            buffer.writeUUID(uuid);
            buffer.writeInt(part_id);
            buffer.writeByteArray(byte_script);
            buffer.writeBoolean(is_compressed);

            ScriptedModelsMain.EntityData.put(uuid, new EntityData());
            if (part_id == -1) ScriptedModelsMain.EntityData.get(uuid).script = script;
            else ScriptedModelsMain.EntityData.get(uuid).parts.put(part_id, script);
			
            for (Player plr : server.players()) {
                ServerPlayer receiver = (ServerPlayer)plr;
                Receiver packet = new Receiver(NetworkIdentifyers.recive_script, buffer);
                Receiver.send_fromServer(receiver, packet);
            }
        });
        Receiver.registerGlobalReceiver_Server(NetworkIdentifyers.remove_script, (server, senderplayer, buf) -> {
            UUID uuid = buf.readUUID();
            int part_id = buf.readInt();

            if (ScriptedModelsMain.EntityData.containsKey(uuid)) {
                if (part_id != -1) ScriptedModelsMain.EntityData.get(uuid).parts.remove(part_id);
                else ScriptedModelsMain.EntityData.get(uuid).script = "";
            }
        });
        Receiver.registerGlobalReceiver_Server(NetworkIdentifyers.reset_entity, (server, senderplayer, buf) -> {
            UUID uuid = buf.readUUID();
            if (ScriptedModelsMain.EntityData.containsKey(uuid)) 
                ScriptedModelsMain.EntityData.remove(uuid);
        });
        Receiver.registerGlobalReceiver_Server(NetworkIdentifyers.connection, (server, senderplayer, buf) -> {
            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());

            buffer.writeInt(1);
            EntityData data = ScriptedModelsMain.EntityData.get(senderplayer.getUUID());
            String entity_data = new Gson().toJson(data);

            buffer.writeUUID(senderplayer.getUUID());
            buffer.writeBoolean(shouldCompressBytes(entity_data));
            buffer.writeByteArray(getBytes(entity_data));
			
            for (Player plr : server.players()) {
                ServerPlayer receiver = (ServerPlayer)plr;
                if (receiver == senderplayer) continue;

                Receiver packet = new Receiver(NetworkIdentifyers.request_entitys, buffer);
                Receiver.send_fromServer(receiver, packet);
            }
        });
    }
    
    public static void send_error(ServerPlayer player, String s) {
        Receiver data = new Receiver(NetworkIdentifyers.error);
        data.buf.writeUtf(s);
        Receiver.send_fromServer(player, data);
    }

}
