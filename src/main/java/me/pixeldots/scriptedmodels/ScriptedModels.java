package me.pixeldots.scriptedmodels;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

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
	public static Map<LivingEntity, EntityModel<?>> EntityModels = new HashMap<>();
	public static LivingEntity Rendering_Entity;

	public static MinecraftClient minecraft;

	@Override
	public void onInitializeClient() {
		LOGGER.info("Hello Fabric world!");
		KeyBindings.registerKeyBindings();
		minecraft = MinecraftClient.getInstance();
	}

}
