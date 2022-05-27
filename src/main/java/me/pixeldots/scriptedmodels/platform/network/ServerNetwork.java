package me.pixeldots.scriptedmodels.platform.network;

import java.util.UUID;

import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain.EntityData;
import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain.NetworkIdentifyers;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class ServerNetwork {

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(NetworkIdentifyers.request_entitys, (server, senderplayer, network, buf, sender) -> {
            PacketByteBuf buffer = PacketByteBufs.create();

            for (UUID uuid : ScriptedModelsMain.EntityData.keySet()) {
                EntityData data = ScriptedModelsMain.EntityData.get(uuid);
                buffer.writeUuid(uuid); // entity's uuid
                buffer.writeString(data.script); // entity's script

                buffer.writeInt(data.parts.size()); // entity parts data length
                for (int key : data.parts.keySet()) {
                    buffer.writeString(data.parts.get(key)); // entity's modelpart script
                    buffer.writeInt(key); // entity's modelpart id
                }
            }

            ServerPlayNetworking.send(senderplayer, NetworkIdentifyers.request_entitys, buffer);
        });
        ServerPlayNetworking.registerGlobalReceiver(NetworkIdentifyers.changed_script, (server, senderplayer, network, buf, sender) -> {
            UUID uuid = senderplayer.getUuid();
            int part_id = buf.readInt();
            String script = buf.readString();
            if (ScriptedModelsMain.MaximumScriptLineCount != 0 && script.split("\n").length >= ScriptedModelsMain.MaximumScriptLineCount) {
                senderplayer.sendMessage(Text.of("Script has too many lines"), false);
                return;
            }
            
            PacketByteBuf buffer = PacketByteBufs.create();
            buffer.writeUuid(uuid);
            buffer.writeInt(part_id);
            buffer.writeString(script);

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
