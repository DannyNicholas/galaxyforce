package com.danosoftware.galaxyforce.flightpath.utilities;

import com.danosoftware.galaxyforce.flightpath.dto.PointDTO;
import com.danosoftware.galaxyforce.flightpath.paths.Point;
import com.danosoftware.galaxyforce.flightpath.translators.PointTranslatorChain;


/**
 * Point Mathematics Utilities
 */
public final class PointMathematics {

    private PointMathematics() {
    }

    /**
     * Convert from point DTO (used for JSON deserialization)
     * to point object used across app
     */
    private static Point convertPoint(PointDTO pointData) {
        return new Point(pointData.getX(), pointData.getY());
    }

    /**
     * Convert from point DTO (used for JSON deserialization)
     * to point object used across app. Then translate to new position
     * based on provided point translators.
     */
    public static Point convertAndTranslatePoint(PointDTO pointData, PointTranslatorChain translators) {
        return translators.translate(
                convertPoint(pointData)
        );
    }

    /**
     * Return a point position scaled by multiplier.
     */
    public static Point multiply(Point point, double multiplier) {
        return new Point(
                (int) (point.getX() * multiplier),
                (int) (point.getY() * multiplier));
    }

    /**
     * Return a point position representing the sum of two supplied points
     */
    public static Point addition(Point point1, Point point2) {
        return new Point(
                point1.getX() + point2.getX(),
                point1.getY() + point2.getY());
    }

    /**
     * Return a point position representing the subtraction of two supplied points
     */
    public static Point subtraction(Point point1, Point point2) {
        return new Point(
                point1.getX() - point2.getX(),
                point1.getY() - point2.getY());
    }
}