package com.mys.keybindmanager.gui;

import com.mys.keybindmanager.config.KeybindPresetManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class CreatePresetScreen extends Screen {
    private final Screen parent;
    private TextFieldWidget nameField;
    private Text errorMessage = null;

    public CreatePresetScreen(Screen parent) {
        super(Text.literal("Create New Keybind Preset"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int fieldWidth = 200;
        int x = this.width / 2 - fieldWidth / 2;
        int y = this.height / 2 - 10;

        this.nameField = new TextFieldWidget(this.textRenderer, x, y, fieldWidth, 20, Text.literal("Preset Name"));
        this.nameField.setMaxLength(32);
        this.nameField.setPlaceholder(Text.literal("e.g. AxePvP, UHC"));
        this.addSelectableChild(this.nameField);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Save Preset"), button -> {
            String presetName = this.nameField.getText().trim();
            if (presetName.isEmpty()) {
                this.errorMessage = Text.literal("§cPreset name cannot be empty!");
                return;
            }

            if (KeybindPresetManager.savePreset(presetName)) {
                if (this.client.player != null) {
                    this.client.player.sendMessage(Text.literal("§aKeybind preset '" + presetName + "' saved successfully!"), false);
                }
                this.client.setScreen(this.parent);
            } else {
                this.errorMessage = Text.literal("§cFailed to save preset!");
            }
        }).dimensions(x, y + 30, 95, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Cancel"), button -> {
            this.client.setScreen(this.parent);
        }).dimensions(x + 105, y + 30, 95, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 40, 0xFFFFFFFF);
        this.nameField.render(context, mouseX, mouseY, delta);

        if (this.errorMessage != null) {
            context.drawCenteredTextWithShadow(this.textRenderer, this.errorMessage, this.width / 2, this.height / 2 - 30, 0xFFFF5555);
        }

        super.render(context, mouseX, mouseY, delta);
    }
}
