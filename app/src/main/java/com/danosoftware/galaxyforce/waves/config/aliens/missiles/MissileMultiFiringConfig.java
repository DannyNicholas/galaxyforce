package com.danosoftware.galaxyforce.waves.config.aliens.missiles;

import java.util.Collection;

import lombok.NonNull;

/**
 * Represents a multi-missile config. That is a missile config that holds multiple missile configs.
 * Useful if an alien has multiple missile behaviours (e.g alien fires downwards missiles and guided
 * missiles at different frequencies).
 */
public class MissileMultiFiringConfig extends MissileConfig {

  // multiple missile configs
  private final Collection<MissileFiringConfig> missileConfigs;

  public MissileMultiFiringConfig(
      @NonNull final Collection<MissileFiringConfig> missileConfigs) {
    super(MissileConfigType.MULTI_MISSILE);
    this.missileConfigs = missileConfigs;
  }

  public static MissileMultiFiringConfigBuilder builder() {
    return new MissileMultiFiringConfigBuilder();
  }

  public Collection<MissileFiringConfig> getMissileConfigs() {
    return this.missileConfigs;
  }

  public static class MissileMultiFiringConfigBuilder {
    private @NonNull Collection<MissileFiringConfig> missileConfigs;

    MissileMultiFiringConfigBuilder() {
    }

    public MissileMultiFiringConfig.MissileMultiFiringConfigBuilder missileConfigs(@NonNull Collection<MissileFiringConfig> missileConfigs) {
      this.missileConfigs = missileConfigs;
      return this;
    }

    public MissileMultiFiringConfig build() {
      return new MissileMultiFiringConfig(missileConfigs);
    }
  }
}
