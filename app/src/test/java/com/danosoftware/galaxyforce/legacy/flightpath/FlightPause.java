package com.danosoftware.galaxyforce.legacy.flightpath;

import com.danosoftware.galaxyforce.flightpath.dto.PathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.PausePathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.PointDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Create pause position from provided point and time delay
 */
public class FlightPause implements FlightPath {

    private final Point position;
    private final float timeInSeconds;

    public FlightPause(float timeInSeconds, Point position) {
        this.position = position;
        this.timeInSeconds = timeInSeconds;
    }

    /**
     * return the straight line points for the current straight line object.
     *
     * @return array of points representing straight line
     */
    public List<Point> addPath() {
        List<Point> path = new ArrayList<Point>();

        /* number of points needed for pause time */
        Double delta = 1 / (60D * timeInSeconds);

        // add the number of points needed to path
        for (double t = 0; t < 1; t = t + delta) {
            path.add(new Point(position.getX(), position.getY()));
        }

        return path;
    }

    @Override
    public PathDTO createDTO() {
        return new PausePathDTO(
                timeInSeconds,
                new PointDTO(position.getX(), position.getY()));
    }

}
