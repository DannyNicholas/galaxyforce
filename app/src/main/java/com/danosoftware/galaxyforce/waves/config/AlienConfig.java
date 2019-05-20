package com.danosoftware.galaxyforce.waves.config;

import com.danosoftware.galaxyforce.waves.AlienType;

public abstract class AlienConfig {

    private final Type type;
    private final AlienType alienType;
    private final int energy;

    public AlienConfig(
            final Type type,
            final AlienType alienType,
            final int energy) {
        this.type = type;
        this.alienType = alienType;
        this.energy = energy;
    }

    public Type getType() {
        return type;
    }

    public AlienType getAlienType() {
        return alienType;
    }

    public int getEnergy() {
        return energy;
    }

    /**
     * Type of alien config
     */
    public enum Type {
        MISSILE, NO_MISSILE
    }
}
