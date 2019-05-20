package com.danosoftware.galaxyforce.waves.config;

import com.danosoftware.galaxyforce.waves.AlienType;

public class AlienWithMissileConfig extends AlienConfig {

    private final MissileConfig missileConfig;

    public AlienWithMissileConfig(
            final AlienType alienType,
            final int energy,
            final MissileConfig missileConfig) {

        super(
                Type.MISSILE,
                alienType,
                energy);
        this.missileConfig = missileConfig;
    }

    public MissileConfig getMissileConfig() {
        return missileConfig;
    }
}
