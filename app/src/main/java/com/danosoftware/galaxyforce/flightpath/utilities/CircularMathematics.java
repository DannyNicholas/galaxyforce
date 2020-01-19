package com.danosoftware.galaxyforce.flightpath.utilities;

import com.danosoftware.galaxyforce.flightpath.paths.DoublePoint;
import com.danosoftware.galaxyforce.flightpath.paths.PathSpeed;

import java.util.ArrayList;
import java.util.List;

/**
 * Circular Mathematics Utilities
 */
public final class CircularMathematics {

    private CircularMathematics() {
    }

    /**
     * return the points for the current circular path.
     */
    public static List<DoublePoint> createCircularPath(
            final DoublePoint centre,
            final double piMultiplier,
            final PathSpeed pathSpeed) {
        List<DoublePoint> path = new ArrayList<>();

        double radius = 300;

        final double centreX = centre.getX();
        final double centreY = centre.getY();
        final double delta = 0.015 * pathSpeed.getMultiplier();

        for (double t = 0; t < piMultiplier * Math.PI; t = t + delta) {

            // calculate position based on centre, radius and angle
            double xPos = centreX + (radius * Math.cos(-t));
            double yPos = centreY + (radius * Math.sin(-t));
            DoublePoint position = new DoublePoint(xPos, yPos);
            path.add(position);

            // reduce radius for next interation
            radius = radius - 0.5;
        }

        return path;
    }
}
