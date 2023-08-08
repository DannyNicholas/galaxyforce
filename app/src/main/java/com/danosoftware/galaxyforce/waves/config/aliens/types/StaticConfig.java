package com.danosoftware.galaxyforce.waves.config.aliens.types;

import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.config.aliens.BasicAlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.ExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.missiles.MissileConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawnConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningConfig;

import lombok.NonNull;

public class StaticConfig extends BasicAlienConfig {

  public StaticConfig(
      @NonNull final AlienCharacter alienCharacter,
      @NonNull final Integer energy,
      final MissileConfig missileConfig,
      final SpawnConfig spawnConfig,
      final SpinningConfig spinningConfig,
      final ExplosionConfig explosionConfig) {
    super(
        AlienType.STATIC,
        alienCharacter,
        energy,
        missileConfig,
        spawnConfig,
        spinningConfig,
        explosionConfig);
  }

  public static StaticConfigBuilder builder() {
    return new StaticConfigBuilder();
  }

  public static class StaticConfigBuilder {
    private @NonNull AlienCharacter alienCharacter;
    private @NonNull Integer energy;
    private MissileConfig missileConfig;
    private SpawnConfig spawnConfig;
    private SpinningConfig spinningConfig;
    private ExplosionConfig explosionConfig;

    StaticConfigBuilder() {
    }

    public StaticConfigBuilder alienCharacter(@NonNull AlienCharacter alienCharacter) {
      this.alienCharacter = alienCharacter;
      return this;
    }

    public StaticConfigBuilder energy(@NonNull Integer energy) {
      this.energy = energy;
      return this;
    }

    public StaticConfigBuilder missileConfig(MissileConfig missileConfig) {
      this.missileConfig = missileConfig;
      return this;
    }

    public StaticConfigBuilder spawnConfig(SpawnConfig spawnConfig) {
      this.spawnConfig = spawnConfig;
      return this;
    }

    public StaticConfigBuilder spinningConfig(SpinningConfig spinningConfig) {
      this.spinningConfig = spinningConfig;
      return this;
    }

    public StaticConfigBuilder explosionConfig(ExplosionConfig explosionConfig) {
      this.explosionConfig = explosionConfig;
      return this;
    }

    public StaticConfig build() {
      return new StaticConfig(alienCharacter, energy, missileConfig, spawnConfig, spinningConfig, explosionConfig);
    }
  }
}
