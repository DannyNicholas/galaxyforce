package com.danosoftware.galaxyforce.flightpath.paths;

/**
 * Represents the speed of a path and the multiplier to be applied to achieve
 * the path speed.
 */
public enum PathSpeed {

    VERY_SLOW(3f),
    SLOW(2f),
    NORMAL(1f),
    FAST(1f / 2f),
    VERY_FAST(1f / 3f);

    PathSpeed(final float multiplier) {
        this.multiplier = multiplier;
    }

    private final float multiplier;

    public float getMultiplier() {
        return multiplier;
    }
}
