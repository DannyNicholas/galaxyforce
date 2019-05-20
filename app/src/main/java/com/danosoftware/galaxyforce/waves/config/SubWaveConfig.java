package com.danosoftware.galaxyforce.waves.config;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;

import java.util.List;

/**
 * Base class for different types of sub-wave configs
 */
public abstract class SubWaveConfig {

    private final Type type;
    private final AlienConfig alienConfig;
    private final List<PowerUpType> powerUps;

    SubWaveConfig(
            final Type type,
            final AlienConfig alienConfig,
            final List<PowerUpType> powerUps) {
        this.type = type;
        this.alienConfig = alienConfig;
        this.powerUps = powerUps;
    }

    public Type getType() {
        return type;
    }

    public AlienConfig getAlienConfig() {
        return alienConfig;
    }

    public List<PowerUpType> getPowerUps() {
        return powerUps;
    }

    /**
     * Type of sub-wave configs
     */
    public enum Type {
        PATH, NO_PATH
    }
}
