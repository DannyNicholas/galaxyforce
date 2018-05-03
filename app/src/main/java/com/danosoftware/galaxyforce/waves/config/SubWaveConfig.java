package com.danosoftware.galaxyforce.waves.config;

/**
 * Base class for different types of sub-wave configs
 */
public abstract class SubWaveConfig {

    private final Type type;

    protected SubWaveConfig(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    /**
     * Type of sub-wave configs
     */
    public static enum Type {
        PATH, NO_PATH;
    }
}