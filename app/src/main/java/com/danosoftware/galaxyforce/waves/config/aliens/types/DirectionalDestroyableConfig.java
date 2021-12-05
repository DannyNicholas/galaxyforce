package com.danosoftware.galaxyforce.waves.config.aliens.types;

import com.danosoftware.galaxyforce.enumerations.AlienSpeed;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.config.aliens.BasicAlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.ExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.missiles.MissileConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawnConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningConfig;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class DirectionalDestroyableConfig extends BasicAlienConfig {

  private final AlienSpeed speed;
  private final Float angle;

  @Builder
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
}
