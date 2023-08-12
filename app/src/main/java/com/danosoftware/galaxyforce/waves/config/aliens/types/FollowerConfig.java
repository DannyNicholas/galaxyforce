package com.danosoftware.galaxyforce.waves.config.aliens.types;

import com.danosoftware.galaxyforce.enumerations.AlienSpeed;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.config.aliens.BasicAlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.ExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.missiles.MissileConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawnConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningConfig;

import lombok.NonNull;

public class FollowerConfig extends BasicAlienConfig {

  private final AlienSpeed speed;

  public FollowerConfig(
      @NonNull final AlienCharacter alienCharacter,
      @NonNull final Integer energy,
      final MissileConfig missileConfig,
      final SpawnConfig spawnConfig,
      final SpinningConfig spinningConfig,
      final ExplosionConfig explosionConfig,
      @NonNull final AlienSpeed speed) {
    super(
        AlienType.FOLLOWER,
        alienCharacter,
        energy,
        missileConfig,
        spawnConfig,
        spinningConfig,
        explosionConfig);
    this.speed = speed;
  }

  public static FollowerConfigBuilder builder() {
    return new FollowerConfigBuilder();
  }

  public AlienSpeed getSpeed() {
    return this.speed;
  }

  public static class FollowerConfigBuilder {
    private @NonNull AlienCharacter alienCharacter;
    private @NonNull Integer energy;
    private MissileConfig missileConfig;
    private SpawnConfig spawnConfig;
    private SpinningConfig spinningConfig;
    private ExplosionConfig explosionConfig;
    private @NonNull AlienSpeed speed;

    FollowerConfigBuilder() {
    }

    public FollowerConfig.FollowerConfigBuilder alienCharacter(@NonNull AlienCharacter alienCharacter) {
      this.alienCharacter = alienCharacter;
      return this;
    }

    public FollowerConfig.FollowerConfigBuilder energy(@NonNull Integer energy) {
      this.energy = energy;
      return this;
    }

    public FollowerConfig.FollowerConfigBuilder missileConfig(MissileConfig missileConfig) {
      this.missileConfig = missileConfig;
      return this;
    }

    public FollowerConfig.FollowerConfigBuilder spawnConfig(SpawnConfig spawnConfig) {
      this.spawnConfig = spawnConfig;
      return this;
    }

    public FollowerConfig.FollowerConfigBuilder spinningConfig(SpinningConfig spinningConfig) {
      this.spinningConfig = spinningConfig;
      return this;
    }

    public FollowerConfig.FollowerConfigBuilder explosionConfig(ExplosionConfig explosionConfig) {
      this.explosionConfig = explosionConfig;
      return this;
    }

    public FollowerConfig.FollowerConfigBuilder speed(@NonNull AlienSpeed speed) {
      this.speed = speed;
      return this;
    }

    public FollowerConfig build() {
      return new FollowerConfig(alienCharacter, energy, missileConfig, spawnConfig, spinningConfig, explosionConfig, speed);
    }
  }
}
