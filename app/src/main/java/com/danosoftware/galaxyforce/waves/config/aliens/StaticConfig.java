package com.danosoftware.galaxyforce.waves.config.aliens;

import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienType;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class StaticConfig extends AlienConfig {

    @Builder
    public StaticConfig(
            @NonNull final AlienCharacter alienCharacter,
            @NonNull final Integer energy,
            final MissileConfig missileConfig,
            final SpawnConfig spawnConfig,
            final SpinningConfig spinningConfig) {
        super(
                AlienType.STATIC,
                alienCharacter,
                energy,
                missileConfig,
                spawnConfig,
                spinningConfig);
    }
}
