package com.danosoftware.galaxyforce.flightpath.translators;

import com.danosoftware.galaxyforce.flightpath.paths.DoublePoint;
import java.util.ArrayList;
import java.util.List;

/**
 * Allows chaining of translators that can be executed in sequence.
 * <p>
 * This allows a complex sequence of point translations to be build-up and then executed.
 */
public class PointTranslatorChain {

  private final List<PointTranslator> translators;

  public PointTranslatorChain() {
    this.translators = new ArrayList<>();
  }

  /**
   * Clone an existing Point Translator Chain
   */
  public static PointTranslatorChain clonePointTranslatorChain(PointTranslatorChain chain) {
    PointTranslatorChain pointTranslatorChain = new PointTranslatorChain();
    for (PointTranslator translator : chain.translators) {
      pointTranslatorChain.add(translator);
    }
    return pointTranslatorChain;
  }

  /**
   * Add a new translator to the chain
   */
  public PointTranslatorChain add(final PointTranslator translator) {
    translators.add(translator);
    return this;
  }

  /**
   * Translate the supplied point using the chain of translators
   */
  public DoublePoint translate(final DoublePoint point) {
    DoublePoint convertedPoint = point;
    for (PointTranslator translator : translators) {
      convertedPoint = translator.convert(convertedPoint);
    }
    return convertedPoint;
  }
}
