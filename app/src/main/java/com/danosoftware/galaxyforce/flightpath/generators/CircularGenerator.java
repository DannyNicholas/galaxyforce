package com.danosoftware.galaxyforce.flightpath.generators;

import com.danosoftware.galaxyforce.flightpath.dto.CircularPathDTO;
import com.danosoftware.galaxyforce.flightpath.paths.Point;
import com.danosoftware.galaxyforce.flightpath.translators.PointTranslatorChain;

import java.util.List;

import static com.danosoftware.galaxyforce.flightpath.utilities.CircularMathematics.createCircularPath;
import static com.danosoftware.galaxyforce.flightpath.utilities.PointMathematics.convertAndTranslatePoint;

/**
 * Create circular path from provided control points
 */
public class CircularGenerator implements PathGenerator {

    private final Point centre;
    private final Double piMultiplier;

    /**
     * Instantiate generator by extracting and converting the circular data points
     * and then translating them to their new positions based on the provided
     * translators (e.g. x-axis flip).
     *
     * @param circularData
     * @param translators
     */
    public CircularGenerator(final CircularPathDTO circularData, final PointTranslatorChain translators) {
        this.centre = convertAndTranslatePoint(circularData.getCentre(), translators);
        this.piMultiplier = circularData.getPiMultiplier();
    }


    @Override
    public List<Point> path() {

        return createCircularPath(centre, piMultiplier);
    }
}
