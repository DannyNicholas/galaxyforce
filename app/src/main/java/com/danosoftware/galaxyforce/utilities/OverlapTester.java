package com.danosoftware.galaxyforce.utilities;

import com.danosoftware.galaxyforce.view.Vector2;

public class OverlapTester {

  public static boolean overlapRectangles(Rectangle r1, Rectangle r2) {
    return r1.left < r2.right
        && r1.right > r2.left
        && r1.bottom < r2.top
        && r1.top > r2.bottom;
  }

  public static boolean pointInRectangle(Rectangle r, Vector2 p) {
    return r.left <= p.x
        && r.right >= p.x
        && r.bottom <= p.y
        && r.top >= p.y;
  }
}
