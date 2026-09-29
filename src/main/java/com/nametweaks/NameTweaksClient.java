package com.nametweaks;

import com.nametweaks.config.ModConfig;
import com.nametweaks.gui.NameTweaksScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class NameTweaksClient implements ClientModInitializer {

    public static final String MOD_ID = "nametweaks";
    public static ModConfig CONFIG;

    private static KeyBinding openMenuKey;

    @Override
    public void onInitializeClient() {
        CONFIG = ModConfig.load();

        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nametweaks.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "key.categories.nametweaks"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new NameTweaksScreen(null));
                }
            }
        });
    }
}
