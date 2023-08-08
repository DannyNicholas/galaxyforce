package com.danosoftware.galaxyforce.waves.config.aliens.spinning;

import lombok.NonNull;

public class SpinningConfig {

  private final SpinningConfigType type;

  public SpinningConfig(
      @NonNull SpinningConfigType type) {
    this.type = type;
  }

  public SpinningConfigType getType() {
    return this.type;
  }

  public enum SpinningConfigType {
    FIXED_ANGULAR_ROTATION, SPEED_BASED_ANGULAR_ROTATION, NO_SPINNING
  }
}
