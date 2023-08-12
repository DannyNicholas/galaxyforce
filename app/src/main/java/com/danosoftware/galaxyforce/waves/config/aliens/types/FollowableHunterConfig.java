package com.danosoftware.galaxyforce.waves.config.aliens.types;

import com.danosoftware.galaxyforce.enumerations.AlienSpeed;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.config.aliens.BasicAlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.ExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.missiles.MissileConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawnConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningConfig;

import java.util.List;

import lombok.NonNull;

public class FollowableHunterConfig extends BasicAlienConfig {

  private final FollowerConfig followerConfig;
  private final int numberOfFollowers;
  private final List<PowerUpType> followerPowerUps;
  private final AlienSpeed speed;
  private final BoundariesConfig boundaries;

  public FollowableHunterConfig(
      @NonNull final AlienCharacter alienCharacter,
      @NonNull final Integer energy,
      final MissileConfig missileConfig,
      final SpawnConfig spawnConfig,
      final SpinningConfig spinningConfig,
      final ExplosionConfig explosionConfig,
      @NonNull final FollowerConfig followerConfig,
      @NonNull final Integer numberOfFollowers,
      @NonNull final List<PowerUpType> followerPowerUps,
      @NonNull final AlienSpeed speed,
      @NonNull final BoundariesConfig boundaries) {
    super(
        AlienType.HUNTER_FOLLOWABLE,
        alienCharacter,
        energy,
        missileConfig,
        spawnConfig,
        spinningConfig,
        explosionConfig);
    this.followerConfig = followerConfig;
    this.numberOfFollowers = numberOfFollowers;
    this.followerPowerUps = followerPowerUps;
    this.speed = speed;
    this.boundaries = boundaries;
  }

  public static FollowableHunterConfigBuilder builder() {
    return new FollowableHunterConfigBuilder();
  }

  public FollowerConfig getFollowerConfig() {
    return this.followerConfig;
  }

  public int getNumberOfFollowers() {
    return this.numberOfFollowers;
  }

  public List<PowerUpType> getFollowerPowerUps() {
    return this.followerPowerUps;
  }

  public AlienSpeed getSpeed() {
    return this.speed;
  }

  public BoundariesConfig getBoundaries() {
    return this.boundaries;
  }

  public static class FollowableHunterConfigBuilder {
    private @NonNull AlienCharacter alienCharacter;
    private @NonNull Integer energy;
    private MissileConfig missileConfig;
    private SpawnConfig spawnConfig;
    private SpinningConfig spinningConfig;
    private ExplosionConfig explosionConfig;
    private @NonNull FollowerConfig followerConfig;
    private @NonNull Integer numberOfFollowers;
    private @NonNull List<PowerUpType> followerPowerUps;
    private @NonNull AlienSpeed speed;
    private @NonNull BoundariesConfig boundaries;

    FollowableHunterConfigBuilder() {
    }

    public FollowableHunterConfig.FollowableHunterConfigBuilder alienCharacter(@NonNull AlienCharacter alienCharacter) {
      this.alienCharacter = alienCharacter;
      return this;
    }

    public FollowableHunterConfig.FollowableHunterConfigBuilder energy(@NonNull Integer energy) {
      this.energy = energy;
      return this;
    }

    public FollowableHunterConfig.FollowableHunterConfigBuilder missileConfig(MissileConfig missileConfig) {
      this.missileConfig = missileConfig;
      return this;
    }

    public FollowableHunterConfig.FollowableHunterConfigBuilder spawnConfig(SpawnConfig spawnConfig) {
      this.spawnConfig = spawnConfig;
      return this;
    }

    public FollowableHunterConfig.FollowableHunterConfigBuilder spinningConfig(SpinningConfig spinningConfig) {
      this.spinningConfig = spinningConfig;
      return this;
    }

    public FollowableHunterConfig.FollowableHunterConfigBuilder explosionConfig(ExplosionConfig explosionConfig) {
      this.explosionConfig = explosionConfig;
      return this;
    }

    public FollowableHunterConfig.FollowableHunterConfigBuilder followerConfig(@NonNull FollowerConfig followerConfig) {
      this.followerConfig = followerConfig;
      return this;
    }

    public FollowableHunterConfig.FollowableHunterConfigBuilder numberOfFollowers(@NonNull Integer numberOfFollowers) {
      this.numberOfFollowers = numberOfFollowers;
      return this;
    }

    public FollowableHunterConfig.FollowableHunterConfigBuilder followerPowerUps(@NonNull List<PowerUpType> followerPowerUps) {
      this.followerPowerUps = followerPowerUps;
      return this;
    }

    public FollowableHunterConfig.FollowableHunterConfigBuilder speed(@NonNull AlienSpeed speed) {
      this.speed = speed;
      return this;
    }

    public FollowableHunterConfig.FollowableHunterConfigBuilder boundaries(@NonNull BoundariesConfig boundaries) {
      this.boundaries = boundaries;
      return this;
    }

    public FollowableHunterConfig build() {
      return new FollowableHunterConfig(alienCharacter, energy, missileConfig, spawnConfig, spinningConfig, explosionConfig, followerConfig, numberOfFollowers, followerPowerUps, speed, boundaries);
    }
  }
}
