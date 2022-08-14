package com.danosoftware.galaxyforce.sprites.game.starfield;

import static com.danosoftware.galaxyforce.waves.utilities.Randomiser.random;
import static com.danosoftware.galaxyforce.waves.utilities.Randomiser.randomFloat;

/**
 * Star field of multiple animated stars.
 */
public class StarField {

  /* number of stars to show */
  private static final int NUMBER_OF_STARS = 125;

  // Star speed will either be slow, normal or fast.
  // Stars with an index less than "slow index" will be slow.
  // Stars with an index greater than "fast index" will be fast.
  // All others will be normal speed.
  private static final int SLOW_STAR_INDEX = NUMBER_OF_STARS / 3;
  private static final int FAST_STAR_INDEX = SLOW_STAR_INDEX * 2;

  // stars in our star-field
  private final Star[] starField;

  // height of our playing area
  private final int height;

  private float stateTime;

  // star colours to choose from when creating a random colour.
  // weighted so white stars are more common
  private static final StarColour[] STAR_COLOURS = {
      StarColour.WHITE, StarColour.BLUE, StarColour.RED, StarColour.WHITE
  };

  public StarField(int width, int height) {
    this.starField = setupStars(width, height);
    this.height = height;
    this.stateTime = 0f;
  }

  /**
   * Animate our star-field. Each star will descend according to time since last animation frame and
   * speed of star.
   *
   * @param deltaTime - time since last animation frame
   */
  public void animate(float deltaTime) {

    stateTime += deltaTime;

    // compute distances moved for each star speed
    final float slowDistanceDelta = distanceDelta(StarSpeed.SLOW, deltaTime);
    final float normalDistanceDelta = distanceDelta(StarSpeed.MEDIUM, deltaTime);
    final float fastDistanceDelta = distanceDelta(StarSpeed.FAST, deltaTime);

    for (int idx = 0; idx < starField.length; idx++) {

      // move star down screen according to speed.
      float starY = starField[idx].y;
      if (idx < SLOW_STAR_INDEX) {
        starY -= slowDistanceDelta;
      } else if (idx < FAST_STAR_INDEX) {
        starY -= normalDistanceDelta;
      } else {
        starY -= fastDistanceDelta;
      }

      // if star has reached the bottom of screen then re-position at the top.
      if (starY < 0) {
        starField[idx].y = height + starY;
      } else {
        starField[idx].y = starY;
      }
    }
  }

  /**
   * return the star-field
   */
  public Star[] getStarField() {
    return starField;
  }

  /**
   * initialise background stars. returns an array of stars in random positions with random
   * colours.
   */
  private Star[] setupStars(int width, int height) {
    Star[] stars = new Star[NUMBER_OF_STARS];

    for (int idx = 0; idx < stars.length; idx++) {
      float x = width * randomFloat();
      float y = height * randomFloat();
      StarColour starColour = getRandomStarColour();
      stars[idx] = new Star(x, y, starColour);
    }

    return stars;
  }

  /**
   * compute the distance a star has moved based on the speed and time elapsed.
   */
  private float distanceDelta(StarSpeed speed, float timeElapsed) {
    return (speed.getPixelsPerSecond() * timeElapsed) % height;
  }

  /**
   * return a random star colour
   */
  private StarColour getRandomStarColour() {
    int colourIndex = (int) (STAR_COLOURS.length * random());
    return STAR_COLOURS[colourIndex];
  }
}
