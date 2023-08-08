package com.danosoftware.galaxyforce.waves.config.aliens.types;

import com.danosoftware.galaxyforce.enumerations.AlienMissileCharacter;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.config.aliens.BasicAlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.ExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.missiles.MissileConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawnConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningConfig;

import lombok.NonNull;

public class ExplodingConfig extends BasicAlienConfig {

  private final float explosionTime;
  private final AlienMissileCharacter explodingMissileCharacter;

  public ExplodingConfig(
      @NonNull final AlienCharacter alienCharacter,
      @NonNull final Integer energy,
      final MissileConfig missileConfig,
      final SpawnConfig spawnConfig,
      final SpinningConfig spinningConfig,
      final ExplosionConfig explosionConfig,
      @NonNull final Float explosionTime,
      @NonNull final AlienMissileCharacter explodingMissileCharacter) {
    super(
        AlienType.EXPLODING,
        alienCharacter,
        energy,
        missileConfig,
        spawnConfig,
        spinningConfig,
        explosionConfig);
    this.explosionTime = explosionTime;
    this.explodingMissileCharacter = explodingMissileCharacter;
  }

  public static ExplodingConfigBuilder builder() {
    return new ExplodingConfigBuilder();
  }

  public float getExplosionTime() {
    return this.explosionTime;
  }

  public AlienMissileCharacter getExplodingMissileCharacter() {
    return this.explodingMissileCharacter;
  }

  public static class ExplodingConfigBuilder {
    private @NonNull AlienCharacter alienCharacter;
    private @NonNull Integer energy;
    private MissileConfig missileConfig;
    private SpawnConfig spawnConfig;
    private SpinningConfig spinningConfig;
    private ExplosionConfig explosionConfig;
    private @NonNull Float explosionTime;
    private @NonNull AlienMissileCharacter explodingMissileCharacter;

    ExplodingConfigBuilder() {
    }

    public ExplodingConfig.ExplodingConfigBuilder alienCharacter(@NonNull AlienCharacter alienCharacter) {
      this.alienCharacter = alienCharacter;
      return this;
    }

    public ExplodingConfig.ExplodingConfigBuilder energy(@NonNull Integer energy) {
      this.energy = energy;
      return this;
    }

    public ExplodingConfig.ExplodingConfigBuilder missileConfig(MissileConfig missileConfig) {
      this.missileConfig = missileConfig;
      return this;
    }

    public ExplodingConfig.ExplodingConfigBuilder spawnConfig(SpawnConfig spawnConfig) {
      this.spawnConfig = spawnConfig;
      return this;
    }

    public ExplodingConfig.ExplodingConfigBuilder spinningConfig(SpinningConfig spinningConfig) {
      this.spinningConfig = spinningConfig;
      return this;
    }

    public ExplodingConfig.ExplodingConfigBuilder explosionConfig(ExplosionConfig explosionConfig) {
      this.explosionConfig = explosionConfig;
      return this;
    }

    public ExplodingConfig.ExplodingConfigBuilder explosionTime(@NonNull Float explosionTime) {
      this.explosionTime = explosionTime;
      return this;
    }

    public ExplodingConfig.ExplodingConfigBuilder explodingMissileCharacter(@NonNull AlienMissileCharacter explodingMissileCharacter) {
      this.explodingMissileCharacter = explodingMissileCharacter;
      return this;
    }

    public ExplodingConfig build() {
      return new ExplodingConfig(alienCharacter, energy, missileConfig, spawnConfig, spinningConfig, explosionConfig, explosionTime, explodingMissileCharacter);
    }
  }
}
