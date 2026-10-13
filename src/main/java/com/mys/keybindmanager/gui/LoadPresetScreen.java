package com.mys.keybindmanager.gui;

import com.mys.keybindmanager.config.KeybindPresetManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.Collections;
import java.util.List;

public class LoadPresetScreen extends Screen {
    private final Screen parent;
    private PresetListWidget listWidget;

    public LoadPresetScreen(Screen parent) {
        super(Text.literal("Load Keybind Preset"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.listWidget = new PresetListWidget(this.client, this.width, this.height - 64, 32, 24);
        this.addSelectableChild(this.listWidget);

        List<String> presets = KeybindPresetManager.getPresetNames();
        for (String preset : presets) {
            this.listWidget.addEntry(new PresetEntry(preset));
        }

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), button -> {
            this.client.setScreen(this.parent);
        }).dimensions(this.width / 2 - 100, this.height - 28, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        this.listWidget.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 12, 0xFFFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    private class PresetListWidget extends ElementListWidget<PresetEntry> {
        public PresetListWidget(net.minecraft.client.MinecraftClient client, int width, int height, int y, int itemHeight) {
            super(client, width, height, y, itemHeight);
        }

        public void addEntry(PresetEntry entry) {
            super.addEntry(entry);
        }
    }

    private class PresetEntry extends ElementListWidget.Entry<PresetEntry> {
        private final String presetName;
        private final ButtonWidget loadButton;

        public PresetEntry(String presetName) {
            this.presetName = presetName;
            this.loadButton = ButtonWidget.builder(Text.literal("LOAD"), button -> {
                if (KeybindPresetManager.loadPreset(presetName)) {
                    if (client.player != null) {
                        client.player.sendMessage(Text.literal("§aSuccessfully loaded profile: " + presetName), false);
                    }
                    client.setScreen(parent);
                }
            }).dimensions(0, 0, 60, 20).build();
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawTextWithShadow(client.textRenderer, this.presetName, x + 10, y + 6, 0xFFFFFFFF);
            this.loadButton.setX(x + entryWidth - 70);
            this.loadButton.setY(y);
            this.loadButton.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        public List<? extends net.minecraft.client.gui.Element> children() {
            return Collections.singletonList(this.loadButton);
        }

        @Override
        public List<? extends ClickableWidget> selectableChildren() {
            return Collections.singletonList(this.loadButton);
        }
    }
}
