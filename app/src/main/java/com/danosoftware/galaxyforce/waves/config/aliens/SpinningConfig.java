package com.danosoftware.galaxyforce.waves.config.aliens;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class SpinningConfig {

    private final SpinningConfigType type;

    public SpinningConfig(
            @NonNull SpinningConfigType type) {
        this.type = type;
    }

    public enum SpinningConfigType {
        FIXED_ANGULAR_ROTATION, SPEED_BASED_ANGULAR_ROTATION
    }
}
