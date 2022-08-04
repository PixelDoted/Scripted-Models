package me.pixeldots.scriptedmodels.platform.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class Receiver {
    
    public static HashMap<ResourceLocation, ReceivedInstace> SERVER_INSTANCES = new HashMap<>();
    public static HashMap<ResourceLocation, ReceivedInstace> CLIENT_INSTANCES = new HashMap<>();

    public ResourceLocation id;
    public ReceivedBuffer buf;
    public boolean server;

    public Receiver(FriendlyByteBuf in) {
        id = new ResourceLocation(in.readUtf());
        server = in.readBoolean();

        buf = new ReceivedBuffer(in.readByteArray());
    }

    public Receiver(ResourceLocation _id) {
        this.id = _id;
        this.buf = new ReceivedBuffer();
    }

    public Receiver(ResourceLocation _id, ReceivedBuffer _buf) {
        this.id = _id;
        this.buf = _buf;
    }

    public static void handle(Receiver msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (msg.server) {
                if (!SERVER_INSTANCES.containsKey(msg.id)) return;
                SERVER_INSTANCES.get(msg.id).received(sender.level, sender, msg.buf);
            } else {
                if (!CLIENT_INSTANCES.containsKey(msg.id)) return;
                CLIENT_INSTANCES.get(msg.id).received(sender.level, sender, msg.buf);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf out) {
        out.writeUtf(id.toString());
        out.writeBoolean(server);
        out.writeByteArray(buf.array());
    }

    public static void registerGlobalReceiver_Server(ResourceLocation id, ReceivedInstace instance) {
        SERVER_INSTANCES.put(id, instance);
    }
    public static void registerGlobalReceiver_Client(ResourceLocation id, ReceivedInstace instance) {
        CLIENT_INSTANCES.put(id, instance);
    }

    public static void send_fromServer(ServerPlayer to, Receiver data) {
        data.server = false;
        ScriptedModelsMain.SMPacket.sendTo(data, to.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }
    public static void send_fromClient(Receiver data) {
        data.server = true;
        ScriptedModelsMain.SMPacket.sendToServer(data);
    }

    public interface ReceivedInstace {
        void received(Level server, ServerPlayer senderplayer, ReceivedBuffer buf);
    }

    public static class ReceivedBuffer {

        public List<Byte> bytes = new ArrayList<>();
    
        public ReceivedBuffer writeString(String s) {
            return writeByteArray(s.getBytes());
        }
    
        public String readString() {
            return new String(readByteArray());
        }

        public ReceivedBuffer writeInt(int i) {
            bytes.add((byte)(i >> 24));
            bytes.add((byte)(i >> 16));
            bytes.add((byte)(i >> 8));
            bytes.add((byte)i);
            return this;
        }

        public int readInt() {
            return (int)(bytes.remove(0) << 24) + 
                    (int)(bytes.remove(0) << 16) + 
                    (int)(bytes.remove(0) << 8) + 
                    (int)bytes.remove(0);
        }

        public ReceivedBuffer writeBoolean(boolean b) {
            bytes.add(b ? (byte)1 : (byte)0);
            return this;
        }

        public boolean readBoolean() {
            return bytes.remove(0) == 1;
        }

        public ReceivedBuffer writeByteArray(byte[] a) {
            writeInt(a.length);

            for (byte b : a) {
                bytes.add(b);
            }
            return this;
        }

        public byte[] readByteArray() {
            int length = readInt();
            byte[] out = new byte[length];

            for (int i = 0; i < length; i++) {
                out[i] = bytes.remove(0);
            }
            return out;
        }

        public ReceivedBuffer writeUUID(UUID uuid) {
            writeString(uuid.toString());
            return this;
        }

        public UUID readUUID() {
            return UUID.fromString(readString());
        }

        public byte[] array() {
            byte[] out = new byte[bytes.size()];
            for (int i = 0; i < out.length; i++) {
                out[i] = bytes.get(i);
            }
            return out;
        }

        public ReceivedBuffer() {}
        public ReceivedBuffer(byte[] _bytes) {
            for (byte b : _bytes) {
                bytes.add(b);
            }
        }
    
    }

}