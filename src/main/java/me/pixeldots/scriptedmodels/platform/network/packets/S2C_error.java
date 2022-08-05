package me.pixeldots.scriptedmodels.platform.network.packets;

import java.util.function.Supplier;

import me.pixeldots.scriptedmodels.ScriptedModels;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class S2C_error {
    
    public String err;

    public S2C_error() {}

    public S2C_error(FriendlyByteBuf in) {
        err = in.readUtf();
    }

    public static void handle(S2C_error msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            String err = msg.err;
            ScriptedModels.LOGGER.error(err);
        });
        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf out) {
        out.writeUtf(this.err);
    }

}