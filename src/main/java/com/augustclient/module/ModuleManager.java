package com.augustclient.module;

import com.augustclient.module.items.*;
import com.augustclient.module.movement.*;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        // Items Category
        modules.add(new OldAnimationModule());
        modules.add(new SwingSpeedModule());
        modules.add(new ItemSwitchModule());
        modules.add(new GlintModule());
        modules.add(new DropsModule());

        // Movement Category
        modules.add(new ToggleSprintModule());
        modules.add(new NameTweaksModule());
        modules.add(new NoFireModule());
        modules.add(new HitColorModule());
        modules.add(new DamageTintModule());
        modules.add(new HitBoxesModule());
        modules.add(new NoDeathAnimationModule());
        modules.add(new NoElytraModule());
    }

    public List<Module> getModules() { return modules; }

    public void onTick(MinecraftClient client) {
        for (Module m : modules) {
            if (m.isEnabled()) {
                m.onTick();
            }
        }
    }
}
