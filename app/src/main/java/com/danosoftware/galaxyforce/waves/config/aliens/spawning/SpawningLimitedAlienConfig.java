package com.danosoftware.galaxyforce.waves.config.aliens.spawning;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;

import java.util.List;

import lombok.NonNull;

public class SpawningLimitedAlienConfig extends SpawnConfig {

  private final AlienConfig spawnedAlienConfig;
  private final float minimumSpawnDelayTime;
  private final float maximumAdditionalRandomSpawnDelayTime;
  private final List<PowerUpType> spawnedPowerUpTypes;
  private final int maximumActiveSpawnedAliens;
  // optional alien character to limit on.
  // if not provided, will base count on all spawned aliens
  private final AlienCharacter limitedCharacter;

  public SpawningLimitedAlienConfig(
      @NonNull final AlienConfig spawnedAlienConfig,
      @NonNull final List<PowerUpType> spawnedPowerUpTypes,
      @NonNull final Float minimumSpawnDelayTime,
      @NonNull final Float maximumAdditionalRandomSpawnDelayTime,
      @NonNull final Integer maximumActiveSpawnedAliens,
      final AlienCharacter limitedCharacter) {

    super(SpawnType.SPAWN_LIMITED);
    this.spawnedAlienConfig = spawnedAlienConfig;
    this.spawnedPowerUpTypes = spawnedPowerUpTypes;
    this.minimumSpawnDelayTime = minimumSpawnDelayTime;
    this.maximumAdditionalRandomSpawnDelayTime = maximumAdditionalRandomSpawnDelayTime;
    this.maximumActiveSpawnedAliens = maximumActiveSpawnedAliens;
    this.limitedCharacter = limitedCharacter;
  }

  public static SpawningLimitedAlienConfigBuilder builder() {
    return new SpawningLimitedAlienConfigBuilder();
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

  public static class SpawningLimitedAlienConfigBuilder {
    private @NonNull AlienConfig spawnedAlienConfig;
    private @NonNull List<PowerUpType> spawnedPowerUpTypes;
    private @NonNull Float minimumSpawnDelayTime;
    private @NonNull Float maximumAdditionalRandomSpawnDelayTime;
    private @NonNull Integer maximumActiveSpawnedAliens;
    private AlienCharacter limitedCharacter;

    SpawningLimitedAlienConfigBuilder() {
    }

    public SpawningLimitedAlienConfig.SpawningLimitedAlienConfigBuilder spawnedAlienConfig(@NonNull AlienConfig spawnedAlienConfig) {
      this.spawnedAlienConfig = spawnedAlienConfig;
      return this;
    }

    public SpawningLimitedAlienConfig.SpawningLimitedAlienConfigBuilder spawnedPowerUpTypes(@NonNull List<PowerUpType> spawnedPowerUpTypes) {
      this.spawnedPowerUpTypes = spawnedPowerUpTypes;
      return this;
    }

    public SpawningLimitedAlienConfig.SpawningLimitedAlienConfigBuilder minimumSpawnDelayTime(@NonNull Float minimumSpawnDelayTime) {
      this.minimumSpawnDelayTime = minimumSpawnDelayTime;
      return this;
    }

    public SpawningLimitedAlienConfig.SpawningLimitedAlienConfigBuilder maximumAdditionalRandomSpawnDelayTime(@NonNull Float maximumAdditionalRandomSpawnDelayTime) {
      this.maximumAdditionalRandomSpawnDelayTime = maximumAdditionalRandomSpawnDelayTime;
      return this;
    }

    public SpawningLimitedAlienConfig.SpawningLimitedAlienConfigBuilder maximumActiveSpawnedAliens(@NonNull Integer maximumActiveSpawnedAliens) {
      this.maximumActiveSpawnedAliens = maximumActiveSpawnedAliens;
      return this;
    }

    public SpawningLimitedAlienConfig.SpawningLimitedAlienConfigBuilder limitedCharacter(AlienCharacter limitedCharacter) {
      this.limitedCharacter = limitedCharacter;
      return this;
    }

    public SpawningLimitedAlienConfig build() {
      return new SpawningLimitedAlienConfig(spawnedAlienConfig, spawnedPowerUpTypes, minimumSpawnDelayTime, maximumAdditionalRandomSpawnDelayTime, maximumActiveSpawnedAliens, limitedCharacter);
    }
  }
}
