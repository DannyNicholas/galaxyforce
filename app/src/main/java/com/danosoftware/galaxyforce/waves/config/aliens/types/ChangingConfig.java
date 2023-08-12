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

import lombok.NonNull;

public class ChangingConfig extends AlienConfig {

  private final Boolean angledToPath;
  private final List<AlienCharacterWithEnergy> alienCharacters;
  private final int energy;
  private final SpawnConfig spawnConfig;
  private final MissileConfig missileConfig;
  private final SpinningConfig spinningConfig;
  private final ExplosionConfig explosionConfig;

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

  public static ChangingConfigBuilder builder() {
    return new ChangingConfigBuilder();
  }

  public AlienCharacter getFirstCharacter() {
    return alienCharacters.get(0).getAlienCharacter();
  }

  public Boolean getAngledToPath() {
    return this.angledToPath;
  }

  public List<AlienCharacterWithEnergy> getAlienCharacters() {
    return this.alienCharacters;
  }

  public int getEnergy() {
    return this.energy;
  }

  public SpawnConfig getSpawnConfig() {
    return this.spawnConfig;
  }

  public MissileConfig getMissileConfig() {
    return this.missileConfig;
  }

  public SpinningConfig getSpinningConfig() {
    return this.spinningConfig;
  }

  public ExplosionConfig getExplosionConfig() {
    return this.explosionConfig;
  }

  public static class ChangingConfigBuilder {
    private @NonNull List<AlienCharacterWithEnergy> alienCharacters;
    private @NonNull Integer energy;
    private MissileConfig missileConfig;
    private SpawnConfig spawnConfig;
    private SpinningConfig spinningConfig;
    private ExplosionConfig explosionConfig;
    private Boolean angledToPath;

    ChangingConfigBuilder() {
    }

    public ChangingConfig.ChangingConfigBuilder alienCharacters(@NonNull List<AlienCharacterWithEnergy> alienCharacters) {
      this.alienCharacters = alienCharacters;
      return this;
    }

    public ChangingConfig.ChangingConfigBuilder energy(@NonNull Integer energy) {
      this.energy = energy;
      return this;
    }

    public ChangingConfig.ChangingConfigBuilder missileConfig(MissileConfig missileConfig) {
      this.missileConfig = missileConfig;
      return this;
    }

    public ChangingConfig.ChangingConfigBuilder spawnConfig(SpawnConfig spawnConfig) {
      this.spawnConfig = spawnConfig;
      return this;
    }

    public ChangingConfig.ChangingConfigBuilder spinningConfig(SpinningConfig spinningConfig) {
      this.spinningConfig = spinningConfig;
      return this;
    }

    public ChangingConfig.ChangingConfigBuilder explosionConfig(ExplosionConfig explosionConfig) {
      this.explosionConfig = explosionConfig;
      return this;
    }

    public ChangingConfig.ChangingConfigBuilder angledToPath(Boolean angledToPath) {
      this.angledToPath = angledToPath;
      return this;
    }

    public ChangingConfig build() {
      return new ChangingConfig(alienCharacters, energy, missileConfig, spawnConfig, spinningConfig, explosionConfig, angledToPath);
    }
  }
}
