package com.danosoftware.galaxyforce.waves.config.aliens;

import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienType;

import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class AlienConfig {

    private final AlienType alienType;
    private final int energy;
    private final AlienCharacter alienCharacter;
    private final SpawnConfig spawnConfig;
    private final MissileConfig missileConfig;
    private final SpinningConfig spinningConfig;

    public AlienConfig(
            @NonNull final AlienType alienType,
            @NonNull final AlienCharacter alienCharacter,
            @NonNull final Integer energy,
            final MissileConfig missileConfig,
            final SpawnConfig spawnConfig,
            final SpinningConfig spinningConfig) {
        this.alienType = alienType;
        this.alienCharacter = alienCharacter;
        this.energy = energy;
        this.missileConfig = missileConfig;
        this.spawnConfig = spawnConfig;
        this.spinningConfig = spinningConfig;
    }
}
