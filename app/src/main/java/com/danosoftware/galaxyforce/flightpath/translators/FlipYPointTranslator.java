package com.danosoftware.galaxyforce.flightpath.translators;

import com.danosoftware.galaxyforce.flightpath.paths.DoublePoint;

/**
 * Point translator that flips the point in the y-axis
 */
public class FlipYPointTranslator implements PointTranslator {

  // height of y dimension
  private final int height;

  public FlipYPointTranslator(int height) {
    this.height = height;
  }

  @Override
  public DoublePoint convert(DoublePoint point) {
    return new DoublePoint(point.getX(), height - point.getY());
  }
}
