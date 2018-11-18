package com.danosoftware.galaxyforce.utilities;

import com.danosoftware.galaxyforce.flightpath.paths.Point;
import com.danosoftware.galaxyforce.view.Vector2;

public class OverlapTester {
    public static boolean overlapCircles(Circle c1, Circle c2) {
        float distance = c1.center.distSquared(c2.center);
        float radiusSum = c1.radius + c2.radius;
        return distance <= radiusSum * radiusSum;
    }

    public static boolean overlapRectangles(Rectangle r1, Rectangle r2) {
        return r1.lowerLeft.x < r2.lowerLeft.x + r2.width && r1.lowerLeft.x + r1.width > r2.lowerLeft.x
                && r1.lowerLeft.y < r2.lowerLeft.y + r2.height && r1.lowerLeft.y + r1.height > r2.lowerLeft.y;
    }

    /**
     * Returns the point where the collision between two rectangles occurs. This
     * has been optimised so it should only be called once we know the two
     * supplied rectangles are definitely overlapping.
     * <p>
     * Must call overlapRectangles() first to check if overlap condition exists.
     *
     * @param r1
     * @param r2
     * @return point where the collision occurs
     */
    public static Point collisionPoint(Rectangle r1, Rectangle r2) {
        // holds co-ordinates of the corners of r1 and r2 rectangles that
        // overlap.
        float r1x;
        float r1y;
        float r2x;
        float r2y;

        /*
         * check r1 left x coordinate overlaps with the r2 rectangle's x
         * coordinates. if it does we assume r1 left x must overlaps with r2
         * right x.
         */
        if ((r1.lowerLeft.x > r2.lowerLeft.x) && (r1.lowerLeft.x < (r2.lowerLeft.x + r2.width))) {
            r1x = r1.lowerLeft.x;
            r2x = r2.lowerLeft.x + r2.width;
        }
        /*
         * Otherwise assume r1 right x must overlaps with r2 left x.
         */
        else {
            r1x = r1.lowerLeft.x + r1.width;
            r2x = r2.lowerLeft.x;
        }

        /*
         * check r1 bottom y coordinate overlaps with the r2 rectangle's y
         * coordinates. if it does we assume r1 bottom y must overlaps with r2
         * top y.
         */
        if ((r1.lowerLeft.y > r2.lowerLeft.y) && (r1.lowerLeft.y < (r2.lowerLeft.y + r2.height))) {
            r1y = r1.lowerLeft.y;
            r2y = r2.lowerLeft.y + r2.height;
        }
        /*
         * Otherwise assume r1 top y must overlaps with r2 bottom y.
         */
        else {
            r1y = r1.lowerLeft.y + r1.height;
            r2y = r2.lowerLeft.y;
        }

        /*
         * We now have the two corner coordinates that overlap. Calculate the
         * collision point as the point half-way between the two overlapping
         * corners.
         */
        float collideX = r1x + ((r2x - r1x) / 2);
        float collideY = r1y + ((r2y - r1y) / 2);

        return new Point((int) collideX, (int) collideY);
    }

    /**
     * Returns the Y position where the collision between two rectangles occurs.
     * This has been optimised so it should only be called once we know the two
     * supplied rectangles are definitely overlapping.
     * <p>
     * Must call overlapRectangles() first to check if overlap condition exists.
     *
     * @param r1
     * @param r2
     * @return point where the collision occurs
     */
    public static int collisionYPosition(Rectangle r1, Rectangle r2) {
        // holds co-ordinates of the y coordinates of r1 and r2 rectangles that
        // overlap.
        float r1y;
        float r2y;

        /*
         * check r1 bottom y coordinate overlaps with the r2 rectangle's y
         * coordinates. if it does we assume r1 bottom y must overlaps with r2
         * top y.
         */
        if ((r1.lowerLeft.y > r2.lowerLeft.y) && (r1.lowerLeft.y < (r2.lowerLeft.y + r2.height))) {
            r1y = r1.lowerLeft.y;
            r2y = r2.lowerLeft.y + r2.height;
        }
        /*
         * Otherwise assume r1 top y must overlaps with r2 bottom y.
         */
        else {
            r1y = r1.lowerLeft.y + r1.height;
            r2y = r2.lowerLeft.y;
        }

        /*
         * We now have the two y coordinates that overlap. Calculate the
         * collision point as the point half-way between the two overlapping
         * coordinates.
         */
        float collideY = r1y + ((r2y - r1y) / 2);

        return (int) collideY;
    }

    public static boolean overlapCircleRectangle(Circle c, Rectangle r) {
        float closestX = c.center.x;
        float closestY = c.center.y;

        if (c.center.x < r.lowerLeft.x) {
            closestX = r.lowerLeft.x;
        } else if (c.center.x > r.lowerLeft.x + r.width) {
            closestX = r.lowerLeft.x + r.width;
        }

        if (c.center.y < r.lowerLeft.y) {
            closestY = r.lowerLeft.y;
        } else if (c.center.y > r.lowerLeft.y + r.height) {
            closestY = r.lowerLeft.y + r.height;
        }

        return c.center.distSquared(closestX, closestY) < c.radius * c.radius;
    }

    public static boolean pointInCircle(Circle c, Vector2 p) {
        return c.center.distSquared(p) < c.radius * c.radius;
    }

    public static boolean pointInCircle(Circle c, float x, float y) {
        return c.center.distSquared(x, y) < c.radius * c.radius;
    }

    public static boolean pointInRectangle(Rectangle r, Vector2 p) {
        return r.lowerLeft.x <= p.x && r.lowerLeft.x + r.width >= p.x && r.lowerLeft.y <= p.y && r.lowerLeft.y + r.height >= p.y;
    }

    public static boolean pointInRectangle(Rectangle r, float x, float y) {
        return r.lowerLeft.x <= x && r.lowerLeft.x + r.width >= x && r.lowerLeft.y <= y && r.lowerLeft.y + r.height >= y;
    }
}
