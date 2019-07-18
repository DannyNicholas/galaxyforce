package com.danosoftware.galaxyforce.waves.config.aliens;

import com.danosoftware.galaxyforce.enumerations.AlienMissileCharacter;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienType;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class ExplodingConfig extends AlienConfig {

    private final float explosionTime;
    private final AlienMissileCharacter explodingMissileCharacter;

    @Builder
    public ExplodingConfig(
            @NonNull final AlienCharacter alienCharacter,
            @NonNull final Integer energy,
            final MissileConfig missileConfig,
            final SpawnConfig spawnConfig,
            @NonNull final Float explosionTime,
            @NonNull final AlienMissileCharacter explodingMissileCharacter) {
        super(
                AlienType.EXPLODING,
                alienCharacter,
                energy,
                missileConfig,
                spawnConfig);
        this.explosionTime = explosionTime;
        this.explodingMissileCharacter = explodingMissileCharacter;
    }
}
