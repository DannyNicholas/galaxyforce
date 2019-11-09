package com.danosoftware.galaxyforce.waves.config.aliens.exploding;

import lombok.Getter;

@Getter
public abstract class ExplosionConfig {
    private final ExplosionConfig.ExplosionConfigType type;

    public ExplosionConfig(ExplosionConfig.ExplosionConfigType type) {
        this.type = type;
    }

    public enum ExplosionConfigType {
        NORMAL, NORMAL_AND_SPAWN, MULTI_EXPLOSION
    }
}