package com.danosoftware.galaxyforce.waves.config.aliens.spinning;

public class NoSpinningConfig extends SpinningConfig {

  public NoSpinningConfig() {
    super(SpinningConfigType.NO_SPINNING);
  }

  public static NoSpinningConfigBuilder builder() {
    return new NoSpinningConfigBuilder();
  }

  public static class NoSpinningConfigBuilder {
    NoSpinningConfigBuilder() {
    }

    public NoSpinningConfig build() {
      return new NoSpinningConfig();
    }
  }
}
