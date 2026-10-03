package com.augustclient.module.items;

import com.augustclient.module.Module;

public class SwingSpeedModule extends Module {
    private int speed = 10;

    public SwingSpeedModule() {
        super("SwingSpeed", "Items");
    }

    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }
}
