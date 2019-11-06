package com.danosoftware.galaxyforce.waves.config.aliens.exploding;

import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawnConfig;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class SpawningExplosionConfig extends ExplosionConfig {

    // holds the spawn config when alien explodes
    private final SpawnConfig spawnConfig;

    @Builder
    public SpawningExplosionConfig(
            @NonNull final SpawnConfig spawnConfig) {
        super(ExplosionConfigType.NORMAL_AND_SPAWN);
        this.spawnConfig = spawnConfig;
    }
}
