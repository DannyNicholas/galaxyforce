package com.danosoftware.galaxyforce.waves.config.aliens.spawning;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;

import java.util.List;

import lombok.NonNull;

public class SpawnOnDemandConfig extends SpawnConfig {

  private final AlienConfig spawnedAlienConfig;
  private final List<PowerUpType> spawnedPowerUpTypes;

  public SpawnOnDemandConfig(
      @NonNull final AlienConfig spawnedAlienConfig,
      @NonNull final List<PowerUpType> spawnedPowerUpTypes) {

    super(SpawnType.SPAWN_ON_DEMAND);
    this.spawnedAlienConfig = spawnedAlienConfig;
    this.spawnedPowerUpTypes = spawnedPowerUpTypes;
  }

  public static SpawnOnDemandConfigBuilder builder() {
    return new SpawnOnDemandConfigBuilder();
  }

  public AlienConfig getSpawnedAlienConfig() {
    return this.spawnedAlienConfig;
  }

  public List<PowerUpType> getSpawnedPowerUpTypes() {
    return this.spawnedPowerUpTypes;
  }

  public static class SpawnOnDemandConfigBuilder {
    private @NonNull AlienConfig spawnedAlienConfig;
    private @NonNull List<PowerUpType> spawnedPowerUpTypes;

    SpawnOnDemandConfigBuilder() {
    }

    public SpawnOnDemandConfig.SpawnOnDemandConfigBuilder spawnedAlienConfig(@NonNull AlienConfig spawnedAlienConfig) {
      this.spawnedAlienConfig = spawnedAlienConfig;
      return this;
    }

    public SpawnOnDemandConfig.SpawnOnDemandConfigBuilder spawnedPowerUpTypes(@NonNull List<PowerUpType> spawnedPowerUpTypes) {
      this.spawnedPowerUpTypes = spawnedPowerUpTypes;
      return this;
    }

    public SpawnOnDemandConfig build() {
      return new SpawnOnDemandConfig(spawnedAlienConfig, spawnedPowerUpTypes);
    }
  }
}
