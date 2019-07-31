package com.danosoftware.galaxyforce.waves.config.aliens;

import java.util.Collection;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents a multi-missile config.
 * That is a missile config that holds multiple missile configs.
 * Useful if an alien has multiple missile behaviours
 * (e.g alien fires downwards missiles and guided missiles at different frequencies).
 */
@Getter
public class MissileMultiFiringConfig extends MissileConfig {

    // multiple missile configs
    private final Collection<MissileFiringConfig> missileConfigs;

    @Builder
    public MissileMultiFiringConfig(
            @NonNull final Collection<MissileFiringConfig> missileConfigs) {
        super(MissileConfigType.MULTI_MISSILE);
        this.missileConfigs = missileConfigs;
    }
}
