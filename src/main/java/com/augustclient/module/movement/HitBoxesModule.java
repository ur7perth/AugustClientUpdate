package com.augustclient.module.movement;

import com.augustclient.module.Module;

public class HitBoxesModule extends Module {
    private float lineWidth = 1.5f;
    private boolean lookVector = true;
    private int color = 0x0000FF;
    private int targetColor = 0xFF0000;

    public HitBoxesModule() {
        super("HitBoxes", "Movement");
    }
}
