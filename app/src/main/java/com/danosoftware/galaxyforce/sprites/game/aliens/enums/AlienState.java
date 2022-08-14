package com.danosoftware.galaxyforce.sprites.game.aliens.enums;

public enum AlienState {

  /**
   * Sprite is not yet active. This could be an alien that has been set-up but not yet started it's
   * move.
   */
  WAITING,

  /**
   * Sprite is active. This could be an alien that has now started it's move.
   */
  ACTIVE,

  /**
   * Sprite has finished it's current pass (e.g. alien reached end of path) but has not been
   * destroyed. An alien that has finished it's pass may be re-set and start again in cases where a
   * wave is repeated until all aliens are destroyed.
   */
  FINISHED_PASS,

  /**
   * Sprite has been hit and is now in the process of exploding. During this state the sprite is
   * still on screen but is unlikely to interact with other sprites.
   */
  EXPLODING,

  /**
   * Sprite is now completely destroyed and can be removed. Sprite should not be displayed on screen
   * and must not interact with other sprites.
   */
  DESTROYED
}
