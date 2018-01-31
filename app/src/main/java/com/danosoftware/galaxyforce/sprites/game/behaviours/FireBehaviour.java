package com.danosoftware.galaxyforce.sprites.game.behaviours;

import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteAlien;

public interface FireBehaviour
{
    /**
     * Returns true if alien is ready to fire.
     * 
     * @param deltaTime
     */
    public boolean readyToFire(float deltaTime);

    /**
     * Fires the alien's current missile type.
     */
    public void fire(SpriteAlien alien);
}
