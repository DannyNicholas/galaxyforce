package com.danosoftware.galaxyforce.waves.config;

import com.danosoftware.galaxyforce.waves.AlienType;

public class AlienWithoutMissileConfig extends AlienConfig {

    public AlienWithoutMissileConfig(
            final AlienType alienType,
            final int energy) {

        super(
                Type.NO_MISSILE,
                alienType,
                energy);
    }
}
