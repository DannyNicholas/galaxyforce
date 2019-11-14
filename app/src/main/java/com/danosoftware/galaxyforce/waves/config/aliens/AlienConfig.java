package com.danosoftware.galaxyforce.waves.config.aliens;

import com.danosoftware.galaxyforce.waves.AlienType;

import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class AlienConfig {

    private final AlienType alienType;

    public AlienConfig(
            @NonNull final AlienType alienType) {
        this.alienType = alienType;
    }
}
