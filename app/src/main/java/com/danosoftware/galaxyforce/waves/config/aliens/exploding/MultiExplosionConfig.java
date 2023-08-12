package com.danosoftware.galaxyforce.waves.config.aliens.exploding;

import lombok.NonNull;

public class MultiExplosionConfig extends ExplosionConfig {

  private final Integer numberOfExplosions;
  private final Float maximumExplosionStartTime;
  // config of additional explosions created
  private final ExplosionConfig explosionConfig;

  public MultiExplosionConfig(
      @NonNull final Integer numberOfExplosions,
      @NonNull final Float maximumExplosionStartTime,
      final ExplosionConfig explosionConfig
  ) {
    super(ExplosionConfigType.MULTI_EXPLOSION);
    this.numberOfExplosions = numberOfExplosions;
    this.maximumExplosionStartTime = maximumExplosionStartTime;
    this.explosionConfig = explosionConfig;
  }

  public static MultiExplosionConfigBuilder builder() {
    return new MultiExplosionConfigBuilder();
  }

  public Integer getNumberOfExplosions() {
    return this.numberOfExplosions;
  }

  public Float getMaximumExplosionStartTime() {
    return this.maximumExplosionStartTime;
  }

  public ExplosionConfig getExplosionConfig() {
    return this.explosionConfig;
  }

  public static class MultiExplosionConfigBuilder {
    private @NonNull Integer numberOfExplosions;
    private @NonNull Float maximumExplosionStartTime;
    private ExplosionConfig explosionConfig;

    MultiExplosionConfigBuilder() {
    }

    public MultiExplosionConfig.MultiExplosionConfigBuilder numberOfExplosions(@NonNull Integer numberOfExplosions) {
      this.numberOfExplosions = numberOfExplosions;
      return this;
    }

    public MultiExplosionConfig.MultiExplosionConfigBuilder maximumExplosionStartTime(@NonNull Float maximumExplosionStartTime) {
      this.maximumExplosionStartTime = maximumExplosionStartTime;
      return this;
    }

    public MultiExplosionConfig.MultiExplosionConfigBuilder explosionConfig(ExplosionConfig explosionConfig) {
      this.explosionConfig = explosionConfig;
      return this;
    }

    public MultiExplosionConfig build() {
      return new MultiExplosionConfig(numberOfExplosions, maximumExplosionStartTime, explosionConfig);
    }
  }
}
