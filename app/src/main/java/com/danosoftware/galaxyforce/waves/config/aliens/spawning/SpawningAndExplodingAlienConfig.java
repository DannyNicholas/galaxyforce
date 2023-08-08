package com.danosoftware.galaxyforce.waves.config.aliens.spawning;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;

import lombok.NonNull;

public class SpawningAndExplodingAlienConfig extends SpawnConfig {

  private final AlienConfig spawnedAlienConfig;
  private final float spawnDelayTime;
  private final PowerUpType spawnedPowerUpType;

  public SpawningAndExplodingAlienConfig(
      @NonNull final AlienConfig spawnedAlienConfig,
      @NonNull final PowerUpType spawnedPowerUpType,
      @NonNull final Float spawnDelayTime) {

    super(SpawnType.SPAWN_AND_EXPLODE);
    this.spawnedAlienConfig = spawnedAlienConfig;
    this.spawnedPowerUpType = spawnedPowerUpType;
    this.spawnDelayTime = spawnDelayTime;
  }

  public static SpawningAndExplodingAlienConfigBuilder builder() {
    return new SpawningAndExplodingAlienConfigBuilder();
  }

  public AlienConfig getSpawnedAlienConfig() {
    return this.spawnedAlienConfig;
  }

  public float getSpawnDelayTime() {
    return this.spawnDelayTime;
  }

  public PowerUpType getSpawnedPowerUpType() {
    return this.spawnedPowerUpType;
  }

  public static class SpawningAndExplodingAlienConfigBuilder {
    private @NonNull AlienConfig spawnedAlienConfig;
    private @NonNull PowerUpType spawnedPowerUpType;
    private @NonNull Float spawnDelayTime;

    SpawningAndExplodingAlienConfigBuilder() {
    }

    public SpawningAndExplodingAlienConfig.SpawningAndExplodingAlienConfigBuilder spawnedAlienConfig(@NonNull AlienConfig spawnedAlienConfig) {
      this.spawnedAlienConfig = spawnedAlienConfig;
      return this;
    }

    public SpawningAndExplodingAlienConfig.SpawningAndExplodingAlienConfigBuilder spawnedPowerUpType(@NonNull PowerUpType spawnedPowerUpType) {
      this.spawnedPowerUpType = spawnedPowerUpType;
      return this;
    }

    public SpawningAndExplodingAlienConfig.SpawningAndExplodingAlienConfigBuilder spawnDelayTime(@NonNull Float spawnDelayTime) {
      this.spawnDelayTime = spawnDelayTime;
      return this;
    }

    public SpawningAndExplodingAlienConfig build() {
      return new SpawningAndExplodingAlienConfig(spawnedAlienConfig, spawnedPowerUpType, spawnDelayTime);
    }
  }
}
