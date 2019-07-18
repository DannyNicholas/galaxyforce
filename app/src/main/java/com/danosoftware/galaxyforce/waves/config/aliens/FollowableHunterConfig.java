package com.danosoftware.galaxyforce.waves.config.aliens;

import com.danosoftware.galaxyforce.enumerations.AlienSpeed;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienType;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class FollowableHunterConfig extends AlienConfig {

    private final FollowerConfig followerConfig;
    private final int numberOfFollowers;
    private final List<PowerUpType> followerPowerUps;
    private final AlienSpeed speed;

    @Builder
    public FollowableHunterConfig(
            @NonNull final AlienCharacter alienCharacter,
            @NonNull final Integer energy,
            final MissileConfig missileConfig,
            final SpawnConfig spawnConfig,
            @NonNull final FollowerConfig followerConfig,
            @NonNull final Integer numberOfFollowers,
            @NonNull final List<PowerUpType> followerPowerUps,
            @NonNull final AlienSpeed speed) {
        super(
                AlienType.HUNTER_FOLLOWABLE,
                alienCharacter,
                energy,
                missileConfig,
                spawnConfig);
        this.followerConfig = followerConfig;
        this.numberOfFollowers = numberOfFollowers;
        this.followerPowerUps = followerPowerUps;
        this.speed = speed;
    }
}
