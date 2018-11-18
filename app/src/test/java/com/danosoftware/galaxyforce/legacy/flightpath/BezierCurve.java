package com.danosoftware.galaxyforce.legacy.flightpath;

import com.danosoftware.galaxyforce.flightpath.dto.BezierPathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.PathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.PointDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Create bezier curve from provided control points
 */
public class BezierCurve implements FlightPath {

    Point start = null;
    Point startControl = null;
    Point finishControl = null;
    Point finish = null;
    Integer segments = null;

    public BezierCurve(Integer segments, Point start, Point startControl, Point finishControl, Point finish) {
        this.start = start;
        this.startControl = startControl;
        this.finishControl = finishControl;
        this.finish = finish;
        this.segments = segments;
    }

    /**
     * return the bezier curve points for the current bezier curve object.
     *
     * @return array of points representing Bezier curve
     */
    public List<Point> addPath() {

        List<Point> path = new ArrayList<Point>();

        /* calculate point for each segment and add to path */
        for (int i = 0; i <= segments; i++) {
            double t = (double) i / segments;
            Point position = calculateBezierPoint(t);
            path.add(position);
        }

        return path;
    }

    @Override
    public PathDTO createDTO() {
        return new BezierPathDTO(
                segments,
                new PointDTO(start.getX(), start.getY()),
                new PointDTO(startControl.getX(), startControl.getY()),
                new PointDTO(finish.getX(), finish.getY()),
                new PointDTO(finishControl.getX(), finishControl.getY()));
    }

    /**
     * calculate current point on bezier curve based on parameter t
     *
     * @param t
     * @return current point on bezier curve
     */
    private Point calculateBezierPoint(double t) {
        double u = 1 - t;
        double tt = t * t;
        double uu = u * u;
        double uuu = uu * u;
        double ttt = tt * t;

        /* stage 1 - uuu * start */
        Point stage1 = start.scalarMultiplication(uuu);

        /* stage 2 - stage 1 + (3 * uu * t * startControl) */
        Point stage2Interim = startControl.scalarMultiplication(3 * uu * t);
        Point stage2 = stage1.addition(stage2Interim);

        /* stage 3 - stage 2 + ( 3 * u * tt * finishControl) */
        Point stage3Interim = finishControl.scalarMultiplication(3 * u * tt);
        Point stage3 = stage2.addition(stage3Interim);

        /* stage 4 - stage 3 + (ttt * finish) */
        Point stage4Interim = finish.scalarMultiplication(ttt);
        Point stage4 = stage3.addition(stage4Interim);

        return stage4;
    }
}
