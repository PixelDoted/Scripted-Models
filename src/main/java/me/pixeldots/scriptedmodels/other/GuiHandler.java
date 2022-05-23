package me.pixeldots.scriptedmodels.other;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class GuiHandler extends Screen {
	
	public List<TextFieldWidget> TextFieldWidgets = Lists.newArrayList();
	public List<ButtonWidget> buttons = Lists.newArrayList();
	
	public GuiHandler(String title) {
		super(Text.of(title));
		buttons.clear();
		TextFieldWidgets.clear();
	}
	
	@Override
	public void init() {
		buttons.clear();
		TextFieldWidgets.clear();
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		for (TextFieldWidget widget : TextFieldWidgets) {
			widget.render(matrices, mouseX, mouseY, delta);
		}
		for (ButtonWidget widget : buttons) {
			widget.render(matrices, mouseX, mouseY, delta);
		}
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (TextFieldWidget widget : TextFieldWidgets) {
			widget.mouseClicked(mouseX, mouseY, button);
		}
		for (ButtonWidget widget : buttons) {
			widget.mouseClicked(mouseX, mouseY, button);
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean charTyped(char chr, int keyCode) {
		for (TextFieldWidget widget : TextFieldWidgets) {
			widget.charTyped(chr, keyCode);
		}
		return super.charTyped(chr, keyCode);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		for (TextFieldWidget widget : TextFieldWidgets) {
			widget.keyPressed(keyCode, scanCode, modifiers);
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		for (TextFieldWidget widget : TextFieldWidgets) {
			widget.keyReleased(keyCode, scanCode, modifiers);
		}
		return super.keyReleased(keyCode, scanCode, modifiers);
	}

    @Override
    public boolean shouldPause() {
        return false;
    }
	
	@Override
	public void tick() {
	}
	
	public TextFieldWidget addTextField(TextFieldWidget TextField) {	
		this.TextFieldWidgets.add(TextField);
		return TextField;
	}
	
	public void drawString(MatrixStack matrices, String text, int x, int y, int color) {
		drawCenteredText(matrices, textRenderer, text, x+textRenderer.getWidth(text)/2, y+5, color);
	}
	
	public void drawString(MatrixStack matrices, String text, int x, int y) {
		drawCenteredText(matrices, textRenderer, text, x+textRenderer.getWidth(text)/2, y+5, 16777215);
	}
	
	public ButtonWidget addButton(ButtonWidget button) {
		buttons.add(button);
		return button;
	}

}