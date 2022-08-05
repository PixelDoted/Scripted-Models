package me.pixeldots.scriptedmodels.platform.network.packets;

import java.util.UUID;
import java.util.function.Supplier;

import com.google.gson.Gson;

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

public class C2S_connection {
    
    public C2S_connection() {}

    public C2S_connection(FriendlyByteBuf in) {
    }

    public static void handle(C2S_connection msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer senderplayer = ctx.get().getSender();
            S2C_request_entitys buffer = new S2C_request_entitys();

            buffer.count = 1;
            EntityData data = ScriptedModelsMain.EntityData.get(senderplayer.getUUID());
            String entity_data = new Gson().toJson(data);

            buffer.uuids = new UUID[] {senderplayer.getUUID()};
            buffer.compressed = new boolean[] {NetworkUtils.shouldCompressBytes(entity_data)};
            buffer.bytes = new byte[][] {NetworkUtils.getBytes(entity_data)};
			
            for (Player plr : senderplayer.level.players()) {
                ServerPlayer receiver = (ServerPlayer)plr;
                if (receiver == senderplayer) continue;
                
                ServerNetwork.request_entitys.sendTo(buffer, receiver.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf out) {
    }

}