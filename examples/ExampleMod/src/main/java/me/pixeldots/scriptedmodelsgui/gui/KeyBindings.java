package me.pixeldots.scriptedmodelsgui.gui;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;

public class KeyBindings {
	private static KeyMapping GUI = new KeyMapping("key.scriptedmodels.gui", GLFW.GLFW_KEY_R, "category.ScriptedModels");
	
	public static void init(RegisterKeyMappingsEvent event) {
		event.register(GUI);
	}

	public static void tick() {
		if (GUI.consumeClick()) {
			Minecraft.getInstance().setScreen(new ScriptedModelsGUI());
		}
	}

}