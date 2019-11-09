package com.danosoftware.galaxyforce.waves.config.aliens.exploding;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class MultiExplosionConfig extends ExplosionConfig {

    private final Integer numberOfExplosions;
    private final Float maximumExplosionStartTime;
    // config of additional explosions created
    private final ExplosionConfig explosionConfig;

    @Builder
    public MultiExplosionConfig(
            @NonNull final Integer numberOfExplosions,
            @NonNull final Float maximumExplosionStartTime,
            final ExplosionConfig explosionConfig
    ) {
        super(ExplosionConfigType.MULTI_EXPLOSION);
        this.numberOfExplosions = numberOfExplosions;
        this.maximumExplosionStartTime = maximumExplosionStartTime;
        this.explosionConfig = explosionConfig;
    }
}
