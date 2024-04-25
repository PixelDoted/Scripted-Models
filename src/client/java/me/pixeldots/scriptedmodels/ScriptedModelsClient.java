package me.pixeldots.scriptedmodels;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.pixeldots.scriptedmodels.ClientNetwork;
import me.pixeldots.scriptedmodels.script.ScriptedEntity;

public class ScriptedModelsClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("scriptedmodels");

	public static Map<UUID, ScriptedEntity> EntityScript = new HashMap<>();
	public static LivingEntity Rendering_Entity;

	public static MinecraftClient minecraft;
	public static boolean isConnectedToWorld = false;

	@Override
	public void onInitializeClient() {
		// TODO: support versions older then 1.19.x (JOML)

		minecraft = MinecraftClient.getInstance();
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
