package com.danosoftware.galaxyforce.waves.config.aliens.spawning;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class SpawnOnDemandConfig extends SpawnConfig {

    private final AlienConfig spawnedAlienConfig;
    private final List<PowerUpType> spwanedPowerUpTypes;

    @Builder
    public SpawnOnDemandConfig(
            @NonNull final AlienConfig spawnedAlienConfig,
            @NonNull final List<PowerUpType> spwanedPowerUpTypes) {

        super(SpawnType.SPAWN_ON_DEMAND);
        this.spawnedAlienConfig = spawnedAlienConfig;
        this.spwanedPowerUpTypes = spwanedPowerUpTypes;
    }
}
