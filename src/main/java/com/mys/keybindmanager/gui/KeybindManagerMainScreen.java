package com.mys.keybindmanager.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class KeybindManagerMainScreen extends Screen {

    public KeybindManagerMainScreen() {
        super(Text.literal("Keybind Preset Manager"));
    }

    @Override
    protected void init() {
        int buttonWidth = 200;
        int buttonHeight = 20;
        int startY = this.height / 2 - 35;
        int x = this.width / 2 - buttonWidth / 2;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("+ Create New Keybinds!"), button -> {
            this.client.setScreen(new CreatePresetScreen(this));
        }).dimensions(x, startY, buttonWidth, buttonHeight).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Load Keybinds!"), button -> {
            this.client.setScreen(new LoadPresetScreen(this));
        }).dimensions(x, startY + 25, buttonWidth, buttonHeight).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("All Keybinds!"), button -> {
            this.client.setScreen(new AllPresetsScreen(this));
        }).dimensions(x, startY + 50, buttonWidth, buttonHeight).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
