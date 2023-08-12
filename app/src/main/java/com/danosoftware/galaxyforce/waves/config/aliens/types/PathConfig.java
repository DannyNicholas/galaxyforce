package com.danosoftware.galaxyforce.waves.config.aliens.types;

import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.config.aliens.BasicAlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.ExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.missiles.MissileConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawnConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningConfig;

import lombok.NonNull;

public class PathConfig extends BasicAlienConfig {

  private final Boolean angledToPath;

  public PathConfig(
      @NonNull final AlienCharacter alienCharacter,
      @NonNull final Integer energy,
      final MissileConfig missileConfig,
      final SpawnConfig spawnConfig,
      final SpinningConfig spinningConfig,
      final ExplosionConfig explosionConfig,
      final Boolean angledToPath) {
    super(
        AlienType.PATH,
        alienCharacter,
        energy,
        missileConfig,
        spawnConfig,
        spinningConfig,
        explosionConfig);
    this.angledToPath = angledToPath != null ? angledToPath : false;
  }

  public static PathConfigBuilder builder() {
    return new PathConfigBuilder();
  }

  public Boolean getAngledToPath() {
    return this.angledToPath;
  }

  public static class PathConfigBuilder {
    private @NonNull AlienCharacter alienCharacter;
    private @NonNull Integer energy;
    private MissileConfig missileConfig;
    private SpawnConfig spawnConfig;
    private SpinningConfig spinningConfig;
    private ExplosionConfig explosionConfig;
    private Boolean angledToPath;

    PathConfigBuilder() {
    }

    public PathConfig.PathConfigBuilder alienCharacter(@NonNull AlienCharacter alienCharacter) {
      this.alienCharacter = alienCharacter;
      return this;
    }

    public PathConfig.PathConfigBuilder energy(@NonNull Integer energy) {
      this.energy = energy;
      return this;
    }

    public PathConfig.PathConfigBuilder missileConfig(MissileConfig missileConfig) {
      this.missileConfig = missileConfig;
      return this;
    }

    public PathConfig.PathConfigBuilder spawnConfig(SpawnConfig spawnConfig) {
      this.spawnConfig = spawnConfig;
      return this;
    }

    public PathConfig.PathConfigBuilder spinningConfig(SpinningConfig spinningConfig) {
      this.spinningConfig = spinningConfig;
      return this;
    }

    public PathConfig.PathConfigBuilder explosionConfig(ExplosionConfig explosionConfig) {
      this.explosionConfig = explosionConfig;
      return this;
    }

    public PathConfig.PathConfigBuilder angledToPath(Boolean angledToPath) {
      this.angledToPath = angledToPath;
      return this;
    }

    public PathConfig build() {
      return new PathConfig(alienCharacter, energy, missileConfig, spawnConfig, spinningConfig, explosionConfig, angledToPath);
    }
  }
}
