package com.danosoftware.galaxyforce.flightpath.generators;

import com.danosoftware.galaxyforce.flightpath.paths.Point;
import com.danosoftware.galaxyforce.flightpath.dto.BezierPathDTO;
import com.danosoftware.galaxyforce.flightpath.translators.PointTranslatorChain;

import java.util.List;

import static com.danosoftware.galaxyforce.flightpath.utilities.BezierMathematics.createBezierPath;
import static com.danosoftware.galaxyforce.flightpath.utilities.PointMathematics.convertAndTranslatePoint;

/**
 * Create bezier curve from provided control points
 */
public class BezierCurveGenerator implements PathGenerator
{
    private final Point start;
    private final Point startControl;
    private final Point finish;
    private final Point finishControl;
    private final int pathPoints;

    /**
     * Instantiate generator by extracting and converting the bezier data points
     * and then translating them to their new positions based on the provided
     * translators (e.g. x-axis flip).
     *
     * @param bezierData
     * @param translators
     */
    public BezierCurveGenerator(BezierPathDTO bezierData, PointTranslatorChain translators)
    {
        this.start = convertAndTranslatePoint(bezierData.getStart(), translators);
        this.startControl = convertAndTranslatePoint(bezierData.getStartControl(), translators);
        this.finish = convertAndTranslatePoint(bezierData.getFinish(), translators);
        this.finishControl = convertAndTranslatePoint(bezierData.getFinishControl(), translators);
        this.pathPoints = bezierData.getPathPoints();
    }

    /**
     * return the bezier curve points for the current bezier curve object.
     * 
     * @return array of points representing Bezier curve
     */
    @Override
    public List<Point> path()
    {
        return createBezierPath(
                start,
                startControl,
                finish,
                finishControl,
                pathPoints);
    }
}
