package com.danosoftware.galaxyforce.waves.config;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;
import com.danosoftware.galaxyforce.waves.rules.SubWavePathRule;

import java.util.List;

/**
 * Describes a sub-wave consisting of a set of path rules and an alien config
 */
public class SubWavePathConfig extends SubWaveConfig {

    private final SubWavePathRule subWaveRule;

    public SubWavePathConfig(
            final SubWavePathRule subWaveRule,
            final AlienConfig alienConfig,
            final List<PowerUpType> powerUps) {

        super(Type.PATH, alienConfig, powerUps);
        this.subWaveRule = subWaveRule;
    }

    public SubWavePathRule getSubWaveRule() {
        return subWaveRule;
    }
}
