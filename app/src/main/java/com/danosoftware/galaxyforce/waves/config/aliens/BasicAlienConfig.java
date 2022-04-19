package com.danosoftware.galaxyforce.waves.config.aliens;

import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.ExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.missiles.MissileConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawnConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningConfig;
import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class BasicAlienConfig extends AlienConfig {

  private final int energy;
  private final AlienCharacter alienCharacter;
  private final SpawnConfig spawnConfig;
  private final MissileConfig missileConfig;
  private final SpinningConfig spinningConfig;
  private final ExplosionConfig explosionConfig;

  public BasicAlienConfig(
      @NonNull final AlienType alienType,
      @NonNull final AlienCharacter alienCharacter,
      @NonNull final Integer energy,
      final MissileConfig missileConfig,
      final SpawnConfig spawnConfig,
      final SpinningConfig spinningConfig,
      final ExplosionConfig explosionConfig) {
    super(alienType);
    this.alienCharacter = alienCharacter;
    this.energy = energy;
    this.missileConfig = missileConfig;
    this.spawnConfig = spawnConfig;
    this.spinningConfig = spinningConfig;
    this.explosionConfig = explosionConfig;
  }
}
