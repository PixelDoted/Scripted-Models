package me.pixeldots.scriptedmodelsgui;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.pixeldots.scriptedmodelsgui.gui.KeyBindings;

public class ScriptedModelsGui implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("scriptedmodelsgui");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello ScriptedModels GUI!");
		KeyBindings.registerKeyBindings();
	}
}
