package com.danosoftware.galaxyforce.enumerations;

public enum AlienMissileSpeed {

    VERY_SLOW(1 * 60),
    SLOW(3 * 60),
    MEDIUM(5 * 60),
    FAST(7 * 60),
    VERY_FAST(9 * 60);

    // missile speed in pixels per second
    private final int speed;

    AlienMissileSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }
}
