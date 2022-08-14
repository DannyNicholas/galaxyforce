package com.danosoftware.galaxyforce.flightpath.generators;

import static com.danosoftware.galaxyforce.flightpath.utilities.LinearMathematics.createLinearPath;
import static com.danosoftware.galaxyforce.flightpath.utilities.PointMathematics.convertAndTranslatePoint;

import com.danosoftware.galaxyforce.flightpath.dto.LinearPathDTO;
import com.danosoftware.galaxyforce.flightpath.paths.DoublePoint;
import com.danosoftware.galaxyforce.flightpath.paths.PathSpeed;
import com.danosoftware.galaxyforce.flightpath.translators.PointTranslatorChain;
import java.util.List;

/**
 * Create line from provided control points
 */
public class LinearGenerator implements PathGenerator {

  private final DoublePoint start;
  private final DoublePoint finish;
  private final int pathPoints;

  /**
   * Instantiate generator by extracting and converting the linear data points and then translating
   * them to their new positions based on the provided translators (e.g. x-axis flip).
   */
  public LinearGenerator(
      LinearPathDTO linearData,
      PointTranslatorChain translators,
      PathSpeed pathSpeed) {
    this.start = convertAndTranslatePoint(linearData.getStart(), translators);
    this.finish = convertAndTranslatePoint(linearData.getFinish(), translators);
    this.pathPoints = (int) (linearData.getPathPoints() * pathSpeed.getMultiplier());
  }

  /**
   * return the linear points for the current linear object.
   *
   * @return array of points representing line
   */
  @Override
  public List<DoublePoint> path() {
    return createLinearPath(start, finish, pathPoints);
  }
}
