package me.pixeldots.scriptedmodels;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.commands.SetBlockCommand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.pixeldots.scriptedmodels.platform.network.ClientNetwork;
import me.pixeldots.scriptedmodels.platform.network.ScriptedModelsMain;
import me.pixeldots.scriptedmodels.script.ScriptedEntity;

@Mod.EventBusSubscriber(modid = "scriptedmodels", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ScriptedModels {

	public static final Logger LOGGER = LoggerFactory.getLogger("scriptedmodels");

	public static Map<UUID, ScriptedEntity> EntityScript = new HashMap<>();
	public static LivingEntity Rendering_Entity;

	public static Minecraft minecraft;
	public static boolean isConnectedToWorld = false;

	
	@SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
		minecraft = Minecraft.getInstance();
		ClientNetwork.register();

		ClientTickEvents.END_CLIENT_TICK.register(c -> {
			if (c.world == null && isConnectedToWorld) { 
				isConnectedToWorld = false;
				EntityScript.clear();
				ScriptedModelsMain.EntityData.clear();
				Rendering_Entity = null;
			} else if (c.world != null && !isConnectedToWorld) {
				isConnectedToWorld = true;
				ClientNetwork.connection();
			}
		});
		LOGGER.info("Scripted Models Loaded");
	}

}
