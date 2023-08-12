package com.danosoftware.galaxyforce.waves.config.aliens.exploding;

public abstract class ExplosionConfig {

  private final ExplosionConfig.ExplosionConfigType type;

  public ExplosionConfig(ExplosionConfig.ExplosionConfigType type) {
    this.type = type;
  }

  public ExplosionConfigType getType() {
    return this.type;
  }

  public enum ExplosionConfigType {
    NORMAL, NORMAL_AND_SPAWN, MULTI_EXPLOSION, FOLLOWER_DELAYED
  }
}