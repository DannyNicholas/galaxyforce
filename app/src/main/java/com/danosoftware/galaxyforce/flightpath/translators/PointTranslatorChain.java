package com.danosoftware.galaxyforce.flightpath.translators;

import com.danosoftware.galaxyforce.flightpath.paths.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows chaining of translators that can be executed in sequence.
 * <p>
 * This allows a complex sequence of point translations to be build-up
 * and then executed.
 */
public class PointTranslatorChain {

    private final List<PointTranslator> translators;

    public PointTranslatorChain() {
        this.translators = new ArrayList<>();
    }

    /**
     * Add a new translator to the chain
     *
     * @param translator
     * @return
     */
    public PointTranslatorChain add(final PointTranslator translator) {
        translators.add(translator);
        return this;
    }

    /**
     * Translate the supplied point using the chain of translators
     *
     * @param point
     * @return
     */
    public Point translate(final Point point) {
        Point convertedPoint = point;
        for (PointTranslator translator : translators) {
            convertedPoint = translator.convert(convertedPoint);
        }
        return convertedPoint;
    }
}
