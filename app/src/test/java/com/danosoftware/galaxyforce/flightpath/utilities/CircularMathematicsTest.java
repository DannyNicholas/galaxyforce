package com.danosoftware.galaxyforce.flightpath.utilities;

import static com.danosoftware.galaxyforce.flightpath.utilities.CircularMathematics.createCircularPath;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.danosoftware.galaxyforce.flightpath.paths.DoublePoint;
import com.danosoftware.galaxyforce.flightpath.paths.PathSpeed;
import java.util.List;
import org.junit.Test;

/**
 * Test Circular Mathematics Utility
 */
public class CircularMathematicsTest {

  @Test
  public void shouldCalculateCircularPath() {

    // create circular control points
    final double piMultiplier = 1;
    final DoublePoint centre = new DoublePoint(0, 0);

    // create a linear path from control points
    final List<DoublePoint> linearPoints = createCircularPath(
        centre,
        piMultiplier,
        PathSpeed.NORMAL);

    // assert path length
    final int pathLength = linearPoints.size();
    assertThat(pathLength, is(210));

    // assert start/end path points match provided control points
    assertThat(linearPoints.get(0).getX(), is(centre.getX() + 300));
    assertThat(linearPoints.get(0).getY(), is(centre.getY()));
    assertThat(
        Math.round(linearPoints.get(pathLength - 1).getX()),
        is(Math.round(centre.getX() - 195)));
    assertThat(Math.round(linearPoints.get(pathLength - 1).getY()),
        is(Math.round(centre.getY() - 1)));
  }
}
