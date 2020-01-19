package com.danosoftware.galaxyforce.waves.config.aliens.types;

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
public class PathConfig extends BasicAlienConfig {

    private final Boolean angledToPath;

    @Builder
    public PathConfig(
            @NonNull final AlienCharacter alienCharacter,
            @NonNull final Integer energy,
            final MissileConfig missileConfig,
            final SpawnConfig spawnConfig,
            final SpinningConfig spinningConfig,
            final ExplosionConfig explosionConfig,
            final Boolean angledToPath) {
        super(
                AlienType.PATH,
                alienCharacter,
                energy,
                missileConfig,
                spawnConfig,
                spinningConfig,
                explosionConfig);
        this.angledToPath = angledToPath != null ? angledToPath : false;
    }
}
