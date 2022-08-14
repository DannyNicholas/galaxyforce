package com.danosoftware.galaxyforce.flightpath.utilities;

import com.danosoftware.galaxyforce.flightpath.dto.PointDTO;
import com.danosoftware.galaxyforce.flightpath.paths.DoublePoint;
import com.danosoftware.galaxyforce.flightpath.translators.PointTranslatorChain;


/**
 * Point Mathematics Utilities
 */
public final class PointMathematics {

  private PointMathematics() {
  }

  /**
   * Convert from point DTO (used for JSON deserialization) to point object used across app
   */
  private static DoublePoint convertPoint(PointDTO pointData) {
    return new DoublePoint(pointData.getX(), pointData.getY());
  }

  /**
   * Convert from point DTO (used for JSON deserialization) to point object used across app. Then
   * translate to new position based on provided point translators.
   */
  public static DoublePoint convertAndTranslatePoint(PointDTO pointData,
      PointTranslatorChain translators) {
    return translators.translate(
        convertPoint(pointData)
    );
  }

  /**
   * Return a point position scaled by multiplier.
   */
  public static DoublePoint multiply(DoublePoint point, double multiplier) {
    return new DoublePoint(
        point.getX() * multiplier,
        point.getY() * multiplier);
  }

  /**
   * Return a point position representing the sum of two supplied points
   */
  public static DoublePoint addition(DoublePoint point1, DoublePoint point2) {
    return new DoublePoint(
        point1.getX() + point2.getX(),
        point1.getY() + point2.getY());
  }

  /**
   * Return a point position representing the subtraction of two supplied points
   */
  public static DoublePoint subtraction(DoublePoint point1, DoublePoint point2) {
    return new DoublePoint(
        point1.getX() - point2.getX(),
        point1.getY() - point2.getY());
  }
}