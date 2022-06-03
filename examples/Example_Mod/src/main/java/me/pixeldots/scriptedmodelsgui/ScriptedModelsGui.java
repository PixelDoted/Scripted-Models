package me.pixeldots.scriptedmodelsgui;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.pixeldots.scriptedmodelsgui.gui.KeyBindings;

public class ScriptedModelsGui implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("scriptedmodelsgui");
	public static final String ScriptsPath = "/ScriptedModels";

	@Override
	public void onInitializeClient() {
		LOGGER.info("Hello ScriptedModels GUI!");
		KeyBindings.registerKeyBindings();

		Path scriptsPath = Path.of(minecraft.runDirectory.getAbsolutePath() + ScriptsPath);
		if (!Files.exists(scriptsPath)) {
			try {
				Files.createDirectory(scriptsPath);
			} catch (IOException e) {}
		}
	}
}
