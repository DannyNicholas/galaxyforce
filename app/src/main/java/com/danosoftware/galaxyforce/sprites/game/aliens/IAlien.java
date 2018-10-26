package com.danosoftware.galaxyforce.sprites.game.aliens;

import com.danosoftware.galaxyforce.sprites.game.missiles.bases.IBaseMissile;
import com.danosoftware.galaxyforce.sprites.refactor.ICollidingSprite;

public interface IAlien extends ICollidingSprite {

    void onHitBy(IBaseMissile baseMissile);

    /**
     * Is alien activate?
     * That is: not exploding, destroyed or idle.
     */
    boolean isActive();

    /**
     * activate alien
     */
    void activate();

    /**
     * Is alien visible on the game screen?
     * That is: not destroyed or idle.
     */
    boolean isVisible();

    /**
     * is alien waiting to start?
     */
    boolean isWaiting();

    /**
     * alien is waiting to start
     */
    void waiting();

    /**
     * is alien exploding?
     */
    boolean isExploding();

    /**
     * trigger alien explosion
     */
    void explode();
}
