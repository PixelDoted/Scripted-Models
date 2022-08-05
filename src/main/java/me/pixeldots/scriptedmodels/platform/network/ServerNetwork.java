package me.pixeldots.scriptedmodels.platform.network;

import me.pixeldots.scriptedmodels.platform.network.packets.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ServerNetwork {

    public static SimpleChannel request_entitys = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("scriptedmodels", "s2c_request_entitys"),
        () -> ScriptedModelsMain.ProVer, ScriptedModelsMain.ProVer::equals, ScriptedModelsMain.ProVer::equals);
    public static SimpleChannel receive_script = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("scriptedmodels", "s2c_receive_script"),
        () -> ScriptedModelsMain.ProVer, ScriptedModelsMain.ProVer::equals, ScriptedModelsMain.ProVer::equals);
    
    public static SimpleChannel error = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("scriptedmodels", "s2c_error"),
        () -> ScriptedModelsMain.ProVer, ScriptedModelsMain.ProVer::equals, ScriptedModelsMain.ProVer::equals);

    public static void register(int id) {
        request_entitys.registerMessage(id++, S2C_request_entitys.class, S2C_request_entitys::encode, S2C_request_entitys::new, S2C_request_entitys::handle);
        receive_script.registerMessage(id++, S2C_receive_script.class, S2C_receive_script::encode, S2C_receive_script::new, S2C_receive_script::handle);
        error.registerMessage(id++, S2C_error.class, S2C_error::encode, S2C_error::new, S2C_error::handle);
    }
    
    public static void send_error(ServerPlayer player, String s) {
        S2C_error packet = new S2C_error();
        packet.err = s;
        error.sendTo(packet, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);

    }

}
