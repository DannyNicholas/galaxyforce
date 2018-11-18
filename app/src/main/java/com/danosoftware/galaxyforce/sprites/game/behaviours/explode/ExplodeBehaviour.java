package com.danosoftware.galaxyforce.sprites.game.behaviours.explode;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;

public interface ExplodeBehaviour {

    /**
     * Initialise start of explosion
     */
    void startExplosion();

    /**
     * Get the current explosion sprite.
     *
     * @param deltaTime
     * @return
     */
    ISpriteIdentifier getExplosion(float deltaTime);

    /**
     * Has the explosion finished?
     *
     * @return
     */
    boolean finishedExploding();

}