package com.danosoftware.galaxyforce.legacy.waves;

import com.danosoftware.galaxyforce.legacy.flightpath.Path;
import com.danosoftware.galaxyforce.waves.AlienType;

/*
 * Class references a sub-wave of aliens on a path
 */
public class SubWaveProperty {
    private AlienType alienType = null;
    private Path path = null;
    private boolean xInvert;
    private boolean yInvert;
    private int numberOfAliens;
    private float delayBetweenAliens;
    private float delayOffet;
    private int xOffset;
    private int yOffset;
    private boolean restartImmediately;

    /**
     * Create a new alien sub-wave using a supplied path, positional offsets and
     * delays in seconds.
     *
     * @param alienType
     * @param path
     * @param xInvert
     * @param yInvert
     * @param xOffset
     * @param yOffset
     * @param numberOfAliens
     * @param delayBetweenAliens
     * @param delayOffet
     */
    public SubWaveProperty(AlienType alienType, Path path, boolean xInvert, boolean yInvert, int xOffset, int yOffset, int numberOfAliens,
                           float delayBetweenAliens, float delayOffet, boolean restartImmediately) {
        this.alienType = alienType;
        this.path = path;
        this.xInvert = xInvert;
        this.yInvert = yInvert;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.numberOfAliens = numberOfAliens;
        this.delayBetweenAliens = delayBetweenAliens;
        this.delayOffet = delayOffet;
        this.restartImmediately = restartImmediately;
    }

    public AlienType getAlienType() {
        return alienType;
    }

    public Path getPath() {
        return path;
    }

    public boolean isxInvert() {
        return xInvert;
    }

    public boolean isyInvert() {
        return yInvert;
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

    public int getxOffset() {
        return xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }

    public boolean isRestartImmediately() {
        return restartImmediately;
    }
}
