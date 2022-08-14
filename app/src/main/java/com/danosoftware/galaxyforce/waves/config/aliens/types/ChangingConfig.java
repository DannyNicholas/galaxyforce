package com.danosoftware.galaxyforce.waves.config.aliens.types;

import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienCharacterWithEnergy;
import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.ExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.missiles.MissileConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawnConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningConfig;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class ChangingConfig extends AlienConfig {

  private final Boolean angledToPath;
  private final List<AlienCharacterWithEnergy> alienCharacters;
  private final int energy;
  private final SpawnConfig spawnConfig;
  private final MissileConfig missileConfig;
  private final SpinningConfig spinningConfig;
  private final ExplosionConfig explosionConfig;

  @Builder
  public ChangingConfig(
      @NonNull final List<AlienCharacterWithEnergy> alienCharacters,
      @NonNull final Integer energy,
      final MissileConfig missileConfig,
      final SpawnConfig spawnConfig,
      final SpinningConfig spinningConfig,
      final ExplosionConfig explosionConfig,
      final Boolean angledToPath) {
    super(AlienType.CHANGING);
    this.alienCharacters = alienCharacters;
    this.energy = energy;
    this.missileConfig = missileConfig;
    this.spawnConfig = spawnConfig;
    this.spinningConfig = spinningConfig;
    this.explosionConfig = explosionConfig;
    this.angledToPath = angledToPath != null ? angledToPath : false;

    if (alienCharacters.isEmpty()) {
      throw new GalaxyForceException("Changing Config has no alien characters");
    }
  }

  public AlienCharacter getFirstCharacter() {
    return alienCharacters.get(0).getAlienCharacter();
  }
}
