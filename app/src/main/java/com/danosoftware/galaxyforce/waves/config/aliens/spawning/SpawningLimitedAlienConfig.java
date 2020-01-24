package com.danosoftware.galaxyforce.waves.config.aliens.spawning;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class SpawningLimitedAlienConfig extends SpawnConfig {

    private final AlienConfig spawnedAlienConfig;
    private final float minimumSpawnDelayTime;
    private final float maximumAdditionalRandomSpawnDelayTime;
    private final List<PowerUpType> spwanedPowerUpTypes;
    private final int maximumActiveSpawnedAliens;
    // optional alien character to limit on.
    // if not provided, will base count on all spawned aliens
    private final AlienCharacter limitedCharacter;

    @Builder
    public SpawningLimitedAlienConfig(
            @NonNull final AlienConfig spawnedAlienConfig,
            @NonNull final List<PowerUpType> spwanedPowerUpTypes,
            @NonNull final Float minimumSpawnDelayTime,
            @NonNull final Float maximumAdditionalRandomSpawnDelayTime,
            @NonNull final Integer maximumActiveSpawnedAliens,
            final AlienCharacter limitedCharacter) {

        super(SpawnType.SPAWN_LIMITED);
        this.spawnedAlienConfig = spawnedAlienConfig;
        this.spwanedPowerUpTypes = spwanedPowerUpTypes;
        this.minimumSpawnDelayTime = minimumSpawnDelayTime;
        this.maximumAdditionalRandomSpawnDelayTime = maximumAdditionalRandomSpawnDelayTime;
        this.maximumActiveSpawnedAliens = maximumActiveSpawnedAliens;
        this.limitedCharacter = limitedCharacter;
    }
}
