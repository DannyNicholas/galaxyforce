package com.danosoftware.galaxyforce.waves.config.aliens.spinning;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SpinningBySpeedConfig extends SpinningConfig {

    @Builder
    public SpinningBySpeedConfig() {
        super(SpinningConfigType.SPEED_BASED_ANGULAR_ROTATION);
    }
}
