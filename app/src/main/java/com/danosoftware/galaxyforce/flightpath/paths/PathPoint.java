package com.danosoftware.galaxyforce.flightpath.paths;

/**
 * Represents a point object (an x-y co-ordinate).
 * Normally used to represent a position on a path.
 */
public class PathPoint {

    private final int x;
    private final int y;
    private final int angle;

    public PathPoint(int x, int y, int angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAngle() {
        return angle;
    }
}
