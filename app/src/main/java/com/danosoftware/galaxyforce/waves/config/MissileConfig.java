package com.danosoftware.galaxyforce.waves.config;

import com.danosoftware.galaxyforce.enumerations.AlienMissileSpeed;
import com.danosoftware.galaxyforce.enumerations.AlienMissileType;

public class MissileConfig {

    private final AlienMissileType missileType;
    private final AlienMissileSpeed missileSpeed;
    private final float missileFrequency;

    public MissileConfig(
            final AlienMissileType missileType,
            final AlienMissileSpeed missileSpeed,
            final float missileFrequency) {
        this.missileType = missileType;
        this.missileSpeed = missileSpeed;
        this.missileFrequency = missileFrequency;
    }

    public AlienMissileType getMissileType() {
        return missileType;
    }

    public AlienMissileSpeed getMissileSpeed() {
        return missileSpeed;
    }

    public float getMissileFrequency() {
        return missileFrequency;
    }
}
