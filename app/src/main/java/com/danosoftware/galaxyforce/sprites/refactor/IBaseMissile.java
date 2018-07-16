package com.danosoftware.galaxyforce.sprites.refactor;

public interface IBaseMissile extends ICollidingSprite {

    /**
     * check if base missile and alien have hit before.
     * Used for missile implementations that do not destroy
     * themselves on initial impact with an alien.
     *
     * Avoid registering the hit multiple times for the same alien
     *
     * @param alien missile has collided with
     * @return has alien been hit before
     */
    boolean hitBefore(IAlien alien);
}
