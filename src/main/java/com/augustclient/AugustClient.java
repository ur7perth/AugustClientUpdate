package com.augustclient;

import com.augustclient.gui.AugustClickGuiScreen;
import com.augustclient.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AugustClient implements ClientModInitializer {
    public static final String MOD_ID = "augustclient";
    public static KeyBinding openGuiKey;
    public static ModuleManager moduleManager;

    @Override
    public void onInitializeClient() {
        moduleManager = new ModuleManager();

        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.augustclient.opengui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.augustclient"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new AugustClickGuiScreen());
                }
            }
            if (client.player != null) {
                moduleManager.onTick(client);
            }
        });
    }
}
