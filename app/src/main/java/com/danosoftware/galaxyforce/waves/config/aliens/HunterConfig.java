package com.danosoftware.galaxyforce.waves.config.aliens;

import com.danosoftware.galaxyforce.enumerations.AlienSpeed;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienType;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class HunterConfig extends AlienConfig {

    private final AlienSpeed speed;

    @Builder
    public HunterConfig(
            @NonNull final AlienCharacter alienCharacter,
            @NonNull final Integer energy,
            final MissileConfig missileConfig,
            final SpawnConfig spawnConfig,
            @NonNull final AlienSpeed speed) {
        super(
                AlienType.HUNTER,
                alienCharacter,
                energy,
                missileConfig,
                spawnConfig);
        this.speed = speed;
    }
}
