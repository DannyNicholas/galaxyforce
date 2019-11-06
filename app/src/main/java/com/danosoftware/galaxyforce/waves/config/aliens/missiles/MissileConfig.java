package com.danosoftware.galaxyforce.waves.config.aliens.missiles;

import lombok.Getter;

@Getter
public abstract class MissileConfig {

    private final MissileConfigType type;

    public MissileConfig(MissileConfigType type) {
        this.type = type;
    }

    public enum MissileConfigType {
        MISSILE, MULTI_MISSILE
    }
}
