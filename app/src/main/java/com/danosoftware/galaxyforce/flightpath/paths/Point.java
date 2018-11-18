package com.danosoftware.galaxyforce.flightpath.paths;

/**
 * Represents a point object (an x-y co-ordinate).
 * Normally used to represent a position on a path.
 */
public class Point {

    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
