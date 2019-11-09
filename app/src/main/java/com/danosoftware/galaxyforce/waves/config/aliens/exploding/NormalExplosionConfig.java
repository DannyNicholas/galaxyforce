package com.danosoftware.galaxyforce.waves.config.aliens.exploding;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NormalExplosionConfig extends ExplosionConfig {

    @Builder
    public NormalExplosionConfig() {
        super(ExplosionConfigType.NORMAL);
    }
}
