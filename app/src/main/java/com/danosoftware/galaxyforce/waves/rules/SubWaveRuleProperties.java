package com.danosoftware.galaxyforce.waves.rules;

import com.danosoftware.galaxyforce.enumerations.Direction;

/*
 * Holds a set of properties that describe a sub-wave (without a path).
 */
public class SubWaveRuleProperties {
    // use random x position
    private final boolean xRandom;

    // use random y position
    private final boolean yRandom;

    // use fixed x start position
    private final int xStart;

    // use fixed y start position
    private final int yStart;

    // total number of aliens in the sub-wave
    private final int numberOfAliens;

    // timing delay between each adjacent alien
    private final float delayBetweenAliens;

    // timing delay before sub-wave starts
    private final float delayOffet;

    // restart alien immediately when it reaches the end of it's path?
    // alternatively will wait until entire in-progress subwave finishes
    private final boolean restartImmediately;

    // alien direction up/down
    private final Direction direction;

    /**
     * Properties to help create a new alien sub-wave using a supplied positions
     * (random or specified) and delays in seconds.
     *
     * @param xRandom
     * @param yRandom
     * @param xStart
     * @param yStart
     * @param numberOfAliens
     * @param delayBetweenAliens
     * @param delayOffet
     * @param restartImmediately
     * @param direction
     */
    public SubWaveRuleProperties(
            boolean xRandom,
            boolean yRandom,
            int xStart,
            int yStart,
            int numberOfAliens,
            float delayBetweenAliens,
            float delayOffet,
            boolean restartImmediately,
            Direction direction) {
        this.xRandom = xRandom;
        this.yRandom = yRandom;
        this.xStart = xStart;
        this.yStart = yStart;
        this.numberOfAliens = numberOfAliens;
        this.delayBetweenAliens = delayBetweenAliens;
        this.delayOffet = delayOffet;
        this.restartImmediately = restartImmediately;
        this.direction = direction;
    }

    public boolean isxRandom() {
        return xRandom;
    }

    public boolean isyRandom() {
        return yRandom;
    }

    public int getxStart() {
        return xStart;
    }

    public int getyStart() {
        return yStart;
    }

    public int getNumberOfAliens() {
        return numberOfAliens;
    }

    public float getDelayBetweenAliens() {
        return delayBetweenAliens;
    }

    public float getDelayOffet() {
        return delayOffet;
    }

    public boolean isRestartImmediately() {
        return restartImmediately;
    }

    public Direction getDirection() {
        return direction;
    }
}
