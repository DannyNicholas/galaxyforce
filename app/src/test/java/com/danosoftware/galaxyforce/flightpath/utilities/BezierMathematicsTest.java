package com.danosoftware.galaxyforce.flightpath.utilities;

import com.danosoftware.galaxyforce.flightpath.paths.DoublePoint;

import org.junit.Test;

import java.util.List;

import static com.danosoftware.galaxyforce.flightpath.utilities.BezierMathematics.createBezierPath;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test Bezier Mathematics Utility
 */
public class BezierMathematicsTest {

    @Test
    public void shouldCalculateBezierPath() {

        // create bezier control points
        final int pathPoints = 100;
        final DoublePoint start = new DoublePoint(10, 20);
        final DoublePoint finish = new DoublePoint(30, 40);
        final DoublePoint startControl = new DoublePoint(50, 60);
        final DoublePoint finishControl = new DoublePoint(70, 80);

        // create a bezier path from control points
        final List<DoublePoint> bezierPoints = createBezierPath(
                start,
                startControl,
                finish,
                finishControl,
                pathPoints);

        // assert path length
        final int pathLength = bezierPoints.size();
        assertThat(pathLength, is(pathPoints + 1));

        // assert start/end path points match provided control points
        assertThat(bezierPoints.get(0).getX(), is(start.getX()));
        assertThat(bezierPoints.get(0).getY(), is(start.getY()));
        assertThat(bezierPoints.get(pathLength - 1).getX(), is(finish.getX()));
        assertThat(bezierPoints.get(pathLength - 1).getY(), is(finish.getY()));
    }
}
