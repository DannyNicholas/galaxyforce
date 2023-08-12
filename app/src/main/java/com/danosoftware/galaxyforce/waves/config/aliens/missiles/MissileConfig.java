package com.danosoftware.galaxyforce.waves.config.aliens.missiles;

public abstract class MissileConfig {

  private final MissileConfigType type;

  public MissileConfig(MissileConfigType type) {
    this.type = type;
  }

  public MissileConfigType getType() {
    return this.type;
  }

  public enum MissileConfigType {
    MISSILE, MULTI_MISSILE
  }
}
