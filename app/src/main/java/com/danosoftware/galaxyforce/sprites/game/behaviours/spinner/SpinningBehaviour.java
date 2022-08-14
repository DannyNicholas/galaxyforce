package com.danosoftware.galaxyforce.sprites.game.behaviours.spinner;

import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;

public interface SpinningBehaviour {

  /**
   * Returns true if alien is spinning
   */
  boolean isSpinning();

  /**
   * Spin the alien according to supplied time since last update.
   */
  void spin(IAlien alien, float deltaTime);
}
