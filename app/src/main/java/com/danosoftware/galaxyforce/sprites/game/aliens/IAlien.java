package com.danosoftware.galaxyforce.sprites.game.aliens;

import com.danosoftware.galaxyforce.sprites.common.ICollidingSprite;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.IBaseMissile;
import com.danosoftware.galaxyforce.waves.AlienCharacter;

public interface IAlien extends ICollidingSprite {

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

    /**
     * trigger alien explosion
     */
    void explode();

    /**
     * Return the alien's character.
     */
    AlienCharacter character();
}
