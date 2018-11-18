package com.danosoftware.galaxyforce.legacy.flightpath;

import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.flightpath.dto.PathDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Create straight line from provided start/finish points that should complete
 * in supplied time.
 */
public class StraightLineTimed implements FlightPath {
    private final Point start;
    private final Point finish;
    private final float timeInSeconds;

    public StraightLineTimed(float timeInSeconds, Point start, Point finish) {
        this.start = start;
        this.finish = finish;
        this.timeInSeconds = timeInSeconds;
    }

    /**
     * return the straight line points for the current straight line object.
     *
     * @return array of points representing straight line
     */
    public List<Point> addPath() {
        List<Point> path = new ArrayList<Point>();

        /*
         * Calculate delta required to cover distance in supplied time in
         * seconds. Assumes 60 screen updates each second. Move routines will
         * compensate for this is updates are slower.
         */
        Double delta = 1 / (60D * timeInSeconds);

        for (double t = 0; t < 1; t = t + delta) {
            /* calculate current position from parametric equation */
            double xPos = start.getX() + ((finish.getX() - start.getX()) * t);
            double yPos = start.getY() + ((finish.getY() - start.getY()) * t);

            /* xPos, yPos refers to centre of alien sprite */
            Point position = new Point((int) Math.round(xPos), (int) Math.round(yPos));
            path.add(position);
        }

        return path;

    }

    @Override
    public PathDTO createDTO() {
        throw new GalaxyForceException(this.getClass().getName());
    }
}
