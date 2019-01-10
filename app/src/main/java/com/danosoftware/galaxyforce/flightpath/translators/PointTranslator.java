package com.danosoftware.galaxyforce.flightpath.translators;

import com.danosoftware.galaxyforce.flightpath.paths.Point;

/**
 * Point translators convert supplied points
 */
interface PointTranslator {

    /**
     * Return a converted version of the supplied point
     */
    Point convert(final Point point);
}
