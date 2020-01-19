package com.danosoftware.galaxyforce.flightpath.translators;

import com.danosoftware.galaxyforce.flightpath.paths.DoublePoint;

/**
 * Point translator that offsets the point in the x-axis
 */
public class OffsetXPointTranslator implements PointTranslator {

    // offset in x dimension
    private final int xOffset;

    public OffsetXPointTranslator(int xOffset) {
        this.xOffset = xOffset;
    }

    @Override
    public DoublePoint convert(DoublePoint point) {
        return new DoublePoint(point.getX() + xOffset, point.getY());
    }
}
