package com.danosoftware.galaxyforce.legacy.flightpath;

/**
 * @author Danny
 * <p>
 * class of Point objects used to store each x/y co-ordinate in path of
 * alien
 */
public class Point {

    private int x;
    private int y;

    public Point(int xInt, int yInt) {
        x = xInt;
        y = yInt;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * return new Point object where x and y has been scaled by the provided
     * scalar mutliplier.
     *
     * @param scalarMultiplier
     * @return
     */
    public Point scalarMultiplication(double scalarMultiplier) {
        // calculate scaled x and y points
        double scaledX = this.getX() * scalarMultiplier;
        double scaledY = this.getY() * scalarMultiplier;

        // create new Point at new scaled x, y
        return new Point((int) scaledX, (int) scaledY);

    }

    /**
     * return new point object that is the addition of this point to provided
     * point
     *
     * @param addPoint
     * @return
     */
    public Point addition(Point addPoint) {
        // calculate x and y point addition
        int addX = this.getX() + addPoint.getX();
        int addY = this.getY() + addPoint.getY();

        // create new point of the addition of two points
        return new Point(addX, addY);

    }

}
