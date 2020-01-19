package com.danosoftware.galaxyforce.flightpath.utilities;

import com.danosoftware.galaxyforce.flightpath.paths.DoublePoint;

import org.junit.Test;

import static com.danosoftware.galaxyforce.flightpath.utilities.PointMathematics.addition;
import static com.danosoftware.galaxyforce.flightpath.utilities.PointMathematics.multiply;
import static com.danosoftware.galaxyforce.flightpath.utilities.PointMathematics.subtraction;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test Point Mathematics Utility Methods
 */
public class PointMathematicsTest {

    @Test
    public void multiplyTest() {
        final DoublePoint point = new DoublePoint(10, 20);
        final DoublePoint multipliedPoint = multiply(point, 1.5);
        assertThat(multipliedPoint.getX(), is(15D));
        assertThat(multipliedPoint.getY(), is(30D));
    }

    @Test
    public void additionTest() {
        final DoublePoint point1 = new DoublePoint(10, 20);
        final DoublePoint point2 = new DoublePoint(90, 30);
        final DoublePoint addedPoint = addition(point1, point2);
        assertThat(addedPoint.getX(), is(100D));
        assertThat(addedPoint.getY(), is(50D));
    }

    @Test
    public void subtractionTest() {
        final DoublePoint point1 = new DoublePoint(100, 50);
        final DoublePoint point2 = new DoublePoint(90, 30);
        final DoublePoint subtractedPoint = subtraction(point1, point2);
        assertThat(subtractedPoint.getX(), is(10D));
        assertThat(subtractedPoint.getY(), is(20D));
    }
}
