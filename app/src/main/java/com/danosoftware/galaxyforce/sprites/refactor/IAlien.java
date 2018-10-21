package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.sprites.game.missiles.bases.IBaseMissile;

public interface IAlien extends ICollidingSprite  {

    void onHitBy(IBaseMissile baseMissile);

    /**
     * Is alien activate?
     * That is: not exploding, destroyed or idle.
     */
    boolean isActive();

    /**
     * Is alien visible on the game screen?
     * That is: not destroyed or idle.
     */
    boolean isVisible();
}
