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

public class DirectionalDestroyableConfig extends BasicAlienConfig {

  private final AlienSpeed speed;
  private final Float angle;

  public DirectionalDestroyableConfig(
      @NonNull final AlienCharacter alienCharacter,
      @NonNull final Integer energy,
      final MissileConfig missileConfig,
      final SpawnConfig spawnConfig,
      final SpinningConfig spinningConfig,
      final ExplosionConfig explosionConfig,
      @NonNull final AlienSpeed speed,
      @NonNull final Float angle) {
    super(
        AlienType.DIRECTIONAL_DESTROYABLE,
        alienCharacter,
        energy,
        missileConfig,
        spawnConfig,
        spinningConfig,
        explosionConfig);
    this.speed = speed;
    this.angle = angle;
  }

  public static DirectionalDestroyableConfigBuilder builder() {
    return new DirectionalDestroyableConfigBuilder();
  }

  public AlienSpeed getSpeed() {
    return this.speed;
  }

  public Float getAngle() {
    return this.angle;
  }

  public static class DirectionalDestroyableConfigBuilder {
    private @NonNull AlienCharacter alienCharacter;
    private @NonNull Integer energy;
    private MissileConfig missileConfig;
    private SpawnConfig spawnConfig;
    private SpinningConfig spinningConfig;
    private ExplosionConfig explosionConfig;
    private @NonNull AlienSpeed speed;
    private @NonNull Float angle;

    DirectionalDestroyableConfigBuilder() {
    }

    public DirectionalDestroyableConfig.DirectionalDestroyableConfigBuilder alienCharacter(@NonNull AlienCharacter alienCharacter) {
      this.alienCharacter = alienCharacter;
      return this;
    }

    public DirectionalDestroyableConfig.DirectionalDestroyableConfigBuilder energy(@NonNull Integer energy) {
      this.energy = energy;
      return this;
    }

    public DirectionalDestroyableConfig.DirectionalDestroyableConfigBuilder missileConfig(MissileConfig missileConfig) {
      this.missileConfig = missileConfig;
      return this;
    }

    public DirectionalDestroyableConfig.DirectionalDestroyableConfigBuilder spawnConfig(SpawnConfig spawnConfig) {
      this.spawnConfig = spawnConfig;
      return this;
    }

    public DirectionalDestroyableConfig.DirectionalDestroyableConfigBuilder spinningConfig(SpinningConfig spinningConfig) {
      this.spinningConfig = spinningConfig;
      return this;
    }

    public DirectionalDestroyableConfig.DirectionalDestroyableConfigBuilder explosionConfig(ExplosionConfig explosionConfig) {
      this.explosionConfig = explosionConfig;
      return this;
    }

    public DirectionalDestroyableConfig.DirectionalDestroyableConfigBuilder speed(@NonNull AlienSpeed speed) {
      this.speed = speed;
      return this;
    }

    public DirectionalDestroyableConfig.DirectionalDestroyableConfigBuilder angle(@NonNull Float angle) {
      this.angle = angle;
      return this;
    }

    public DirectionalDestroyableConfig build() {
      return new DirectionalDestroyableConfig(alienCharacter, energy, missileConfig, spawnConfig, spinningConfig, explosionConfig, speed, angle);
    }
  }
}
