package com.danosoftware.galaxyforce.waves.config.aliens.exploding;

public class NormalExplosionConfig extends ExplosionConfig {

  public NormalExplosionConfig() {
    super(ExplosionConfigType.NORMAL);
  }

  public static NormalExplosionConfigBuilder builder() {
    return new NormalExplosionConfigBuilder();
  }

  public static class NormalExplosionConfigBuilder {
    NormalExplosionConfigBuilder() {
    }

    public NormalExplosionConfig build() {
      return new NormalExplosionConfig();
    }
  }
}
