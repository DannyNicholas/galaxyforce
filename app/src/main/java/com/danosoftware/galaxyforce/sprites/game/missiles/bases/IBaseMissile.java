package com.danosoftware.galaxyforce.sprites.game.missiles.bases;

import com.danosoftware.galaxyforce.sprites.common.ICollidingSprite;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;

public interface IBaseMissile extends ICollidingSprite {

  /**
   * check if base missile and alien have hit before. Used for missile implementations that do not
   * destroy themselves on initial impact with an alien.
   * <p>
   * Avoid registering the hit multiple times for the same alien
   *
   * @param alien missile has collided with
   * @return has alien been hit before
   */
  boolean hitBefore(IAlien alien);
}
