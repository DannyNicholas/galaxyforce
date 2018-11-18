package com.danosoftware.galaxyforce.legacy.flightpath;

import com.danosoftware.galaxyforce.flightpath.dto.BezierPathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.PathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.PathListDTO;
import com.danosoftware.galaxyforce.flightpath.generators.BezierCurveGenerator;
import com.danosoftware.galaxyforce.flightpath.translators.PointTranslatorChain;

import java.util.ArrayList;
import java.util.List;

import static com.danosoftware.galaxyforce.flightpath.utilities.PathLoader.loadPaths;

public class PathFactory {

    public static final boolean X_INVERT_TRUE = true;
    public static final boolean X_INVERT_FALSE = false;
    public static final boolean Y_INVERT_TRUE = true;
    public static final boolean Y_INVERT_FALSE = false;

    public static List<Point> createPath(Path path, boolean xInvert, boolean yInvert, int xOffset, int yOffset, int width, int height) {
        List<Point> alienPath = new ArrayList<Point>();

        /* compile list of flight path points from provided path */
        for (FlightPath pathItem : path.getPathList()) {
            alienPath.addAll(pathItem.addPath());
        }

        /*
         * correct path if needed. path can be offset by x,y. path can be
         * inverted in x or y axis.
         */
        if (xInvert || yInvert || xOffset != 0 || yOffset != 0) {
            for (Point currentPoint : alienPath) {

                currentPoint.setX(currentPoint.getX() + xOffset);
                currentPoint.setY(currentPoint.getY() + yOffset);

                if (xInvert) {
                    currentPoint.setX(width - currentPoint.getX());
                }

                if (yInvert) {
                    currentPoint.setY(height - currentPoint.getY());
                }

            }
        }

        // temporary test of new JSON loading
        PathListDTO pathData = loadPaths("testPathData.json");
        List<com.danosoftware.galaxyforce.flightpath.paths.Point> points = new ArrayList<>();
        for (PathDTO aPath : pathData.getPathList()) {
            BezierPathDTO bezierData = (BezierPathDTO) aPath;
            BezierCurveGenerator generator = new BezierCurveGenerator(bezierData, new PointTranslatorChain());
            points.addAll(generator.path());
        }
        alienPath = new ArrayList<>();
        for (com.danosoftware.galaxyforce.flightpath.paths.Point aPoint : points) {
            alienPath.add(new Point(aPoint.getX(), aPoint.getY()));
        }


        return alienPath;
    }
}
