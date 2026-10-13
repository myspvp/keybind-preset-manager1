package com.mys.keybindmanager;

import com.mys.keybindmanager.config.KeybindPresetManager;
import com.mys.keybindmanager.gui.KeybindManagerMainScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeybindManagerClient implements ClientModInitializer {
    private static KeyBinding openMenuKey;

    @Override
    public void onInitializeClient() {
        KeybindPresetManager.init();

        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybindmanager.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_PERIOD, // '.' (Nokta) Tuşu
                "category.keybindmanager.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new KeybindManagerMainScreen());
                }
            }
        });
    }
}
