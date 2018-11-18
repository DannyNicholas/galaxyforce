package com.danosoftware.galaxyforce.legacy.flightpath;

import com.danosoftware.galaxyforce.flightpath.dto.LinearPathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.PathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.PointDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Create straight line from provided start/finish points and delta multiplier
 */
public class StraightLine implements FlightPath {
    private final Point start;
    private final Point finish;

    /* distance alien can move each cycle in pixels */
    private final int distancePerCycle;

    public StraightLine(int distancePerCycle, Point start, Point finish) {
        this.start = start;
        this.finish = finish;
        this.distancePerCycle = distancePerCycle;
    }

    /**
     * return the straight line points for the current straight line object.
     *
     * @return array of points representing straight line
     */
    public List<Point> addPath() {
        List<Point> path = new ArrayList<Point>();

        /* calculate x, y delta and distance to be travelled */
        double xDelta = finish.getX() - start.getX();
        double yDelta = finish.getY() - start.getY();
        double distance = Math.sqrt(Math.pow(xDelta, 2) + Math.pow(yDelta, 2));

        /* number of points on line */
        double delta = distancePerCycle / distance;

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

        double xDelta = finish.getX() - start.getX();
        double yDelta = finish.getY() - start.getY();
        double distance = Math.sqrt(Math.pow(xDelta, 2) + Math.pow(yDelta, 2));

        return new LinearPathDTO(
                (int) (distance / distancePerCycle),
                new PointDTO(start.getX(), start.getY()),
                new PointDTO(finish.getX(), finish.getY()));
    }
}
