package com.danosoftware.galaxyforce.sprites.refactor;

public interface IAlien extends ICollidingSprite  {

    void onHitBy(IBaseMissile baseMissile);

    void destroy();

    /**
     * Is alien activate?
     * That is: not exploding, destroyed or idle.
     */
    boolean isActive();

    /**
     * Is alien destroyed and can be removed?
     */
    boolean isDestroyed();

    /**
     * Is alien visible on the game screen?
     * That is: not destroyed or idle.
     */
    boolean isVisible();
}
