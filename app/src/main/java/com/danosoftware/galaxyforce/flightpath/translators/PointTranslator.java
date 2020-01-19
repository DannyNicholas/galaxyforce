package com.danosoftware.galaxyforce.flightpath.translators;

import com.danosoftware.galaxyforce.flightpath.paths.DoublePoint;

/**
 * Point translators convert supplied points
 */
interface PointTranslator {

    /**
     * Return a converted version of the supplied point
     */
    DoublePoint convert(final DoublePoint point);
}
