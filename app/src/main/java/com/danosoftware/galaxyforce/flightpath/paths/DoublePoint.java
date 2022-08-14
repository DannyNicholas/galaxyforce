package com.danosoftware.galaxyforce.flightpath.paths;

/**
 * Represents a point object (an x-y co-ordinate). Normally used to represent a position on a path.
 */
public class DoublePoint {

  private final double x;
  private final double y;

  public DoublePoint(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }
}
