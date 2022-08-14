package com.danosoftware.galaxyforce.flightpath.translators;

import com.danosoftware.galaxyforce.flightpath.paths.DoublePoint;

/**
 * Point translator that flips the point in the x-axis
 */
public class FlipXPointTranslator implements PointTranslator {

  // width of x dimension
  private final int width;

  public FlipXPointTranslator(int width) {
    this.width = width;
  }

  @Override
  public DoublePoint convert(DoublePoint point) {
    return new DoublePoint(width - point.getX(), point.getY());
  }
}
