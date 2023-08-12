package com.danosoftware.galaxyforce.waves.config.aliens.spinning;

public class SpinningBySpeedConfig extends SpinningConfig {

  public SpinningBySpeedConfig() {
    super(SpinningConfigType.SPEED_BASED_ANGULAR_ROTATION);
  }

  public static SpinningBySpeedConfigBuilder builder() {
    return new SpinningBySpeedConfigBuilder();
  }

  public static class SpinningBySpeedConfigBuilder {
    SpinningBySpeedConfigBuilder() {
    }

    public SpinningBySpeedConfig build() {
      return new SpinningBySpeedConfig();
    }
  }
}
