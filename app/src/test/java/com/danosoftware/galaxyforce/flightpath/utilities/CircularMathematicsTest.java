package com.danosoftware.galaxyforce.flightpath.utilities;

import com.danosoftware.galaxyforce.flightpath.paths.PathSpeed;
import com.danosoftware.galaxyforce.flightpath.paths.Point;

import org.junit.Test;

import java.util.List;

import static com.danosoftware.galaxyforce.flightpath.utilities.CircularMathematics.createCircularPath;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test Circular Mathematics Utility
 */
public class CircularMathematicsTest {

    @Test
    public void shouldCalculateCircularPath() {

        // create circular control points
        final double piMultiplier = 1;
        final Point centre = new Point(0, 0);

        // create a linear path from control points
        final List<Point> linearPoints = createCircularPath(
                centre,
                piMultiplier,
                PathSpeed.NORMAL);

        // assert path length
        final int pathLength = linearPoints.size();
        assertThat(pathLength, is(210));

        // assert start/end path points match provided control points
        assertThat(linearPoints.get(0).getX(), is(centre.getX() + 300));
        assertThat(linearPoints.get(0).getY(), is(centre.getY()));
        assertThat(linearPoints.get(pathLength - 1).getX(), is(centre.getX() - 195));
        assertThat(linearPoints.get(pathLength - 1).getY(), is(centre.getY() - 1));
    }
}
