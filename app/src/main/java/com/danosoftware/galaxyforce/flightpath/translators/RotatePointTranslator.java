package com.danosoftware.galaxyforce.flightpath.translators;

import static com.danosoftware.galaxyforce.constants.GameConstants.ANGLE_TO_RADIANS_MULTIPLIER;
import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_HEIGHT;
import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_WIDTH;
import static com.danosoftware.galaxyforce.constants.GameConstants.SCREEN_MID_X;
import static com.danosoftware.galaxyforce.constants.GameConstants.SCREEN_MID_Y;

import com.danosoftware.galaxyforce.flightpath.paths.DoublePoint;

/**
 * Point translator that rotates a point around the screen centre.
 * <p>
 * Due to unequal x, y screen dimensions, rescaling must occur to re-fit rotated points within the
 * screen dimensions.
 * <p>
 * As a result of this re-scaling, only +/-90 degree rotations are supported.
 */
public class RotatePointTranslator implements PointTranslator {

  public enum Rotation {
    CLOCKWISE(-90),
    ANTI_CLOCKWISE(90);

    private final int angle;

    Rotation(final int angle) {
      this.angle = angle;
    }

    public int getAngle() {
      return this.angle;
    }
  }

  // rotation angle in radians
  private final double angleRadians;

  // centre of rotation
  private final double centreX;
  private final double centreY;

  // x scaling fe
  private final double scalarX;
  private final double scalarY;

  public RotatePointTranslator(Rotation rotation) {
    this.angleRadians = rotation.getAngle() * ANGLE_TO_RADIANS_MULTIPLIER;
    this.centreX = SCREEN_MID_X;
    this.centreY = SCREEN_MID_Y;
    this.scalarX = (double) GAME_WIDTH / GAME_HEIGHT;
    this.scalarY = (double) GAME_HEIGHT / GAME_WIDTH;
  }

  @Override
  public DoublePoint convert(DoublePoint point) {
    final double rotatedX = Math.cos(angleRadians)
        * (point.getX() - centreX)
        - Math.sin(angleRadians)
        * (point.getY() - centreY)
        + centreX;
    final double rotatedY = Math.sin(angleRadians)
        * (point.getX() - centreX)
        + Math.cos(angleRadians)
        * (point.getY() - centreY)
        + centreY;

    // since our x and y are different dimensions,
    // we must re-scale so it fits the screen after a rotation.
    final double xDeltaFromCentre = rotatedX - SCREEN_MID_X;
    final double yDeltaFromCentre = rotatedY - SCREEN_MID_Y;

    final double xScaledFromCentre = xDeltaFromCentre * scalarX;
    final double yScaledFromCentre = yDeltaFromCentre * scalarY;

    return new DoublePoint(
        SCREEN_MID_X + xScaledFromCentre,
        SCREEN_MID_Y + yScaledFromCentre);
  }
}
