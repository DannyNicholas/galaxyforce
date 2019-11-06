package com.danosoftware.galaxyforce.waves.config.aliens.types;

import com.danosoftware.galaxyforce.enumerations.AlienSpeed;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.ExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.missiles.MissileConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawnConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningConfig;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Config for an alien that will split in different directions
 * as specified in the supplied angles.
 */
@Getter
public class SplitterConfig extends AlienConfig {

    @NonNull final AlienSpeed speed;
    private final List<Float> angles;

    @Builder
    public SplitterConfig(
            @NonNull final AlienCharacter alienCharacter,
            @NonNull final Integer energy,
            final MissileConfig missileConfig,
            final SpawnConfig spawnConfig,
            final SpinningConfig spinningConfig,
            final ExplosionConfig explosionConfig,
            @NonNull final AlienSpeed speed,
            @NonNull List<Float> angles) {
        super(
                AlienType.SPLITTER,
                alienCharacter,
                energy,
                missileConfig,
                spawnConfig,
                spinningConfig,
                explosionConfig);
        this.speed = speed;
        this.angles = angles;
    }
}
