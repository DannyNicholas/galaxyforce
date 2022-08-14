package com.danosoftware.galaxyforce.flightpath.generators;

import static com.danosoftware.galaxyforce.flightpath.utilities.PauseMathematics.createPausePath;
import static com.danosoftware.galaxyforce.flightpath.utilities.PointMathematics.convertAndTranslatePoint;

import com.danosoftware.galaxyforce.flightpath.dto.PausePathDTO;
import com.danosoftware.galaxyforce.flightpath.paths.DoublePoint;
import com.danosoftware.galaxyforce.flightpath.translators.PointTranslatorChain;
import java.util.List;

/**
 * Create pause path from provided control points. A pause path represents an alien staying in the
 * same position for a set time (in seconds).
 */
public class PauseGenerator implements PathGenerator {

  private final DoublePoint pausePosition;
  private final float pauseTime;

  /**
   * Instantiate generator by extracting and converting the data point and then translating to it's
   * new position based on the provided translators (e.g. x-axis flip).
   */
  public PauseGenerator(PausePathDTO pauseData, PointTranslatorChain translators) {
    this.pausePosition = convertAndTranslatePoint(pauseData.getPosition(), translators);
    this.pauseTime = pauseData.getPauseTime();
  }

  @Override
  public List<DoublePoint> path() {
    return createPausePath(pausePosition, pauseTime);
  }
}
