package com.danosoftware.galaxyforce.waves.config.aliens;

import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class SpawnConfig {

    private final SpawnType type;

    public SpawnConfig(
            @NonNull SpawnType type) {
        this.type = type;
    }

    public enum SpawnType {
        SPAWN
    }
}
