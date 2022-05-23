package me.pixeldots.scriptedmodels;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptedModels implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("scriptedmodels");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
	}
}
