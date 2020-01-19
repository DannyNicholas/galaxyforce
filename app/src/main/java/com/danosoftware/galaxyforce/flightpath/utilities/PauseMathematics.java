package com.danosoftware.galaxyforce.flightpath.utilities;

import com.danosoftware.galaxyforce.flightpath.paths.DoublePoint;

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
     */
    public static List<DoublePoint> createPausePath(
            final DoublePoint pausePosition,
            final float pauseTime) {

        List<DoublePoint> path = new ArrayList<>();

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
