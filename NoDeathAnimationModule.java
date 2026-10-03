package com.augustclient.module.movement;

import com.augustclient.module.Module;

public class NoDeathAnimationModule extends Module {
    public NoDeathAnimationModule() {
        super("NoDeathAnimation", "Movement");
    }

    @Override
    public void onTick() {
        // Handled via Mixin/Render hook to remove entity death rotation & delay
    }
}
