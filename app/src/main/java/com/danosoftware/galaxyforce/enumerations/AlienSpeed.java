package com.danosoftware.galaxyforce.enumerations;

public enum AlienSpeed {

    VERY_SLOW(60),
    SLOW(2 * 60),
    MEDIUM(3 * 60),
    FAST(4 * 60),
    VERY_FAST(5 * 60);

    private final int pixelSpeed;

    AlienSpeed(final int pixelSpeed) {
        this.pixelSpeed = pixelSpeed;
    }

    public int getSpeedInPixelsPerSeconds() {
        return pixelSpeed;
    }
}
