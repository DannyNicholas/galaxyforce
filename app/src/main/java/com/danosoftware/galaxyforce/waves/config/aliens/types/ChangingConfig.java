package com.danosoftware.galaxyforce.waves.config.aliens.types;

import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.config.aliens.BasicAlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.ExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.missiles.MissileConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawnConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningConfig;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class ChangingConfig extends BasicAlienConfig {

  private final Boolean angledToPath;
  private final List<AlienCharacter> alienCharacters;

  @Builder
  public ChangingConfig(
      @NonNull final List<AlienCharacter> alienCharacters,
      @NonNull final Integer energy,
      final MissileConfig missileConfig,
      final SpawnConfig spawnConfig,
      final SpinningConfig spinningConfig,
      final ExplosionConfig explosionConfig,
      final Boolean angledToPath) {
    super(
        AlienType.CHANGING,
        !alienCharacters.isEmpty() ? alienCharacters.get(0) : AlienCharacter.NULL,
        energy,
        missileConfig,
        spawnConfig,
        spinningConfig,
        explosionConfig);
    this.angledToPath = angledToPath != null ? angledToPath : false;
    this.alienCharacters = alienCharacters;

    if (alienCharacters.isEmpty()) {
      throw new GalaxyForceException("Changing Config has no alien characters");
    }
  }
}
