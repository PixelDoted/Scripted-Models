package me.pixeldots.scriptedmodels;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.pixeldots.scriptedmodels.platform.other.KeyBindings;
import me.pixeldots.scriptedmodels.script.ScriptedEntity;

public class ScriptedModels implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("scriptedmodels");
	public static final String ScriptsPath = "/ScriptedModels";

	public static Map<UUID, ScriptedEntity> EntityScript = new HashMap<>();
	public static LivingEntity Rendering_Entity;

	public static MinecraftClient minecraft;
	public static ClientPlayerEntity clientPlayer;
	public static boolean isConnectedToWorld = false;

	@Override
	public void onInitializeClient() {
		KeyBindings.registerKeyBindings();
		minecraft = MinecraftClient.getInstance();

		Path scriptsPath = Path.of(minecraft.runDirectory.getAbsolutePath() + ScriptsPath);
		if (!Files.exists(scriptsPath)) {
			try {
				Files.createDirectory(scriptsPath);
			} catch (IOException e) {}
		}

		ClientTickEvents.END_CLIENT_TICK.register(c -> {
			if (c.player == null && isConnectedToWorld) { 
				isConnectedToWorld = false;
				EntityScript.clear();
				Rendering_Entity = null;
			} else if (c.player != null && !isConnectedToWorld) isConnectedToWorld = true;
		});
		LOGGER.info("Scripted Models Loaded");
	}

}
