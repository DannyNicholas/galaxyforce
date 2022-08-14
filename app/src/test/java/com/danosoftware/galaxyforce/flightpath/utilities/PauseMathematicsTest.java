package com.danosoftware.galaxyforce.flightpath.utilities;

import static com.danosoftware.galaxyforce.flightpath.utilities.PauseMathematics.createPausePath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import com.danosoftware.galaxyforce.flightpath.paths.DoublePoint;
import java.util.List;
import org.junit.Test;

/**
 * Test Pause Mathematics Utility
 */
public class PauseMathematicsTest {

  @Test
  public void shouldCalculatePausePath() {

    // create pause control points
    final float pauseTimeInSeconds = 0.5f;
    final DoublePoint pausePosition = new DoublePoint(10, 20);

    // create a pause path from control points
    final List<DoublePoint> pausePoints = createPausePath(
        pausePosition,
        pauseTimeInSeconds);

    // assert path length is equal to 30 (0.5 seconds pause)
    final int pathLength = pausePoints.size();
    assertThat(pathLength, equalTo(30));

    // assert all path points match provided pause points
    for (DoublePoint point : pausePoints) {
      assertThat(point.getX(), equalTo(pausePosition.getX()));
      assertThat(point.getY(), equalTo(pausePosition.getY()));

    }
  }
}
