package com.danosoftware.galaxyforce.waves.config.aliens.types;

import com.danosoftware.galaxyforce.enumerations.AlienMissileCharacter;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.config.aliens.BasicAlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.ExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.missiles.MissileConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawnConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningConfig;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class ExplodingConfig extends BasicAlienConfig {

    private final float explosionTime;
    private final AlienMissileCharacter explodingMissileCharacter;

    @Builder
    public ExplodingConfig(
            @NonNull final AlienCharacter alienCharacter,
            @NonNull final Integer energy,
            final MissileConfig missileConfig,
            final SpawnConfig spawnConfig,
            final SpinningConfig spinningConfig,
            final ExplosionConfig explosionConfig,
            @NonNull final Float explosionTime,
            @NonNull final AlienMissileCharacter explodingMissileCharacter) {
        super(
                AlienType.EXPLODING,
                alienCharacter,
                energy,
                missileConfig,
                spawnConfig,
                spinningConfig,
                explosionConfig);
        this.explosionTime = explosionTime;
        this.explodingMissileCharacter = explodingMissileCharacter;
    }
}
