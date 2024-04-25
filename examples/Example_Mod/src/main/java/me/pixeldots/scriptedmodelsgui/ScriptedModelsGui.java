package me.pixeldotsgui;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import me.pixeldotsgui.gui.KeyBindings;

public class ScriptedModelsGui implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("scriptedmodelsgui");
	public static final String ScriptsPath = "/ScriptedModels";

	@Override
	public void onInitializeClient() {
		LOGGER.info("Hello ScriptedModels GUI!");
		KeyBindings.registerKeyBindings();

		Path scriptsPath = Path.of(MinecraftClient.getInstance().runDirectory.getAbsolutePath() + ScriptsPath);
		if (!Files.exists(scriptsPath)) {
			try {
				Files.createDirectory(scriptsPath);
			} catch (IOException e) {}
		}
	}
}
