package com.danosoftware.galaxyforce.waves.config.aliens.missiles;

import com.danosoftware.galaxyforce.enumerations.AlienMissileCharacter;
import com.danosoftware.galaxyforce.enumerations.AlienMissileSpeed;
import com.danosoftware.galaxyforce.enumerations.AlienMissileType;

import lombok.NonNull;

public class MissileFiringConfig extends MissileConfig {

  private final AlienMissileType missileType;
  private final AlienMissileSpeed missileSpeed;
  private final float missileFrequency;
  private final AlienMissileCharacter missileCharacter;

  public MissileFiringConfig(
      @NonNull final AlienMissileType missileType,
      @NonNull final AlienMissileSpeed missileSpeed,
      @NonNull final AlienMissileCharacter missileCharacter,
      @NonNull final Float missileFrequency) {
    super(MissileConfigType.MISSILE);
    this.missileType = missileType;
    this.missileSpeed = missileSpeed;
    this.missileCharacter = missileCharacter;
    this.missileFrequency = missileFrequency;
  }

  public static MissileFiringConfigBuilder builder() {
    return new MissileFiringConfigBuilder();
  }

  public AlienMissileType getMissileType() {
    return this.missileType;
  }

  public AlienMissileSpeed getMissileSpeed() {
    return this.missileSpeed;
  }

  public float getMissileFrequency() {
    return this.missileFrequency;
  }

  public AlienMissileCharacter getMissileCharacter() {
    return this.missileCharacter;
  }

  public static class MissileFiringConfigBuilder {
    private @NonNull AlienMissileType missileType;
    private @NonNull AlienMissileSpeed missileSpeed;
    private @NonNull AlienMissileCharacter missileCharacter;
    private @NonNull Float missileFrequency;

    MissileFiringConfigBuilder() {
    }

    public MissileFiringConfig.MissileFiringConfigBuilder missileType(@NonNull AlienMissileType missileType) {
      this.missileType = missileType;
      return this;
    }

    public MissileFiringConfig.MissileFiringConfigBuilder missileSpeed(@NonNull AlienMissileSpeed missileSpeed) {
      this.missileSpeed = missileSpeed;
      return this;
    }

    public MissileFiringConfig.MissileFiringConfigBuilder missileCharacter(@NonNull AlienMissileCharacter missileCharacter) {
      this.missileCharacter = missileCharacter;
      return this;
    }

    public MissileFiringConfig.MissileFiringConfigBuilder missileFrequency(@NonNull Float missileFrequency) {
      this.missileFrequency = missileFrequency;
      return this;
    }

    public MissileFiringConfig build() {
      return new MissileFiringConfig(missileType, missileSpeed, missileCharacter, missileFrequency);
    }
  }
}
