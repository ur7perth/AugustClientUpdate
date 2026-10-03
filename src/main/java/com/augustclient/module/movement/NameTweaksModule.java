package com.augustclient.module.movement;

import com.augustclient.module.Module;

public class NameTweaksModule extends Module {
    private boolean nameTabPing = true;
    private boolean pingAboveName = true;
    private boolean nameInF5 = true;

    public NameTweaksModule() {
        super("NameTweaks", "Movement");
    }
}
