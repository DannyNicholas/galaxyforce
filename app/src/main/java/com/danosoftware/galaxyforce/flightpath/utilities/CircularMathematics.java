package com.danosoftware.galaxyforce.flightpath.utilities;

import com.danosoftware.galaxyforce.flightpath.paths.PathSpeed;
import com.danosoftware.galaxyforce.flightpath.paths.Point;

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
    public static List<Point> createCircularPath(
            final Point centre,
            final double piMultiplier,
            final PathSpeed pathSpeed) {
        List<Point> path = new ArrayList<>();

        double radius = 300;

        final int centreX = centre.getX();
        final int centreY = centre.getY();
        final double delta = 0.015 * pathSpeed.getMultiplier();

        for (double t = 0; t < piMultiplier * Math.PI; t = t + delta) {

            // calculate position based on centre, radius and angle
            int xPos = centreX + (int) (radius * Math.cos(-t));
            int yPos = centreY + (int) (radius * Math.sin(-t));
            Point position = new Point(xPos, yPos);
            path.add(position);

            // reduce radius for next interation
            radius = radius - 0.5;
        }

        return path;
    }
}
