package com.danosoftware.galaxyforce.waves.config.aliens;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class SpinningFixedAngularConfig extends SpinningConfig {

    // fixed spinning angular speed
    private final int angularSpeed;

    @Builder
    public SpinningFixedAngularConfig(
            @NonNull Integer angularSpeed) {
        super(SpinningConfigType.FIXED_ANGULAR_ROTATION);
        this.angularSpeed = angularSpeed;
    }
}
