package com.danosoftware.galaxyforce.waves.config.aliens.types;

import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.ExplosionConfig;

import lombok.NonNull;

public class StaticExplosionConfig extends AlienConfig {

  private final AlienCharacter alienCharacter;
  private final ExplosionConfig explosionConfig;

  public StaticExplosionConfig(
      @NonNull final AlienCharacter alienCharacter,
      final ExplosionConfig explosionConfig) {
    super(AlienType.STATIC_EXPLOSION);
    this.alienCharacter = alienCharacter;
    this.explosionConfig = explosionConfig;
  }

  public static StaticExplosionConfigBuilder builder() {
    return new StaticExplosionConfigBuilder();
  }

  public AlienCharacter getAlienCharacter() {
    return this.alienCharacter;
  }

  public ExplosionConfig getExplosionConfig() {
    return this.explosionConfig;
  }

  public static class StaticExplosionConfigBuilder {
    private @NonNull AlienCharacter alienCharacter;
    private ExplosionConfig explosionConfig;

    StaticExplosionConfigBuilder() {
    }

    public StaticExplosionConfig.StaticExplosionConfigBuilder alienCharacter(@NonNull AlienCharacter alienCharacter) {
      this.alienCharacter = alienCharacter;
      return this;
    }

    public StaticExplosionConfig.StaticExplosionConfigBuilder explosionConfig(ExplosionConfig explosionConfig) {
      this.explosionConfig = explosionConfig;
      return this;
    }

    public StaticExplosionConfig build() {
      return new StaticExplosionConfig(alienCharacter, explosionConfig);
    }
  }
}
