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

public class HunterConfig extends BasicAlienConfig {

  private final AlienSpeed speed;
  private final BoundariesConfig boundaries;

  public HunterConfig(
      @NonNull final AlienCharacter alienCharacter,
      @NonNull final Integer energy,
      final MissileConfig missileConfig,
      final SpawnConfig spawnConfig,
      final SpinningConfig spinningConfig,
      final ExplosionConfig explosionConfig,
      @NonNull final AlienSpeed speed,
      @NonNull final BoundariesConfig boundaries) {
    super(
        AlienType.HUNTER,
        alienCharacter,
        energy,
        missileConfig,
        spawnConfig,
        spinningConfig,
        explosionConfig);
    this.speed = speed;
    this.boundaries = boundaries;
  }

  public static HunterConfigBuilder builder() {
    return new HunterConfigBuilder();
  }

  public AlienSpeed getSpeed() {
    return this.speed;
  }

  public BoundariesConfig getBoundaries() {
    return this.boundaries;
  }

  public static class HunterConfigBuilder {
    private @NonNull AlienCharacter alienCharacter;
    private @NonNull Integer energy;
    private MissileConfig missileConfig;
    private SpawnConfig spawnConfig;
    private SpinningConfig spinningConfig;
    private ExplosionConfig explosionConfig;
    private @NonNull AlienSpeed speed;
    private @NonNull BoundariesConfig boundaries;

    HunterConfigBuilder() {
    }

    public HunterConfig.HunterConfigBuilder alienCharacter(@NonNull AlienCharacter alienCharacter) {
      this.alienCharacter = alienCharacter;
      return this;
    }

    public HunterConfig.HunterConfigBuilder energy(@NonNull Integer energy) {
      this.energy = energy;
      return this;
    }

    public HunterConfig.HunterConfigBuilder missileConfig(MissileConfig missileConfig) {
      this.missileConfig = missileConfig;
      return this;
    }

    public HunterConfig.HunterConfigBuilder spawnConfig(SpawnConfig spawnConfig) {
      this.spawnConfig = spawnConfig;
      return this;
    }

    public HunterConfig.HunterConfigBuilder spinningConfig(SpinningConfig spinningConfig) {
      this.spinningConfig = spinningConfig;
      return this;
    }

    public HunterConfig.HunterConfigBuilder explosionConfig(ExplosionConfig explosionConfig) {
      this.explosionConfig = explosionConfig;
      return this;
    }

    public HunterConfig.HunterConfigBuilder speed(@NonNull AlienSpeed speed) {
      this.speed = speed;
      return this;
    }

    public HunterConfig.HunterConfigBuilder boundaries(@NonNull BoundariesConfig boundaries) {
      this.boundaries = boundaries;
      return this;
    }

    public HunterConfig build() {
      return new HunterConfig(alienCharacter, energy, missileConfig, spawnConfig, spinningConfig, explosionConfig, speed, boundaries);
    }
  }
}
