package com.danosoftware.galaxyforce.waves.config;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;
import com.danosoftware.galaxyforce.waves.rules.SubWaveRule;

import java.util.List;

/**
 * Describes a sub-wave consisting of a set of rules and an alien config
 */
public class SubWaveNoPathConfig extends SubWaveConfig {

    private final SubWaveRule subWaveRule;

    public SubWaveNoPathConfig(
            final SubWaveRule subWaveRule,
            final AlienConfig alienConfig,
            final List<PowerUpType> powerUps) {

        super(Type.NO_PATH, alienConfig, powerUps);
        this.subWaveRule = subWaveRule;
    }

    public SubWaveRule getSubWaveRule() {
        return subWaveRule;
    }
}
