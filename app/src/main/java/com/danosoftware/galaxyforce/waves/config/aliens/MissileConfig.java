package com.danosoftware.galaxyforce.waves.config.aliens;

import lombok.Getter;

@Getter
public abstract class MissileConfig {

    private final MissileConfigType type;

    public MissileConfig(MissileConfigType type) {
        this.type = type;
    }

    public enum MissileConfigType {
        MISSILE
    }
}
