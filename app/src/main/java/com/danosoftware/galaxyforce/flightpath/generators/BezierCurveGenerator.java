package com.danosoftware.galaxyforce.flightpath.generators;

import com.danosoftware.galaxyforce.flightpath.dto.BezierPathDTO;
import com.danosoftware.galaxyforce.flightpath.paths.DoublePoint;
import com.danosoftware.galaxyforce.flightpath.paths.PathSpeed;
import com.danosoftware.galaxyforce.flightpath.translators.PointTranslatorChain;

import java.util.List;

import static com.danosoftware.galaxyforce.flightpath.utilities.BezierMathematics.createBezierPath;
import static com.danosoftware.galaxyforce.flightpath.utilities.PointMathematics.convertAndTranslatePoint;

/**
 * Create bezier curve from provided control points
 */
public class BezierCurveGenerator implements PathGenerator {
    private final DoublePoint start;
    private final DoublePoint startControl;
    private final DoublePoint finish;
    private final DoublePoint finishControl;
    private final int pathPoints;

    /**
     * Instantiate generator by extracting and converting the bezier data points
     * and then translating them to their new positions based on the provided
     * translators (e.g. x-axis flip).
     */
    public BezierCurveGenerator(
            BezierPathDTO bezierData,
            PointTranslatorChain translators,
            PathSpeed pathSpeed) {
        this.start = convertAndTranslatePoint(bezierData.getStart(), translators);
        this.startControl = convertAndTranslatePoint(bezierData.getStartControl(), translators);
        this.finish = convertAndTranslatePoint(bezierData.getFinish(), translators);
        this.finishControl = convertAndTranslatePoint(bezierData.getFinishControl(), translators);
        this.pathPoints = (int) (bezierData.getPathPoints() * pathSpeed.getMultiplier());
    }

    /**
     * return the bezier curve points for the current bezier curve object.
     *
     * @return array of points representing Bezier curve
     */
    @Override
    public List<DoublePoint> path() {
        return createBezierPath(
                start,
                startControl,
                finish,
                finishControl,
                pathPoints);
    }
}
