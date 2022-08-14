package com.danosoftware.galaxyforce.sprites.game.aliens;

/**
 * Implemented by aliens that can be reset and replayed once their pass has finished.
 */
public interface IResettableAlien extends IAlien {

  /**
   * sets alien back to the beginning with an amended time-delay offset.
   */
  void reset(float offset);

  /**
   * has alien reached the end of it's pass? This could be the end of a followed path or could have
   * moved off-screen.
   */
  boolean isEndOfPass();

  /**
   * Set alien to end of path
   */
  void endOfPass();

  /**
   * Get the original time delay. Can be used to calculate a corrected time delay offset.
   */
  float getTimeDelay();
}
