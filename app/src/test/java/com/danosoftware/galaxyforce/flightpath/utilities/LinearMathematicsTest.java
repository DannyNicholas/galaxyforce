package com.danosoftware.galaxyforce.flightpath.utilities;

import com.danosoftware.galaxyforce.flightpath.paths.Point;

import org.junit.Test;

import java.util.List;

import static com.danosoftware.galaxyforce.flightpath.utilities.LinearMathematics.createLinearPath;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test Linear Mathematics Utility
 */
public class LinearMathematicsTest {

    @Test
    public void shouldCalculateLinearPath() {

        // create linear control points
        final int pathPoints = 100;
        final Point start = new Point(10, 20);
        final Point finish = new Point(30, 40);

        // create a linear path from control points
        final List<Point> linearPoints = createLinearPath(
                start,
                finish,
                pathPoints);

        // assert path length
        final int pathLength = linearPoints.size();
        assertThat(pathLength, is(pathPoints + 1));

        // assert start/end path points match provided control points
        assertThat(linearPoints.get(0).getX(), is(start.getX()));
        assertThat(linearPoints.get(0).getY(), is(start.getY()));
        assertThat(linearPoints.get(pathLength - 1).getX(), is(finish.getX()));
        assertThat(linearPoints.get(pathLength - 1).getY(), is(finish.getY()));
    }
}
