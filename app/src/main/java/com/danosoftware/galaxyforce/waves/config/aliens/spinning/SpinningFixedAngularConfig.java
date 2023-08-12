package com.danosoftware.galaxyforce.waves.config.aliens.spinning;

import lombok.NonNull;

public class SpinningFixedAngularConfig extends SpinningConfig {

  // fixed spinning angular speed
  private final int angularSpeed;

  public SpinningFixedAngularConfig(
      @NonNull Integer angularSpeed) {
    super(SpinningConfigType.FIXED_ANGULAR_ROTATION);
    this.angularSpeed = angularSpeed;
  }

  public static SpinningFixedAngularConfigBuilder builder() {
    return new SpinningFixedAngularConfigBuilder();
  }

  public int getAngularSpeed() {
    return this.angularSpeed;
  }

  public static class SpinningFixedAngularConfigBuilder {
    private @NonNull Integer angularSpeed;

    SpinningFixedAngularConfigBuilder() {
    }

    public SpinningFixedAngularConfig.SpinningFixedAngularConfigBuilder angularSpeed(@NonNull Integer angularSpeed) {
      this.angularSpeed = angularSpeed;
      return this;
    }

    public SpinningFixedAngularConfig build() {
      return new SpinningFixedAngularConfig(angularSpeed);
    }
  }
}
