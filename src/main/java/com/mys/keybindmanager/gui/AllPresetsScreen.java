package com.mys.keybindmanager.gui;

import com.mys.keybindmanager.config.KeybindPresetManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;

public class AllPresetsScreen extends Screen {
    private final Screen parent;
    private AllPresetListWidget listWidget;

    public AllPresetsScreen(Screen parent) {
        super(Text.literal("All Keybind Presets"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        refreshList();

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), button -> {
            this.client.setScreen(this.parent);
        }).dimensions(this.width / 2 - 100, this.height - 28, 200, 20).build());
    }

    public void refreshList() {
        if (this.listWidget != null) {
            this.remove(this.listWidget);
        }

        this.listWidget = new AllPresetListWidget(this.client, this.width, this.height - 64, 32, 24);
        this.addSelectableChild(this.listWidget);

        List<String> presets = KeybindPresetManager.getPresetNames();
        for (String preset : presets) {
            this.listWidget.addEntry(new AllPresetEntry(preset));
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        this.listWidget.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 12, 0xFFFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    private class AllPresetListWidget extends ElementListWidget<AllPresetEntry> {
        public AllPresetListWidget(net.minecraft.client.MinecraftClient client, int width, int height, int y, int itemHeight) {
            super(client, width, height, y, itemHeight);
        }

        public void addEntry(AllPresetEntry entry) {
            super.addEntry(entry);
        }
    }

    private class AllPresetEntry extends ElementListWidget.Entry<AllPresetEntry> {
        private final String presetName;
        private final ButtonWidget renameButton;
        private final ButtonWidget updateButton;
        private final ButtonWidget deleteButton;

        public AllPresetEntry(String presetName) {
            this.presetName = presetName;

            this.renameButton = ButtonWidget.builder(Text.literal("Rename"), button -> {
                client.setScreen(new RenamePresetScreen(AllPresetsScreen.this, presetName));
            }).dimensions(0, 0, 50, 20).build();

            this.updateButton = ButtonWidget.builder(Text.literal("Update"), button -> {
                if (KeybindPresetManager.savePreset(presetName)) {
                    if (client.player != null) {
                        client.player.sendMessage(Text.literal("§aPreset '" + presetName + "' updated with current keybinds!"), false);
                    }
                }
            }).dimensions(0, 0, 50, 20).build();

            this.deleteButton = ButtonWidget.builder(Text.literal("§cDelete"), button -> {
                if (KeybindPresetManager.deletePreset(presetName)) {
                    refreshList();
                }
            }).dimensions(0, 0, 50, 20).build();
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawTextWithShadow(client.textRenderer, this.presetName, x + 5, y + 6, 0xFFFFFFFF);

            int btnX = x + entryWidth - 160;
            this.renameButton.setX(btnX);
            this.renameButton.setY(y);

            this.updateButton.setX(btnX + 52);
            this.updateButton.setY(y);

            this.deleteButton.setX(btnX + 104);
            this.deleteButton.setY(y);

            this.renameButton.render(context, mouseX, mouseY, tickDelta);
            this.updateButton.render(context, mouseX, mouseY, tickDelta);
            this.deleteButton.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        public List<? extends net.minecraft.client.gui.Element> children() {
            return Arrays.asList(this.renameButton, this.updateButton, this.deleteButton);
        }

        @Override
        public List<? extends ClickableWidget> selectableChildren() {
            return Arrays.asList(this.renameButton, this.updateButton, this.deleteButton);
        }
    }
}
