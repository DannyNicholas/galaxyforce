package com.danosoftware.galaxyforce.waves.rules;

/*
 * Holds a set of properties that describe a sub-wave (without a path).
 */
public class SubWaveRuleProperties {

  // use random x position
  private final boolean xRandom;

  // use random y position
  private final boolean yRandom;

  // use fixed x start position
  private final int xStart;

  // use fixed y start position
  private final int yStart;

  // total number of aliens in the sub-wave
  private final int numberOfAliens;

  // timing delay between each adjacent alien
  private final float delayBetweenAliens;

  // timing delay before sub-wave starts
  private final float delayOffet;

  // restart alien immediately when it reaches the end of it's path?
  // alternatively will wait until entire in-progress subwave finishes
  private final boolean restartImmediately;

  /**
   * Properties to help create a new alien sub-wave using a supplied positions (random or specified)
   * and delays in seconds.
   */
  public SubWaveRuleProperties(
      boolean xRandom,
      boolean yRandom,
      int xStart,
      int yStart,
      int numberOfAliens,
      float delayBetweenAliens,
      float delayOffet,
      boolean restartImmediately) {
    this.xRandom = xRandom;
    this.yRandom = yRandom;
    this.xStart = xStart;
    this.yStart = yStart;
    this.numberOfAliens = numberOfAliens;
    this.delayBetweenAliens = delayBetweenAliens;
    this.delayOffet = delayOffet;
    this.restartImmediately = restartImmediately;
  }

  public boolean isxRandom() {
    return xRandom;
  }

  public boolean isyRandom() {
    return yRandom;
  }

  public int getxStart() {
    return xStart;
  }

  public int getyStart() {
    return yStart;
  }

  public int getNumberOfAliens() {
    return numberOfAliens;
  }

  public float getDelayBetweenAliens() {
    return delayBetweenAliens;
  }

  public float getDelayOffet() {
    return delayOffet;
  }

  public boolean isRestartImmediately() {
    return restartImmediately;
  }
}
