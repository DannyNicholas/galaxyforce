package com.danosoftware.galaxyforce.waves.config.aliens.exploding;

import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawnConfig;

import lombok.NonNull;

public class SpawningExplosionConfig extends ExplosionConfig {

  // holds the spawn config when alien explodes
  private final SpawnConfig spawnConfig;

  public SpawningExplosionConfig(
      @NonNull final SpawnConfig spawnConfig) {
    super(ExplosionConfigType.NORMAL_AND_SPAWN);
    this.spawnConfig = spawnConfig;
  }

  public static SpawningExplosionConfigBuilder builder() {
    return new SpawningExplosionConfigBuilder();
  }

  public SpawnConfig getSpawnConfig() {
    return this.spawnConfig;
  }

  public static class SpawningExplosionConfigBuilder {
    private @NonNull SpawnConfig spawnConfig;

    SpawningExplosionConfigBuilder() {
    }

    public SpawningExplosionConfig.SpawningExplosionConfigBuilder spawnConfig(@NonNull SpawnConfig spawnConfig) {
      this.spawnConfig = spawnConfig;
      return this;
    }

    public SpawningExplosionConfig build() {
      return new SpawningExplosionConfig(spawnConfig);
    }
  }
}
