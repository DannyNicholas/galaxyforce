package com.danosoftware.galaxyforce.flightpath.translators;

import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_HEIGHT;
import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_WIDTH;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.danosoftware.galaxyforce.flightpath.paths.DoublePoint;
import org.junit.jupiter.api.Test;

/**
 * Checks that a chain of point translators correctly converts an example point
 */
public class TranslatorChainTest {

  @Test
  public void translateTest() {

    DoublePoint point = new DoublePoint(10, 20);

    // build up a chain of translators
    PointTranslatorChain translatorChain = new PointTranslatorChain()
        .add(new FlipXPointTranslator(100))
        .add(new FlipYPointTranslator(200))
        .add(new OffsetXPointTranslator(50))
        .add(new OffsetYPointTranslator(75));

    // converts the point using the build chain
    // x = 10 -> flip:(100-10) = 90 -> offset:(90+50) = 140
    // y = 20 -> flip:(200-20) = 180 -> offset:(180+75) = 255
    DoublePoint convertedPoint = translatorChain.translate(point);

    assertThat(convertedPoint.getX(), is(140D));
    assertThat(convertedPoint.getY(), is(255D));
  }

  @Test
  public void translateRotateAntiClockwiseTopRightTest() {

    DoublePoint point = new DoublePoint(GAME_WIDTH, GAME_HEIGHT);

    // build up a chain of translators
    PointTranslatorChain translatorChain = new PointTranslatorChain()
        .add(new RotatePointTranslator(RotatePointTranslator.Rotation.ANTI_CLOCKWISE));

    // rotates point by +90 degrees (anti-clockwise)
    // x = GAME_WIDTH -> rotate(90) -> scaled -> 0
    // y = GAME_HEIGHT -> rotate(90) -> scaled -> GAME_HEIGHT
    DoublePoint convertedPoint = translatorChain.translate(point);
    assertThat((int) convertedPoint.getX(), is(0));
    assertThat((int) convertedPoint.getY(), is(GAME_HEIGHT));
  }

  @Test
  public void translateRotateAntiClockwiseTopLeftTest() {

    DoublePoint point = new DoublePoint(0, GAME_HEIGHT);

    // build up a chain of translators
    PointTranslatorChain translatorChain = new PointTranslatorChain()
        .add(new RotatePointTranslator(RotatePointTranslator.Rotation.ANTI_CLOCKWISE));

    // rotates point by +90 degrees (anti-clockwise)
    // x = 0 -> rotate(90) -> scaled -> 0
    // y = GAME_HEIGHT -> rotate(90) -> scaled -> 0
    DoublePoint convertedPoint = translatorChain.translate(point);
    assertThat((int) convertedPoint.getX(), is(0));
    assertThat((int) convertedPoint.getY(), is(0));
  }

  @Test
  public void translateRotateAntiClockwiseBottomLeftTest() {

    DoublePoint point = new DoublePoint(0, 0);

    // build up a chain of translators
    PointTranslatorChain translatorChain = new PointTranslatorChain()
        .add(new RotatePointTranslator(RotatePointTranslator.Rotation.ANTI_CLOCKWISE));

    // rotates point by +90 degrees (anti-clockwise)
    // x = 0 -> rotate(90) -> scaled -> GAME_WIDTH
    // y = 0 -> rotate(90) -> scaled -> 0
    DoublePoint convertedPoint = translatorChain.translate(point);
    assertThat((int) convertedPoint.getX(), is(GAME_WIDTH));
    assertThat((int) convertedPoint.getY(), is(0));
  }

  @Test
  public void translateRotateAntiClockwiseBottomRightTest() {

    DoublePoint point = new DoublePoint(GAME_WIDTH, 0);

    // build up a chain of translators
    PointTranslatorChain translatorChain = new PointTranslatorChain()
        .add(new RotatePointTranslator(RotatePointTranslator.Rotation.ANTI_CLOCKWISE));

    // rotates point by +90 degrees (anti-clockwise)
    // x = GAME_WIDTH -> rotate(90) -> scaled -> GAME_WIDTH
    // y = 0 -> rotate(90) -> scaled -> GAME_HEIGHT
    DoublePoint convertedPoint = translatorChain.translate(point);
    assertThat((int) convertedPoint.getX(), is(GAME_WIDTH));
    assertThat((int) convertedPoint.getY(), is(GAME_HEIGHT));
  }

  @Test
  public void translateRotateClockwiseTopRightTest() {

    DoublePoint point = new DoublePoint(GAME_WIDTH, GAME_HEIGHT);

    // build up a chain of translators
    PointTranslatorChain translatorChain = new PointTranslatorChain()
        .add(new RotatePointTranslator(RotatePointTranslator.Rotation.CLOCKWISE));

    // rotates point by -90 degrees (clockwise)
    // x = GAME_WIDTH -> rotate(-90) -> scaled -> GAME_WIDTH
    // y = GAME_HEIGHT -> rotate(-90) -> scaled -> 0
    DoublePoint convertedPoint = translatorChain.translate(point);
    assertThat((int) convertedPoint.getX(), is(GAME_WIDTH));
    assertThat((int) convertedPoint.getY(), is(0));
  }

  @Test
  public void translateRotateClockwiseTopLeftTest() {

    DoublePoint point = new DoublePoint(0, GAME_HEIGHT);

    // build up a chain of translators
    PointTranslatorChain translatorChain = new PointTranslatorChain()
        .add(new RotatePointTranslator(RotatePointTranslator.Rotation.CLOCKWISE));

    // rotates point by -90 degrees (clockwise)
    // x = 0 -> rotate(-90) -> scaled -> GAME_WIDTH
    // y = GAME_HEIGHT -> rotate(-90) -> scaled -> GAME_HEIGHT
    DoublePoint convertedPoint = translatorChain.translate(point);
    assertThat((int) convertedPoint.getX(), is(GAME_WIDTH));
    assertThat((int) convertedPoint.getY(), is(GAME_HEIGHT));
  }

  @Test
  public void translateRotateClockwiseBottomLeftTest() {

    DoublePoint point = new DoublePoint(0, 0);

    // build up a chain of translators
    PointTranslatorChain translatorChain = new PointTranslatorChain()
        .add(new RotatePointTranslator(RotatePointTranslator.Rotation.CLOCKWISE));

    // rotates point by -90 degrees (clockwise)
    // x = 0 -> rotate(-90) -> scaled -> 0
    // y = 0 -> rotate(-90) -> scaled -> GAME_HEIGHT
    DoublePoint convertedPoint = translatorChain.translate(point);
    assertThat((int) convertedPoint.getX(), is(0));
    assertThat((int) convertedPoint.getY(), is(GAME_HEIGHT));
  }

  @Test
  public void translateRotateClockwiseBottomRightTest() {

    DoublePoint point = new DoublePoint(GAME_WIDTH, 0);

    // build up a chain of translators
    PointTranslatorChain translatorChain = new PointTranslatorChain()
        .add(new RotatePointTranslator(RotatePointTranslator.Rotation.CLOCKWISE));

    // rotates point by -90 degrees (clockwise)
    // x = GAME_WIDTH -> rotate(-90) -> scaled -> 0
    // y = 0 -> rotate(-90) -> scaled -> 0
    DoublePoint convertedPoint = translatorChain.translate(point);
    assertThat((int) convertedPoint.getX(), is(0));
    assertThat((int) convertedPoint.getY(), is(0));
  }
}
