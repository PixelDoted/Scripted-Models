package me.pixeldots.scriptedmodelsgui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import me.pixeldots.scriptedmodelsgui.gui.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod("scriptedmodelsgui")
public class ScriptedModelsGui {
	public static final Logger LOGGER = LoggerFactory.getLogger("scriptedmodelsgui");
	public static final String ScriptsPath = "/ScriptedModels";

	public ScriptedModelsGui() {
        MinecraftForge.EVENT_BUS.register(new ClientModEvents());
    }


	@Mod.EventBusSubscriber(modid = "scriptedmodelsgui", bus = Mod.EventBusSubscriber.Bus.MOD)
	public class ClientModEvents {

		@SubscribeEvent
		public void onClientSetup(FMLClientSetupEvent event) {
			LOGGER.info("Hello ScriptedModels GUI!");

			Path scriptsPath = Path.of(Minecraft.getInstance().gameDirectory.getAbsolutePath() + ScriptsPath);
			if (!Files.exists(scriptsPath)) {
				try {
					Files.createDirectory(scriptsPath);
				} catch (IOException e) {}
			}
		}

		@SubscribeEvent
		public void onClientTick(ClientTickEvent event) {
			KeyBindings.tick();
		}

		@SubscribeEvent
		public void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
			KeyBindings.init(event);
		}

	}
}
