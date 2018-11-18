package com.danosoftware.galaxyforce.legacy.flightpath;

import com.danosoftware.galaxyforce.flightpath.dto.CircularPathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.PathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.PointDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Create straight line from provided start/finish points and delta multiplier
 */
public class CircularPath implements FlightPath {

    /* references point representing centre of circle */
    Point centre = null;

    /*
     * PI multiplier for how many times path goes around circle e.g. 2 = 2xPI or
     * one whole circle
     */
    Double piMulitplier = null;

    public CircularPath(Point centre, Double piMulitplier) {
        this.centre = centre;
        this.piMulitplier = piMulitplier;
    }

    /**
     * return the circular path points for the current circular path object.
     *
     * @return array of points representing circular path
     */
    public List<Point> addPath() {

        List<Point> path = new ArrayList<Point>();

        double radius = 300;

        double centreX = centre.getX();
        double centreY = centre.getY();

        for (double t = 0; t < piMulitplier * Math.PI; t = t + 0.015) {
            /* calculate position based on centre, radius and angle */
            double xPos = centreX + (radius * Math.cos(-t));
            double yPos = centreY + (radius * Math.sin(-t));

            /* xPos, yPos refers to centre of alien sprite */
            Point position = new Point((int) xPos, (int) yPos);
            path.add(position);

            radius = radius - 0.5;
        }

        return path;

    }

    @Override
    public PathDTO createDTO() {
        return new CircularPathDTO(
                new PointDTO(centre.getX(), centre.getY()),
                piMulitplier);
    }
}
