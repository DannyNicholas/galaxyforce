package com.danosoftware.galaxyforce.waves.config.aliens.missiles;

import com.danosoftware.galaxyforce.enumerations.AlienMissileCharacter;
import com.danosoftware.galaxyforce.enumerations.AlienMissileSpeed;
import com.danosoftware.galaxyforce.enumerations.AlienMissileType;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class MissileFiringConfig extends MissileConfig {

    private final AlienMissileType missileType;
    private final AlienMissileSpeed missileSpeed;
    private final float missileFrequency;
    private final AlienMissileCharacter missileCharacter;

    @Builder
    public MissileFiringConfig(
            @NonNull final AlienMissileType missileType,
            @NonNull final AlienMissileSpeed missileSpeed,
            @NonNull final AlienMissileCharacter missileCharacter,
            @NonNull final Float missileFrequency) {
        super(MissileConfigType.MISSILE);
        this.missileType = missileType;
        this.missileSpeed = missileSpeed;
        this.missileCharacter = missileCharacter;
        this.missileFrequency = missileFrequency;
    }
}
