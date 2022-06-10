package me.pixeldots.scriptedmodels.platform.network;

import java.util.UUID;

import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain.EntityData;
import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain.NetworkIdentifyers;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

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

            for (UUID uuid : ScriptedModelsMain.EntityData.keySet()) {
                EntityData data = ScriptedModelsMain.EntityData.get(uuid);
                buffer.writeUuid(uuid); // entity's uuid

                buffer.writeByteArray(getBytes(data.script)); // entity's script
                buffer.writeBoolean(shouldCompressBytes(data.script)); // is script compressed

                buffer.writeInt(data.parts.size()); // entity parts data length
                for (int key : data.parts.keySet()) {
                    String script = data.parts.get(key);
                    buffer.writeByteArray(getBytes(script)); // entity's modelpart script
                    buffer.writeInt(key); // entity's modelpart id
                    buffer.writeBoolean(shouldCompressBytes(script)); // is script compressed
                }
            }

            ServerPlayNetworking.send(senderplayer, NetworkIdentifyers.request_entitys, buffer);
        });
        ServerPlayNetworking.registerGlobalReceiver(NetworkIdentifyers.changed_script, (server, senderplayer, network, buf, sender) -> {
            UUID uuid = senderplayer.getUuid();
            int part_id = buf.readInt();

            byte[] byte_script = buf.readByteArray();
            boolean is_compressed = buf.readBoolean();

            String script = (is_compressed ? NetworkUtils.decompress_tostring(byte_script) : new String(byte_script));
            if (ScriptedModelsMain.MaximumScriptLineCount != 0 && script.split("\n").length >= ScriptedModelsMain.MaximumScriptLineCount) {
                senderplayer.sendMessage(Text.of("Script has too many lines"), false);
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

            String[] names = server.getPlayerManager().getPlayerNames();
            for (int i = 0; i < names.length; i++) {
                ServerPlayNetworking.send(server.getPlayerManager().getPlayer(names[i]), NetworkIdentifyers.recive_script, buffer);
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
    }
    
}
