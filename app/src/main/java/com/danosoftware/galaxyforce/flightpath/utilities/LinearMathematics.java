package com.danosoftware.galaxyforce.flightpath.utilities;

import com.danosoftware.galaxyforce.flightpath.paths.DoublePoint;

import java.util.ArrayList;
import java.util.List;

import static com.danosoftware.galaxyforce.flightpath.utilities.PointMathematics.addition;
import static com.danosoftware.galaxyforce.flightpath.utilities.PointMathematics.multiply;
import static com.danosoftware.galaxyforce.flightpath.utilities.PointMathematics.subtraction;

/**
 * Linear Mathematics Utilities
 */
public final class LinearMathematics {

    private LinearMathematics() {
    }

    /**
     * return the path points points for the current linear path.
     */
    public static List<DoublePoint> createLinearPath(
            final DoublePoint start,
            final DoublePoint finish,
            final int pathPoints) {

        List<DoublePoint> path = new ArrayList<>();

        final DoublePoint delta = subtraction(finish, start);
        for (int i = 0; i <= pathPoints; i++) {

            // t -> 0 to 1
            double t = (double) i / pathPoints;

            // pathPoint = start + ((finish - start) * t)
            DoublePoint pathPoint = addition(start, multiply(delta, t));
            path.add(pathPoint);
        }

        return path;
    }
}
