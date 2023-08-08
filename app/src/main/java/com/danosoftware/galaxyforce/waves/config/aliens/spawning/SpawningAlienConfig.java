package com.danosoftware.galaxyforce.waves.config.aliens.spawning;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;

import java.util.List;

import lombok.NonNull;

public class SpawningAlienConfig extends SpawnConfig {

  private final AlienConfig spawnedAlienConfig;
  private final float minimumSpawnDelayTime;
  private final float maximumAdditionalRandomSpawnDelayTime;
  private final List<PowerUpType> spawnedPowerUpTypes;

  public SpawningAlienConfig(
      @NonNull final AlienConfig spawnedAlienConfig,
      @NonNull final List<PowerUpType> spawnedPowerUpTypes,
      @NonNull final Float minimumSpawnDelayTime,
      @NonNull final Float maximumAdditionalRandomSpawnDelayTime) {

    super(SpawnType.SPAWN);
    this.spawnedAlienConfig = spawnedAlienConfig;
    this.spawnedPowerUpTypes = spawnedPowerUpTypes;
    this.minimumSpawnDelayTime = minimumSpawnDelayTime;
    this.maximumAdditionalRandomSpawnDelayTime = maximumAdditionalRandomSpawnDelayTime;
  }

  public static SpawningAlienConfigBuilder builder() {
    return new SpawningAlienConfigBuilder();
  }

  public AlienConfig getSpawnedAlienConfig() {
    return this.spawnedAlienConfig;
  }

  public float getMinimumSpawnDelayTime() {
    return this.minimumSpawnDelayTime;
  }

  public float getMaximumAdditionalRandomSpawnDelayTime() {
    return this.maximumAdditionalRandomSpawnDelayTime;
  }

  public List<PowerUpType> getSpawnedPowerUpTypes() {
    return this.spawnedPowerUpTypes;
  }

  public static class SpawningAlienConfigBuilder {
    private @NonNull AlienConfig spawnedAlienConfig;
    private @NonNull List<PowerUpType> spawnedPowerUpTypes;
    private @NonNull Float minimumSpawnDelayTime;
    private @NonNull Float maximumAdditionalRandomSpawnDelayTime;

    SpawningAlienConfigBuilder() {
    }

    public SpawningAlienConfig.SpawningAlienConfigBuilder spawnedAlienConfig(@NonNull AlienConfig spawnedAlienConfig) {
      this.spawnedAlienConfig = spawnedAlienConfig;
      return this;
    }

    public SpawningAlienConfig.SpawningAlienConfigBuilder spawnedPowerUpTypes(@NonNull List<PowerUpType> spawnedPowerUpTypes) {
      this.spawnedPowerUpTypes = spawnedPowerUpTypes;
      return this;
    }

    public SpawningAlienConfig.SpawningAlienConfigBuilder minimumSpawnDelayTime(@NonNull Float minimumSpawnDelayTime) {
      this.minimumSpawnDelayTime = minimumSpawnDelayTime;
      return this;
    }

    public SpawningAlienConfig.SpawningAlienConfigBuilder maximumAdditionalRandomSpawnDelayTime(@NonNull Float maximumAdditionalRandomSpawnDelayTime) {
      this.maximumAdditionalRandomSpawnDelayTime = maximumAdditionalRandomSpawnDelayTime;
      return this;
    }

    public SpawningAlienConfig build() {
      return new SpawningAlienConfig(spawnedAlienConfig, spawnedPowerUpTypes, minimumSpawnDelayTime, maximumAdditionalRandomSpawnDelayTime);
    }
  }
}
