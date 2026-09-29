package com.nametweaks.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("nametweaks.json");

    public boolean nameTagPingEnabled = false;
    public boolean pingColorEnabled = true;
    public boolean pingAboveName = false;
    public double pingOffset = -15.0; // range: -30 .. 0
    public boolean nameInF5 = false;

    public static ModConfig load() {
        if (Files.exists(PATH)) {
            try (Reader reader = Files.newBufferedReader(PATH, StandardCharsets.UTF_8)) {
                ModConfig cfg = GSON.fromJson(reader, ModConfig.class);
                if (cfg != null) return cfg;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ModConfig();
    }

    public void save() {
        try {
            Files.createDirectories(PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(PATH, StandardCharsets.UTF_8)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
