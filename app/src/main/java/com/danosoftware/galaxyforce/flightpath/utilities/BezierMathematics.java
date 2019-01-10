package com.danosoftware.galaxyforce.flightpath.utilities;

import com.danosoftware.galaxyforce.flightpath.paths.Point;

import java.util.ArrayList;
import java.util.List;

import static com.danosoftware.galaxyforce.flightpath.utilities.PointMathematics.addition;
import static com.danosoftware.galaxyforce.flightpath.utilities.PointMathematics.multiply;

/**
 * Bezier Mathematics Utilities
 */
public final class BezierMathematics {

    private BezierMathematics() {
    }

    /**
     * return the bezier curve points for the current bezier curve path.
     */
    public static List<Point> createBezierPath(
            final Point start,
            final Point startControl,
            final Point finish,
            final Point finishControl,
            final int pathPoints) {
        List<Point> path = new ArrayList<>();

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
    private static Point calculateBezierPoint(final Point start,
                                              final Point startControl,
                                              final Point finish,
                                              final Point finishControl,
                                              double t) {
        double u = 1 - t;
        double tt = t * t;
        double uu = u * u;
        double uuu = uu * u;
        double ttt = tt * t;

        /* stage 1 - uuu * start */
        Point stage1 = multiply(start, uuu);

        /* stage 2 - stage 1 + (3 * uu * t * startControl) */
        Point stage2 = addition(stage1, multiply(startControl, (3 * uu * t)));

        /* stage 3 - stage 2 + ( 3 * u * tt * finishControl) */
        Point stage3 = addition(stage2, multiply(finishControl, (3 * u * tt)));

        /* stage 4 - stage 3 + (ttt * finish) */
        return addition(stage3, multiply(finish, ttt));
    }
}
