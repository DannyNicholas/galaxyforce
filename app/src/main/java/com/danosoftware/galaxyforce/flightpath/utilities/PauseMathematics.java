package com.danosoftware.galaxyforce.flightpath.utilities;

import com.danosoftware.galaxyforce.flightpath.paths.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Pause Mathematics Utilities
 */
public class PauseMathematics {

    private PauseMathematics() {
    }

    /**
     * return the path points points for the current pause path.
     * a pause path represents an alien staying in the same position
     * for a set time.
     *
     * @param pausePosition
     * @param pauseTime
     * @return array of points representing Linear path
     */
    public static List<Point> createPausePath(
            final Point pausePosition,
            final float pauseTime) {

        List<Point> path = new ArrayList<>();

        // number of points to create to pause of wanted time
        // e.g. 1 second pause requires 60 points
        int numberPoints = (int) (60 * pauseTime);

        // add the number of points needed to path
        for (int t = 0; t < numberPoints; t++) {
            path.add(pausePosition);
        }

        return path;
    }
}
