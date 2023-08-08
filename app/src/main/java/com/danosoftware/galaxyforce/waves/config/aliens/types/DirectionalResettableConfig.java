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

public class DirectionalResettableConfig extends BasicAlienConfig {

  private final AlienSpeed speed;
  private final Float angle;

  public DirectionalResettableConfig(
      @NonNull final AlienCharacter alienCharacter,
      @NonNull final Integer energy,
      final MissileConfig missileConfig,
      final SpawnConfig spawnConfig,
      final SpinningConfig spinningConfig,
      final ExplosionConfig explosionConfig,
      @NonNull final AlienSpeed speed,
      @NonNull final Float angle) {
    super(
        AlienType.DIRECTIONAL_RESETTABLE,
        alienCharacter,
        energy,
        missileConfig,
        spawnConfig,
        spinningConfig,
        explosionConfig);
    this.speed = speed;
    this.angle = angle;
  }

  public static DirectionalResettableConfigBuilder builder() {
    return new DirectionalResettableConfigBuilder();
  }

  public AlienSpeed getSpeed() {
    return this.speed;
  }

  public Float getAngle() {
    return this.angle;
  }

  public static class DirectionalResettableConfigBuilder {
    private @NonNull AlienCharacter alienCharacter;
    private @NonNull Integer energy;
    private MissileConfig missileConfig;
    private SpawnConfig spawnConfig;
    private SpinningConfig spinningConfig;
    private ExplosionConfig explosionConfig;
    private @NonNull AlienSpeed speed;
    private @NonNull Float angle;

    DirectionalResettableConfigBuilder() {
    }

    public DirectionalResettableConfigBuilder alienCharacter(@NonNull AlienCharacter alienCharacter) {
      this.alienCharacter = alienCharacter;
      return this;
    }

    public DirectionalResettableConfigBuilder energy(@NonNull Integer energy) {
      this.energy = energy;
      return this;
    }

    public DirectionalResettableConfigBuilder missileConfig(MissileConfig missileConfig) {
      this.missileConfig = missileConfig;
      return this;
    }

    public DirectionalResettableConfigBuilder spawnConfig(SpawnConfig spawnConfig) {
      this.spawnConfig = spawnConfig;
      return this;
    }

    public DirectionalResettableConfigBuilder spinningConfig(SpinningConfig spinningConfig) {
      this.spinningConfig = spinningConfig;
      return this;
    }

    public DirectionalResettableConfigBuilder explosionConfig(ExplosionConfig explosionConfig) {
      this.explosionConfig = explosionConfig;
      return this;
    }

    public DirectionalResettableConfigBuilder speed(@NonNull AlienSpeed speed) {
      this.speed = speed;
      return this;
    }

    public DirectionalResettableConfigBuilder angle(@NonNull Float angle) {
      this.angle = angle;
      return this;
    }

    public DirectionalResettableConfig build() {
      return new DirectionalResettableConfig(alienCharacter, energy, missileConfig, spawnConfig, spinningConfig, explosionConfig, speed, angle);
    }
  }
}
