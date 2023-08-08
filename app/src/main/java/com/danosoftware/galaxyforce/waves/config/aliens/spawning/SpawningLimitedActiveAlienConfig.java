package com.danosoftware.galaxyforce.waves.config.aliens.spawning;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;

import java.util.List;

import lombok.NonNull;

public class SpawningLimitedActiveAlienConfig extends SpawnConfig {

  private final AlienConfig spawnedAlienConfig;
  private final float minimumSpawnDelayTime;
  private final float maximumAdditionalRandomSpawnDelayTime;
  private final List<PowerUpType> spawnedPowerUpTypes;
  private final int maximumActiveSpawnedAliens;
  private final AlienCharacter limitedCharacter;

  public SpawningLimitedActiveAlienConfig(
      @NonNull final AlienConfig spawnedAlienConfig,
      @NonNull final List<PowerUpType> spawnedPowerUpTypes,
      @NonNull final Float minimumSpawnDelayTime,
      @NonNull final Float maximumAdditionalRandomSpawnDelayTime,
      @NonNull final Integer maximumActiveSpawnedAliens,
      @NonNull final AlienCharacter limitedCharacter) {

    super(SpawnType.SPAWN_LIMITED_ACTIVE_ALIEN);
    this.spawnedAlienConfig = spawnedAlienConfig;
    this.spawnedPowerUpTypes = spawnedPowerUpTypes;
    this.minimumSpawnDelayTime = minimumSpawnDelayTime;
    this.maximumAdditionalRandomSpawnDelayTime = maximumAdditionalRandomSpawnDelayTime;
    this.maximumActiveSpawnedAliens = maximumActiveSpawnedAliens;
    this.limitedCharacter = limitedCharacter;
  }

  public static SpawningLimitedActiveAlienConfigBuilder builder() {
    return new SpawningLimitedActiveAlienConfigBuilder();
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

  public int getMaximumActiveSpawnedAliens() {
    return this.maximumActiveSpawnedAliens;
  }

  public AlienCharacter getLimitedCharacter() {
    return this.limitedCharacter;
  }

  public static class SpawningLimitedActiveAlienConfigBuilder {
    private @NonNull AlienConfig spawnedAlienConfig;
    private @NonNull List<PowerUpType> spawnedPowerUpTypes;
    private @NonNull Float minimumSpawnDelayTime;
    private @NonNull Float maximumAdditionalRandomSpawnDelayTime;
    private @NonNull Integer maximumActiveSpawnedAliens;
    private @NonNull AlienCharacter limitedCharacter;

    SpawningLimitedActiveAlienConfigBuilder() {
    }

    public SpawningLimitedActiveAlienConfig.SpawningLimitedActiveAlienConfigBuilder spawnedAlienConfig(@NonNull AlienConfig spawnedAlienConfig) {
      this.spawnedAlienConfig = spawnedAlienConfig;
      return this;
    }

    public SpawningLimitedActiveAlienConfig.SpawningLimitedActiveAlienConfigBuilder spawnedPowerUpTypes(@NonNull List<PowerUpType> spawnedPowerUpTypes) {
      this.spawnedPowerUpTypes = spawnedPowerUpTypes;
      return this;
    }

    public SpawningLimitedActiveAlienConfig.SpawningLimitedActiveAlienConfigBuilder minimumSpawnDelayTime(@NonNull Float minimumSpawnDelayTime) {
      this.minimumSpawnDelayTime = minimumSpawnDelayTime;
      return this;
    }

    public SpawningLimitedActiveAlienConfig.SpawningLimitedActiveAlienConfigBuilder maximumAdditionalRandomSpawnDelayTime(@NonNull Float maximumAdditionalRandomSpawnDelayTime) {
      this.maximumAdditionalRandomSpawnDelayTime = maximumAdditionalRandomSpawnDelayTime;
      return this;
    }

    public SpawningLimitedActiveAlienConfig.SpawningLimitedActiveAlienConfigBuilder maximumActiveSpawnedAliens(@NonNull Integer maximumActiveSpawnedAliens) {
      this.maximumActiveSpawnedAliens = maximumActiveSpawnedAliens;
      return this;
    }

    public SpawningLimitedActiveAlienConfig.SpawningLimitedActiveAlienConfigBuilder limitedCharacter(@NonNull AlienCharacter limitedCharacter) {
      this.limitedCharacter = limitedCharacter;
      return this;
    }

    public SpawningLimitedActiveAlienConfig build() {
      return new SpawningLimitedActiveAlienConfig(spawnedAlienConfig, spawnedPowerUpTypes, minimumSpawnDelayTime, maximumAdditionalRandomSpawnDelayTime, maximumActiveSpawnedAliens, limitedCharacter);
    }
  }
}
