package com.danosoftware.galaxyforce.sprites.game.missiles.aliens;

/**
 * Holds the calculated angle of travel and sprite rotation of an alien missile.
 */
public final class AlienMissileRotateCalculation {

  private final float angle;
  private final float rotation;

  AlienMissileRotateCalculation(float angle, float rotation) {
    this.angle = angle;
    this.rotation = rotation;
  }

  public static AlienMissileRotateCalculationBuilder builder() {
    return new AlienMissileRotateCalculationBuilder();
  }

  public float getAngle() {
    return this.angle;
  }

  public float getRotation() {
    return this.rotation;
  }

  public static class AlienMissileRotateCalculationBuilder {
    private float angle;
    private float rotation;

    AlienMissileRotateCalculationBuilder() {
    }

    public AlienMissileRotateCalculation.AlienMissileRotateCalculationBuilder angle(float angle) {
      this.angle = angle;
      return this;
    }

    public AlienMissileRotateCalculation.AlienMissileRotateCalculationBuilder rotation(float rotation) {
      this.rotation = rotation;
      return this;
    }

    public AlienMissileRotateCalculation build() {
      return new AlienMissileRotateCalculation(angle, rotation);
    }
  }
}
