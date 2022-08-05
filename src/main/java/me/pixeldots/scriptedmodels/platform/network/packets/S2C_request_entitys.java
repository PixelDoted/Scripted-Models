package me.pixeldots.scriptedmodels.platform.network.packets;

import java.util.UUID;
import java.util.function.Supplier;

import com.google.gson.Gson;

import me.pixeldots.scriptedmodels.ScriptedModels;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import me.pixeldots.scriptedmodels.platform.network.ClientNetwork;
import me.pixeldots.scriptedmodels.platform.network.NetworkUtils;
import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain.EntityData;
import me.pixeldots.scriptedmodels.script.Interpreter;
import me.pixeldots.scriptedmodels.script.ScriptedEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

public class S2C_request_entitys {
    
    public int count;
    public UUID[] uuids;
    public boolean[] compressed;
    public byte[][] bytes;

    public S2C_request_entitys() {}

    public S2C_request_entitys(FriendlyByteBuf in) {
        count = in.readInt();
        uuids = new UUID[count];
        compressed = new boolean[count];
        bytes = new byte[count][];

        for (int i = 0; i < count; i++) {
            uuids[i] = in.readUUID();
            compressed[i] = in.readBoolean();
            bytes[i] = in.readByteArray();
        }
    }

    public static void handle(S2C_request_entitys msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            int count = msg.count;

            for (int k = 0; k < count; k++) {
                ScriptedEntity scripted = new ScriptedEntity();
                
                UUID uuid = msg.uuids[k];
                LivingEntity entity = PlatformUtils.getLivingEntity(uuid);
                EntityModel<?> model = PlatformUtils.getModel(entity);
                if (model == null) return;

                boolean is_compressed = msg.compressed[k];
                byte[] bytes = msg.bytes[k];
                EntityData data = new Gson().fromJson((is_compressed ? NetworkUtils.decompress_tostring(bytes) : new String(bytes)), EntityData.class);
                
                if (data.script != null) scripted.global = Interpreter.compile(data.script.split("\n"));

                for (int part_id : data.parts.keySet()) {
                    ModelPart model_part;

                    if (part_id >= 100) model_part = ClientNetwork.getModelPart(part_id-100, PlatformUtils.getHeadParts(model));
                    else model_part = ClientNetwork.getModelPart(part_id, PlatformUtils.getBodyParts(model));

                    scripted.parts.put(model_part, Interpreter.compile(data.parts.get(part_id).split("\n")));
                }

                ScriptedModels.EntityScript.put(uuid, scripted);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf out) {
        out.writeInt(count);
        for (int i = 0; i < count; i++) {
            out.writeUUID(uuids[i]);
            out.writeBoolean(compressed[i]);
            out.writeByteArray(bytes[i]);
        }
    }

}