package me.pixeldots.scriptedmodels;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
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
    public void onClientSetup(FMLClientSetupEvent event) {
		ClientNetwork.register();
		LOGGER.info("Scripted Models Loaded");
	}

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END && event.side == LogicalSide.CLIENT) {
			if (minecraft == null) {
				minecraft = Minecraft.getInstance();
				return;
			}

			if (minecraft.level == null && isConnectedToWorld) { 
				isConnectedToWorld = false;
				EntityScript.clear();
				ScriptedModelsMain.EntityData.clear();
				Rendering_Entity = null;
			} else if (minecraft.level != null && !isConnectedToWorld) {
				isConnectedToWorld = true;
				ClientNetwork.connection();
			}
		}
	}

}
