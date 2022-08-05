package me.pixeldots.scriptedmodels.platform.network.packets;

import java.util.UUID;
import java.util.function.Supplier;

import com.google.gson.Gson;

import me.pixeldots.scriptedmodels.platform.network.NetworkUtils;
import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain;
import me.pixeldots.scriptedmodels.platform.network.ServerNetwork;
import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain.EntityData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class C2S_request_entitys {
    
    public C2S_request_entitys() {}

    public C2S_request_entitys(FriendlyByteBuf in) {
    }

    public static void handle(C2S_request_entitys msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            S2C_request_entitys buffer = new S2C_request_entitys();

            buffer.count = ScriptedModelsMain.EntityData.size();
            buffer.uuids = new UUID[buffer.count];
            buffer.compressed = new boolean[buffer.count];
            buffer.bytes = new byte[buffer.count][];

            int index = 0;
            for (UUID uuid : ScriptedModelsMain.EntityData.keySet()) {
                EntityData data = ScriptedModelsMain.EntityData.get(uuid);
                String entity_data = new Gson().toJson(data);

                buffer.uuids[index] = uuid;
                buffer.compressed[index] = NetworkUtils.shouldCompressBytes(entity_data);
                buffer.bytes[index] = NetworkUtils.getBytes(entity_data);
                index += 1;
            }

            ServerNetwork.request_entitys.sendTo(buffer, ctx.get().getSender().connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
        });
        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf out) {
    }

}