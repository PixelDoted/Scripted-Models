package me.pixeldots.scriptedmodels.platform.network.packets;

import java.util.UUID;
import java.util.function.Supplier;

import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class C2S_reset_entity {
    
    public UUID uuid;
    
    public C2S_reset_entity() {}

    public C2S_reset_entity(FriendlyByteBuf in) {
        uuid = in.readUUID();
    }

    public static void handle(C2S_reset_entity msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            UUID uuid = msg.uuid;
            if (ScriptedModelsMain.EntityData.containsKey(uuid)) 
                ScriptedModelsMain.EntityData.remove(uuid);
        });
        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf out) {
        out.writeUUID(uuid);
    }

}