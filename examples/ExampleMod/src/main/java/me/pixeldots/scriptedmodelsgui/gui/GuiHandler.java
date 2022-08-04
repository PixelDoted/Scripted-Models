package me.pixeldots.scriptedmodelsgui.gui;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class GuiHandler extends Screen {
	
	public List<EditBox> TextFieldWidgets = new ArrayList<>();
	public List<Button> buttons = new ArrayList<>();
	
	public GuiHandler(String title) {
		super(Component.literal(title));
		buttons.clear();
		TextFieldWidgets.clear();
	}
	
	@Override
	public void init() {
		buttons.clear();
		TextFieldWidgets.clear();
	}
	
	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		for (EditBox widget : TextFieldWidgets) {
			widget.render(matrices, mouseX, mouseY, delta);
		}
		for (Button widget : buttons) {
			widget.render(matrices, mouseX, mouseY, delta);
		}
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (EditBox widget : TextFieldWidgets) {
			widget.mouseClicked(mouseX, mouseY, button);
		}
		for (Button widget : buttons) {
			widget.mouseClicked(mouseX, mouseY, button);
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean charTyped(char chr, int keyCode) {
		for (EditBox widget : TextFieldWidgets) {
			widget.charTyped(chr, keyCode);
		}
		return super.charTyped(chr, keyCode);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		for (EditBox widget : TextFieldWidgets) {
			widget.keyPressed(keyCode, scanCode, modifiers);
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		for (EditBox widget : TextFieldWidgets) {
			widget.keyReleased(keyCode, scanCode, modifiers);
		}
		return super.keyReleased(keyCode, scanCode, modifiers);
	}

    @Override
	public boolean isPauseScreen() {
		return false;
	}
	
	@Override
	public void tick() {
	}
	
	public EditBox addTextField(EditBox TextField) {	
		this.TextFieldWidgets.add(TextField);
		return TextField;
	}
	
	public void drawString(PoseStack matrices, String text, int x, int y, int color) {
		drawCenteredString(matrices, this.font, text, x+this.font.width(text)/2, y+5, color);
	}
	
	public void drawString(PoseStack matrices, String text, int x, int y) {
		drawCenteredString(matrices, this.font, text, x+this.font.width(text)/2, y+5, 16777215);
	}
	
	public Button addButton(Button button) {
		buttons.add(button);
		return button;
	}

}