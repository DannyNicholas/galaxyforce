package com.danosoftware.galaxyforce.flightpath.generators;

import com.danosoftware.galaxyforce.flightpath.paths.Point;

import java.util.List;

/**
 * Implementations provide a path consisting of a list of points.
 *
 * These paths can be chained together to form complex paths.
 */
public interface PathGenerator {

    /**
     * returns a list of path points
     * @return points
     */
    List<Point> path();
}
