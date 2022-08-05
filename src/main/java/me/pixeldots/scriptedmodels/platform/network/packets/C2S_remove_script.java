package me.pixeldots.scriptedmodels.platform.network.packets;

import java.util.UUID;
import java.util.function.Supplier;

import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class C2S_remove_script {
    
    public UUID uuid;
    public int part_id;

    public C2S_remove_script() {}

    public C2S_remove_script(FriendlyByteBuf in) {
        uuid = in.readUUID();
        part_id = in.readInt();
    }

    public static void handle(C2S_remove_script msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            UUID uuid = msg.uuid;
            int part_id = msg.part_id;

            if (ScriptedModelsMain.EntityData.containsKey(uuid)) {
                if (part_id != -1) ScriptedModelsMain.EntityData.get(uuid).parts.remove(part_id);
                else ScriptedModelsMain.EntityData.get(uuid).script = "";
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf out) {
        out.writeUUID(uuid);
        out.writeInt(part_id);
    }

}