package com.mys.keybindmanager.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.*;

public class KeybindPresetManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PRESETS_DIR = FabricLoader.getInstance().getConfigDir().resolve("keybind_presets");

    public static void init() {
        File dir = PRESETS_DIR.toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static List<String> getPresetNames() {
        init();
        File dir = PRESETS_DIR.toFile();
        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        List<String> names = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                names.add(name.substring(0, name.length() - 5));
            }
        }
        Collections.sort(names);
        return names;
    }

    public static boolean savePreset(String name) {
        init();
        if (name == null || name.trim().isEmpty()) return false;
        
        MinecraftClient client = MinecraftClient.getInstance();
        Map<String, String> keyData = new HashMap<>();

        for (KeyBinding key : client.options.allKeys) {
            keyData.put(key.getTranslationKey(), key.getBoundKeyTranslationKey());
        }

        File targetFile = PRESETS_DIR.resolve(name + ".json").toFile();
        try (FileWriter writer = new FileWriter(targetFile)) {
            GSON.toJson(keyData, writer);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean loadPreset(String name) {
        File targetFile = PRESETS_DIR.resolve(name + ".json").toFile();
        if (!targetFile.exists()) return false;

        try (FileReader reader = new FileReader(targetFile)) {
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> keyData = GSON.fromJson(reader, type);

            if (keyData == null) return false;

            MinecraftClient client = MinecraftClient.getInstance();
            for (KeyBinding key : client.options.allKeys) {
                if (keyData.containsKey(key.getTranslationKey())) {
                    String boundKeyStr = keyData.get(key.getTranslationKey());
                    InputUtil.Key inputKey = InputUtil.fromTranslationKey(boundKeyStr);
                    key.setBoundKey(inputKey);
                }
            }

            KeyBinding.updateKeysByCode();
            client.options.write();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean renamePreset(String oldName, String newName) {
        File oldFile = PRESETS_DIR.resolve(oldName + ".json").toFile();
        File newFile = PRESETS_DIR.resolve(newName + ".json").toFile();

        if (oldFile.exists() && !newFile.exists()) {
            return oldFile.renameTo(newFile);
        }
        return false;
    }

    public static boolean deletePreset(String name) {
        File targetFile = PRESETS_DIR.resolve(name + ".json").toFile();
        if (targetFile.exists()) {
            return targetFile.delete();
        }
        return false;
    }
}
