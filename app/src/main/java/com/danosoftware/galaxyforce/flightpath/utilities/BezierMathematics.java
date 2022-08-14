package com.danosoftware.galaxyforce.flightpath.utilities;

import static com.danosoftware.galaxyforce.flightpath.utilities.PointMathematics.addition;
import static com.danosoftware.galaxyforce.flightpath.utilities.PointMathematics.multiply;

import com.danosoftware.galaxyforce.flightpath.paths.DoublePoint;
import java.util.ArrayList;
import java.util.List;

/**
 * Bezier Mathematics Utilities
 */
public final class BezierMathematics {

  private BezierMathematics() {
  }

  /**
   * return the bezier curve points for the current bezier curve path.
   */
  public static List<DoublePoint> createBezierPath(
      final DoublePoint start,
      final DoublePoint startControl,
      final DoublePoint finish,
      final DoublePoint finishControl,
      final int pathPoints) {
    List<DoublePoint> path = new ArrayList<>();

    /* calculate point for each segment and add to path */
    for (int i = 0; i <= pathPoints; i++) {
      double t = (double) i / pathPoints;
      path.add(
          calculateBezierPoint(
              start,
              startControl,
              finish,
              finishControl,
              t)
      );
    }

    return path;
  }

  /**
   * calculate current point on bezier curve based control points and on parameter t
   *
   * @return current point on bezier curve
   */
  private static DoublePoint calculateBezierPoint(final DoublePoint start,
      final DoublePoint startControl,
      final DoublePoint finish,
      final DoublePoint finishControl,
      double t) {
    double u = 1 - t;
    double tt = t * t;
    double uu = u * u;
    double uuu = uu * u;
    double ttt = tt * t;

    /* stage 1 - uuu * start */
    DoublePoint stage1 = multiply(start, uuu);

    /* stage 2 - stage 1 + (3 * uu * t * startControl) */
    DoublePoint stage2 = addition(stage1, multiply(startControl, (3 * uu * t)));

    /* stage 3 - stage 2 + ( 3 * u * tt * finishControl) */
    DoublePoint stage3 = addition(stage2, multiply(finishControl, (3 * u * tt)));

    /* stage 4 - stage 3 + (ttt * finish) */
    return addition(stage3, multiply(finish, ttt));
  }
}
