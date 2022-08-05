package me.pixeldots.scriptedmodels.platform.network.packets;

import java.util.UUID;
import java.util.function.Supplier;

import me.pixeldots.scriptedmodels.platform.network.ClientNetwork;
import me.pixeldots.scriptedmodels.platform.network.NetworkUtils;
import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain;
import me.pixeldots.scriptedmodels.platform.network.ServerNetwork;
import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain.EntityData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class C2S_changed_script {
    
    public UUID uuid;
    public int part_id;
    public byte[] byte_script;
    public boolean is_compressed;

    public C2S_changed_script() {}

    public C2S_changed_script(FriendlyByteBuf in) {
        uuid = in.readUUID();
        part_id = in.readInt();
        byte_script = in.readByteArray();
        is_compressed = in.readBoolean();
    }

    public static void handle(C2S_changed_script msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();

            UUID uuid = msg.uuid;
            int part_id = msg.part_id;

            byte[] byte_script = msg.byte_script;
            boolean is_compressed = msg.is_compressed;

            String script = (is_compressed ? NetworkUtils.decompress_tostring(byte_script) : new String(byte_script));
            if (ScriptedModelsMain.MaximumScriptLineCount != 0 && script.split("\n").length >= ScriptedModelsMain.MaximumScriptLineCount) {
                ServerNetwork.send_error(sender, "The sent script has too many lines");
                return;
            }
            
            S2C_receive_script buffer = new S2C_receive_script();
            buffer.uuid = uuid;
            buffer.part_id = part_id;
            buffer.byte_script = byte_script;
            buffer.is_compressed = is_compressed;

            if (!ScriptedModelsMain.EntityData.containsKey(uuid))
                ScriptedModelsMain.EntityData.put(uuid, new EntityData());

            if (part_id == -1) ScriptedModelsMain.EntityData.get(uuid).script = script;
            else ScriptedModelsMain.EntityData.get(uuid).parts.put(part_id, script);
			
            for (Player plr : sender.level.players()) {
                ServerPlayer receiver = (ServerPlayer)plr;
                ServerNetwork.receive_script.sendTo(buffer, receiver.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf out) {
        out.writeUUID(this.uuid);
        out.writeInt(this.part_id);
        out.writeByteArray(this.byte_script);
        out.writeBoolean(this.is_compressed);
    }

}
