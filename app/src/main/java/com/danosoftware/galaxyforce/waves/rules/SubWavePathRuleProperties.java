package com.danosoftware.galaxyforce.waves.rules;

import com.danosoftware.galaxyforce.flightpath.paths.Path;
import com.danosoftware.galaxyforce.flightpath.paths.PathSpeed;
import com.danosoftware.galaxyforce.flightpath.translators.PointTranslatorChain;

/*
 * Holds a set of properties that describe a sub-wave (including a path).
 */
public class SubWavePathRuleProperties {

  // the path the subwave aliens will follow
  private final Path path;

  private final PathSpeed pathSpeed;

  // total number of aliens in the sub-wave
  private final int numberOfAliens;

  // timing delay between each adjacent alien
  private final float delayBetweenAliens;

  // timing delay before sub-wave starts
  private final float delayOffset;

  // restart alien immediately when it reaches the end of it's path?
  // alternatively will wait until entire in-progress subwave finishes
  private final boolean restartImmediately;

  // optional translators that can change the path (e.g. flip in x-axis)
  private final PointTranslatorChain translators;


  /**
   * Create a new alien sub-wave using a supplied path and delays in seconds.
   */
  public SubWavePathRuleProperties(
      final Path path,
      final PathSpeed pathSpeed,
      final int numberOfAliens,
      final float delayBetweenAliens,
      final float delayOffset,
      final boolean restartImmediately) {
    this.path = path;
    this.pathSpeed = pathSpeed;
    this.numberOfAliens = numberOfAliens;
    this.delayBetweenAliens = delayBetweenAliens;
    this.delayOffset = delayOffset;
    this.restartImmediately = restartImmediately;

    // creates an empty translator chain
    this.translators = new PointTranslatorChain();
  }

  /**
   * Create a new alien sub-wave using a supplied path and delays in seconds. Plus translators to
   * alter the path.
   */
  public SubWavePathRuleProperties(
      final Path path,
      final PathSpeed pathSpeed,
      final int numberOfAliens,
      final float delayBetweenAliens,
      final float delayOffset,
      final boolean restartImmediately,
      final PointTranslatorChain translators) {
    this.path = path;
    this.pathSpeed = pathSpeed;
    this.numberOfAliens = numberOfAliens;
    this.delayBetweenAliens = delayBetweenAliens;
    this.delayOffset = delayOffset;
    this.restartImmediately = restartImmediately;
    this.translators = translators;
  }

  public Path getPath() {
    return path;
  }

  public PathSpeed getPathSpeed() {
    return pathSpeed;
  }

  public int getNumberOfAliens() {
    return numberOfAliens;
  }

  public float getDelayBetweenAliens() {
    return delayBetweenAliens;
  }

  public float getDelayOffset() {
    return delayOffset;
  }

  public boolean isRestartImmediately() {
    return restartImmediately;
  }

  public PointTranslatorChain getTranslators() {
    return translators;
  }
}
