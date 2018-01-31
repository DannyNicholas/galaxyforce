package com.danosoftware.galaxyforce.sprites.game.behaviours;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;

public interface ExplodeBehaviour
{

    /**
     * Initialise start of explosion
     */
    public void startExplosion();

    /**
     * Get the current explosion sprite.
     * 
     * @param deltaTime
     * @return
     */
    public ISpriteIdentifier getExplosion(float deltaTime);

    /**
     * Has the explosion finished?
     * 
     * @return
     */
    public boolean finishedExploding();

}