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
    private final HunterBoundariesConfig boundaries;

    @Builder
    public FollowableHunterConfig(
            @NonNull final AlienCharacter alienCharacter,
            @NonNull final Integer energy,
            final MissileConfig missileConfig,
            final SpawnConfig spawnConfig,
            final SpinningConfig spinningConfig,
            @NonNull final FollowerConfig followerConfig,
            @NonNull final Integer numberOfFollowers,
            @NonNull final List<PowerUpType> followerPowerUps,
            @NonNull final AlienSpeed speed,
            @NonNull final HunterBoundariesConfig boundaries) {
        super(
                AlienType.HUNTER_FOLLOWABLE,
                alienCharacter,
                energy,
                missileConfig,
                spawnConfig,
                spinningConfig);
        this.followerConfig = followerConfig;
        this.numberOfFollowers = numberOfFollowers;
        this.followerPowerUps = followerPowerUps;
        this.speed = speed;
        this.boundaries = boundaries;
    }
}
