package com.danosoftware.galaxyforce.waves.config.aliens.types;

import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;

import java.util.List;

import lombok.NonNull;

/**
 * Config for an alien that will split into different aliens as specified in the supplied configs.
 */
public class SplitterConfig extends AlienConfig {

  private final List<AlienConfig> alienConfigs;

  public SplitterConfig(
      @NonNull final List<AlienConfig> alienConfigs) {
    super(
        AlienType.SPLITTER);
    this.alienConfigs = alienConfigs;
  }

  public static SplitterConfigBuilder builder() {
    return new SplitterConfigBuilder();
  }

  public List<AlienConfig> getAlienConfigs() {
    return this.alienConfigs;
  }

  public static class SplitterConfigBuilder {
    private @NonNull List<AlienConfig> alienConfigs;

    SplitterConfigBuilder() {
    }

    public SplitterConfig.SplitterConfigBuilder alienConfigs(@NonNull List<AlienConfig> alienConfigs) {
      this.alienConfigs = alienConfigs;
      return this;
    }

    public SplitterConfig build() {
      return new SplitterConfig(alienConfigs);
    }
  }
}
