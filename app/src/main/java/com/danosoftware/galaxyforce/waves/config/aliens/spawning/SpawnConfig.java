package com.danosoftware.galaxyforce.waves.config.aliens.spawning;

import lombok.NonNull;

public abstract class SpawnConfig {

  private final SpawnType type;

  public SpawnConfig(
      @NonNull SpawnType type) {
    this.type = type;
  }

  public SpawnType getType() {
    return this.type;
  }

  public enum SpawnType {
    SPAWN, SPAWN_LIMITED, SPAWN_LIMITED_ACTIVE_ALIEN, SPAWN_AND_EXPLODE, SPAWN_ON_DEMAND
  }
}
