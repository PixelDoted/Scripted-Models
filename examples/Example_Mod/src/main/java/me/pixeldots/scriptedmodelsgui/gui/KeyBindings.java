package me.pixeldotsgui.gui;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeyBindings {
	private static KeyBinding GUI = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.scriptedmodels.gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "category.ScriptedModels"));
	
	public static void registerKeyBindings() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
		    if (GUI.wasPressed()) {
                MinecraftClient.getInstance().setScreen(new ScriptedModelsGUI());
		    }
		});
	}

}