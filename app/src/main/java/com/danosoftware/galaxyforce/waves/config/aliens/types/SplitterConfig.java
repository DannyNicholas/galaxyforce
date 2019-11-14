package com.danosoftware.galaxyforce.waves.config.aliens.types;

import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Config for an alien that will split into different aliens
 * as specified in the supplied configs.
 */
@Getter
public class SplitterConfig extends AlienConfig {

    private final List<AlienConfig> alienConfigs;

    @Builder
    public SplitterConfig(
            @NonNull final List<AlienConfig> alienConfigs) {
        super(
                AlienType.SPLITTER);
        this.alienConfigs = alienConfigs;
    }
}
