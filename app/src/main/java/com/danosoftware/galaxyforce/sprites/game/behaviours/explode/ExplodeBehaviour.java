package com.danosoftware.galaxyforce.sprites.game.behaviours.explode;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;

public interface ExplodeBehaviour {

    /**
     * Initialise start of explosion
     */
    void startExplosion();

    /**
     * Initialise start of explosion.
     * <p>
     * This implementation is silent and does not cause an explosion
     * sound or any vibration. Typically used for followers
     * when another sprite will handle sound/vibration effects.
     */
    void startExplosionSilently();

    /**
     * Get the current explosion sprite.
     */
    ISpriteIdentifier getExplosion(float deltaTime);

    /**
     * Has the explosion finished?
     */
    boolean finishedExploding();

}