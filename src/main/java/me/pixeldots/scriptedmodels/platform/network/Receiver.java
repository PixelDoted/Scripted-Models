package me.pixeldots.scriptedmodels.platform.network;

import java.util.HashMap;
import java.util.function.Supplier;

import com.google.common.base.Suppliers;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class Receiver {
    
    public static HashMap<ResourceLocation, ReceivedInstace> SERVER_INSTANCES = new HashMap<>();
    public static HashMap<ResourceLocation, ReceivedInstace> CLIENT_INSTANCES = new HashMap<>();

    public ResourceLocation id;
    public FriendlyByteBuf buf;
    public boolean server;

    public Receiver(FriendlyByteBuf in) {
        id = new ResourceLocation(in.readUtf());

        FriendlyByteBuf _buf = new FriendlyByteBuf(Unpooled.buffer());
        _buf.setBytes(0, in.readByteArray());
        buf = _buf;

        server = in.readBoolean();
    }

    public Receiver(ResourceLocation _id) {
        this.id = _id;
        this.buf = new FriendlyByteBuf(Unpooled.buffer());
    }

    public Receiver(ResourceLocation _id, FriendlyByteBuf _buf) {
        this.id = _id;
        this.buf = _buf;
    }

    public static void handle(Receiver msg, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayer sender = ctx.get().getSender();
        if (msg.server) {
            if (!SERVER_INSTANCES.containsKey(msg.id)) return;
            SERVER_INSTANCES.get(msg.id).received(sender.level, sender, msg.buf);
        } else {
            if (!CLIENT_INSTANCES.containsKey(msg.id)) return;
            CLIENT_INSTANCES.get(msg.id).received(sender.level, sender, msg.buf);
        }
    }

    public void encode(FriendlyByteBuf out) {
        out.writeUtf(id.toString());
        out.writeByteArray(buf.array());
        out.writeBoolean(server);
    }

    public static void registerGlobalReceiver_Server(ResourceLocation id, ReceivedInstace instance) {
        SERVER_INSTANCES.put(id, instance);
    }
    public static void registerGlobalReceiver_Client(ResourceLocation id, ReceivedInstace instance) {
        CLIENT_INSTANCES.put(id, instance);
    }

    public static void send_fromServer(ServerPlayer to, Receiver data) {
        data.server = false;
        ScriptedModelsMain.SMPacket.send(PacketDistributor.PLAYER.with(Suppliers.ofInstance(to)), data);
    }
    public static void send_fromClient(Receiver data) {
        data.server = true;
        ScriptedModelsMain.SMPacket.send(PacketDistributor.SERVER.with(Suppliers.ofInstance(null)), data);
    }

    public interface ReceivedInstace {
        void received(Level server, ServerPlayer senderplayer, FriendlyByteBuf buf);
    }

}
