package com.danosoftware.galaxyforce.flightpath.paths;

import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.flightpath.dto.BezierPathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.CircularPathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.LinearPathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.PathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.PathListDTO;
import com.danosoftware.galaxyforce.flightpath.dto.PausePathDTO;
import com.danosoftware.galaxyforce.flightpath.generators.BezierCurveGenerator;
import com.danosoftware.galaxyforce.flightpath.generators.CircularGenerator;
import com.danosoftware.galaxyforce.flightpath.generators.LinearGenerator;
import com.danosoftware.galaxyforce.flightpath.generators.PathGenerator;
import com.danosoftware.galaxyforce.flightpath.generators.PauseGenerator;
import com.danosoftware.galaxyforce.flightpath.translators.PointTranslatorChain;
import com.danosoftware.galaxyforce.flightpath.utilities.PathLoader;
import java.util.ArrayList;
import java.util.List;

public final class PathFactory {

  private final static double HALF_PI = Math.PI / 2d;
  private final static double TWICE_PI = Math.PI * 2d;
  private final static double TO_DEGREES = 180f / Math.PI;

  private final PathLoader loader;
  private double lastAngle;

  public PathFactory(PathLoader loader) {
    this.loader = loader;
    this.lastAngle = 0d;
  }

  public List<PathPoint> createPath(
      Path path,
      PointTranslatorChain translators,
      PathSpeed pathSpeed) {
    List<PathPoint> pathPoints = new ArrayList<>();

    // load path data from file
    PathListDTO pathData = loader.loadPaths(path);

    double lastAngle = 0d;

    for (PathDTO pathDTO : pathData.getPathList()) {
      final PathGenerator generator;
      switch (pathDTO.getType()) {
        case BEZIER:
          BezierPathDTO bezierData = (BezierPathDTO) pathDTO;
          generator = new BezierCurveGenerator(bezierData, translators, pathSpeed);
          break;
        case LINEAR:
          LinearPathDTO linearData = (LinearPathDTO) pathDTO;
          generator = new LinearGenerator(linearData, translators, pathSpeed);
          break;
        case PAUSE:
          PausePathDTO pauseData = (PausePathDTO) pathDTO;
          generator = new PauseGenerator(pauseData, translators);
          break;
        case CIRCULAR:
          CircularPathDTO circularData = (CircularPathDTO) pathDTO;
          generator = new CircularGenerator(circularData, translators, pathSpeed);
          break;
        default:
          throw new GalaxyForceException("Unknown path type: " + pathDTO.getType().name());
      }

      pathPoints.addAll(
          createPathPoints(generator.path()));
    }
    return pathPoints;
  }

  // convert double path points into rounded integer points
  private List<PathPoint> createPathPoints(List<DoublePoint> dblPoints) {
    List<PathPoint> pathPoints = new ArrayList<>();
    for (int idx = 0; idx < dblPoints.size(); idx++) {

      DoublePoint current = dblPoints.get(idx);

      if (idx == dblPoints.size() - 1) {
        pathPoints.add(
            new PathPoint(
                (float) current.getX(),
                (float) current.getY(),
                (float) lastAngle));
      } else {
        DoublePoint next = dblPoints.get(idx + 1);

        // use last angle in cases where alien hasn't moved
        // would otherwise calculate an angle of zero
        if (current.getX() == next.getX() && current.getY() == next.getY()) {
          pathPoints.add(
              new PathPoint(
                  (float) current.getX(),
                  (float) current.getY(),
                  (float) lastAngle));
        } else {
          // calculate angle to next position
          double angleInRadians = Math.atan2(
              next.getY() - current.getY(),
              next.getX() - current.getX());

          // adjust angle so that a result more positive than PI/2
          // becomes a negative value.
          if (angleInRadians > HALF_PI) {
            angleInRadians -= TWICE_PI;
          }

          // calculate angle rotation
          final double angle =
              (angleInRadians + HALF_PI) * (TO_DEGREES);

          pathPoints.add(
              new PathPoint(
                  (float) current.getX(),
                  (float) current.getY(),
                  (float) angle));
          lastAngle = angle;
        }
      }
    }
    return pathPoints;
  }
}
