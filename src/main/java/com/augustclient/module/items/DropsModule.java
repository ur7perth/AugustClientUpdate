package com.augustclient.module.items;

import com.augustclient.module.Module;

public class DropsModule extends Module {
    private boolean is2d = false;
    private boolean is3d = true;
    private float scale = 1.0f;

    public DropsModule() {
        super("Drops", "Items");
    }
}
