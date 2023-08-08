package com.danosoftware.galaxyforce.waves.utilities;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.waves.config.aliens.types.PathConfig;

import java.util.List;

/**
 * Represents the configuration required to create a row of aliens.
 */
public class AlienRowConfig {

  final PathConfig alienConfig;
  final List<PowerUpType> powerUps;

  AlienRowConfig(PathConfig alienConfig, List<PowerUpType> powerUps) {
    this.alienConfig = alienConfig;
    this.powerUps = powerUps;
  }

  public static AlienRowConfigBuilder builder() {
    return new AlienRowConfigBuilder();
  }

  public PathConfig getAlienConfig() {
    return this.alienConfig;
  }

  public List<PowerUpType> getPowerUps() {
    return this.powerUps;
  }

  public static class AlienRowConfigBuilder {
    private PathConfig alienConfig;
    private List<PowerUpType> powerUps;

    AlienRowConfigBuilder() {
    }

    public AlienRowConfig.AlienRowConfigBuilder alienConfig(PathConfig alienConfig) {
      this.alienConfig = alienConfig;
      return this;
    }

    public AlienRowConfig.AlienRowConfigBuilder powerUps(List<PowerUpType> powerUps) {
      this.powerUps = powerUps;
      return this;
    }

    public AlienRowConfig build() {
      return new AlienRowConfig(alienConfig, powerUps);
    }
  }
}
