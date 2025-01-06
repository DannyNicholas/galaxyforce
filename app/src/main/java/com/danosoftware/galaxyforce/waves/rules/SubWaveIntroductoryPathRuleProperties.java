package com.danosoftware.galaxyforce.waves.rules;

import com.danosoftware.galaxyforce.flightpath.paths.Path;
import com.danosoftware.galaxyforce.flightpath.paths.PathSpeed;
import com.danosoftware.galaxyforce.flightpath.translators.PointTranslatorChain;

/*
 * Holds a set of properties that describe a sub-wave (including an introductory and repeating path).
 */
public class SubWaveIntroductoryPathRuleProperties {

  private final Path introductoryPath;
  private final Path repeatingPath;

  private final PathSpeed pathSpeed;

  // total number of aliens in the sub-wave
  private final int numberOfAliens;

  // timing delay between each adjacent alien
  private final float delayBetweenAliens;

  // timing delay before sub-wave starts
  private final float delayOffset;

  // optional translators that can change the path (e.g. flip in x-axis)
  private final PointTranslatorChain translators;


  /**
   * Create a new alien sub-wave using a supplied path and delays in seconds.
   */
  public SubWaveIntroductoryPathRuleProperties(
      final Path introductoryPath,
      final Path repeatingPath,
      final PathSpeed pathSpeed,
      final int numberOfAliens,
      final float delayBetweenAliens,
      final float delayOffset) {
    this.introductoryPath = introductoryPath;
    this.repeatingPath = repeatingPath;
    this.pathSpeed = pathSpeed;
    this.numberOfAliens = numberOfAliens;
    this.delayBetweenAliens = delayBetweenAliens;
    this.delayOffset = delayOffset;

    // creates an empty translator chain
    this.translators = new PointTranslatorChain();
  }

  /**
   * Create a new alien sub-wave using a supplied path and delays in seconds. Plus translators to
   * alter the path.
   */
  public SubWaveIntroductoryPathRuleProperties(
      final Path introductoryPath,
      final Path repeatingPath,
      final PathSpeed pathSpeed,
      final int numberOfAliens,
      final float delayBetweenAliens,
      final float delayOffset,
      final PointTranslatorChain translators) {
    this.introductoryPath = introductoryPath;
    this.repeatingPath = repeatingPath;
    this.pathSpeed = pathSpeed;
    this.numberOfAliens = numberOfAliens;
    this.delayBetweenAliens = delayBetweenAliens;
    this.delayOffset = delayOffset;
    this.translators = translators;
  }

  public Path getIntroductoryPath() {
    return introductoryPath;
  }

  public Path getRepeatingPath() {
    return repeatingPath;
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

  public PointTranslatorChain getTranslators() {
    return translators;
  }
}
