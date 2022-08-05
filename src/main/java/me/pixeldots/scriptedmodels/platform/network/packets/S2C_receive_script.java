package me.pixeldots.scriptedmodels.platform.network.packets;

import java.util.UUID;
import java.util.function.Supplier;

import me.pixeldots.scriptedmodels.ScriptedModels;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import me.pixeldots.scriptedmodels.platform.network.ClientNetwork;
import me.pixeldots.scriptedmodels.platform.network.NetworkUtils;
import me.pixeldots.scriptedmodels.script.Interpreter;
import me.pixeldots.scriptedmodels.script.ScriptedEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

public class S2C_receive_script {
    
    public UUID uuid;
    public int part_id;
    public byte[] byte_script;
    public boolean is_compressed;

    public S2C_receive_script() {}

    public S2C_receive_script(FriendlyByteBuf in) {
        uuid = in.readUUID();
        part_id = in.readInt();
        byte_script = in.readByteArray();
        is_compressed = in.readBoolean();
    }

    public static void handle(S2C_receive_script msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            UUID uuid = msg.uuid;
            int part_id = msg.part_id;
            byte[] byte_script = msg.byte_script;
            boolean is_compressed = msg.is_compressed;

            String script = (is_compressed ? NetworkUtils.decompress_tostring(byte_script) : new String(byte_script));

            if (!ScriptedModels.EntityScript.containsKey(uuid)) ScriptedModels.EntityScript.put(uuid, new ScriptedEntity());
            if (part_id == -1) ScriptedModels.EntityScript.get(uuid).global = Interpreter.compile(script.split("\n"));
            else {
                LivingEntity entity = PlatformUtils.getLivingEntity(uuid);
                EntityModel<?> model = PlatformUtils.getModel(entity);
                if (model == null) return;

                ModelPart model_part;
                if (part_id >= 100) model_part = ClientNetwork.getModelPart(part_id-100, PlatformUtils.getHeadParts(model));
                else model_part = ClientNetwork.getModelPart(part_id, PlatformUtils.getBodyParts(model));

                if (!ScriptedModels.EntityScript.containsKey(uuid)) ScriptedModels.EntityScript.put(uuid, new ScriptedEntity());
                ScriptedModels.EntityScript.get(uuid).parts.put(model_part, Interpreter.compile(script.split("\n")));
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf out) {
        out.writeUUID(uuid);
        out.writeInt(part_id);
        out.writeByteArray(byte_script);
        out.writeBoolean(is_compressed);
    }

}