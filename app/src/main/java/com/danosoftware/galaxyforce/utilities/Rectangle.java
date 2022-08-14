package com.danosoftware.galaxyforce.utilities;

public class Rectangle {

  public final float left;
  public final float right;
  public final float top;
  public final float bottom;

  /**
   * Create a rectangle from a centre x,y and value representing half the width and height.
   */
  public Rectangle(float x, float y, float halfWidth, float halfHeight) {
    this.left = x - halfWidth;
    this.right = x + halfWidth;
    this.bottom = y - halfHeight;
    this.top = y + halfHeight;
  }
}
