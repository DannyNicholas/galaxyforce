package com.danosoftware.galaxyforce.sprites.game.missiles.aliens;

import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;

/**
 * Calculates alien missile angle of travel (in radians) and sprite rotation (in degrees) from alien
 * missile position to base position.
 * <p>
 * As an optimisation, a missile travelling straight downwards is assumed to be travelling at an
 * angle of zero degrees.
 * <p>
 * This minimises sprite rotation calculations in the normal case.
 * <p>
 * As a result, all alien missile sprites images should be designed to point downwards.
 * <p>
 * This is the opposite to how base missiles are optimised.
 */
public final class AlienMissileRotater {

  private static final double TO_DEGREES = 180f / Math.PI;
  private static final double HALF_PI_OFFSET = Math.PI / 2f;
  private static final float PI_BY_TWO = (float) Math.PI / 2f;
  private static final float TWO_PI = (float) Math.PI * 2f;

  private AlienMissileRotater() {
  }

  /**
   * Return calculation holding the missile's direction of travel and sprite rotation (in degrees).
   * This is calculated from the from alien missile position to the base's current position.
   *
   * @param missile - alien missile
   * @param base    - current base
   * @return angle calculation from missile to base
   */
  public static AlienMissileRotateCalculation calculateAngle(
      final IAlienMissile missile,
      final IBasePrimary base
  ) {
    if (base != null) {
      // calculate angle from missile position to base
      float angleInRadians = (float) Math.atan2(
          base.y() - missile.y(),
          base.x() - missile.x());

      // adjust angle so that a result more positive than PI/2
      // becomes a negative value. Gives missile a more direct
      // route to target (otherwise goes the long-way around).
      if (angleInRadians > PI_BY_TWO) {
        angleInRadians -= TWO_PI;
      }

      // calculate missile sprite rotation
      final float missileRotation = calculateRotation(angleInRadians);

      return AlienMissileRotateCalculation
          .builder()
          .angle(angleInRadians)
          .rotation(missileRotation)
          .build();
    }

    // if base is null, fire downwards
    return AlienMissileRotateCalculation
        .builder()
        .angle((float) Math.atan2(-1, 0))
        .rotation(0f)
        .build();
  }

  /**
   * Return alien missile's rotation angle in degrees from it's angle of travel (in radians). An
   * angle of zero represents a missile travelling straight downwards.
   *
   * @param angleInRadians - rotation angle in radians
   * @return missile rotation angle in degrees
   */
  public static float calculateRotation(
      final float angleInRadians
  ) {
    // we adjust our angle by PI/2 since we want our origin (0 degrees) to point downwards
    return (float) ((angleInRadians + HALF_PI_OFFSET) * TO_DEGREES);
  }

}
