package com.augustclient.module.movement;

import com.augustclient.module.Module;

public class NoElytraModule extends Module {
    public NoElytraModule() {
        super("NoElytra", "Movement");
    }

    @Override
    public void onTick() {
        // Logic to cancel Elytra FeatureRenderer on player entity visual model
    }
}
