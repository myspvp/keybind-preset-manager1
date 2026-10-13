package com.mys.keybindmanager.gui;

import com.mys.keybindmanager.config.KeybindPresetManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class RenamePresetScreen extends Screen {
    private final Screen parent;
    private final String oldName;
    private TextFieldWidget nameField;

    public RenamePresetScreen(Screen parent, String oldName) {
        super(Text.literal("Rename Preset: " + oldName));
        this.parent = parent;
        this.oldName = oldName;
    }

    @Override
    protected void init() {
        int fieldWidth = 200;
        int x = this.width / 2 - fieldWidth / 2;
        int y = this.height / 2 - 10;

        this.nameField = new TextFieldWidget(this.textRenderer, x, y, fieldWidth, 20, Text.literal("New Name"));
        this.nameField.setMaxLength(32);
        this.nameField.setText(oldName);
        this.addSelectableChild(this.nameField);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Save"), button -> {
            String newName = this.nameField.getText().trim();
            if (!newName.isEmpty() && !newName.equals(oldName)) {
                KeybindPresetManager.renamePreset(oldName, newName);
            }
            this.client.setScreen(this.parent);
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
        super.render(context, mouseX, mouseY, delta);
    }
}
