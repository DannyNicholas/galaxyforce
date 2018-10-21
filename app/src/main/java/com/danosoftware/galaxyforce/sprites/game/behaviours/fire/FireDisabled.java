package com.danosoftware.galaxyforce.sprites.game.behaviours.fire;

import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteAlien;

/**
 * Fire behaviour implementation for aliens that do NOT fire missiles.
 * 
 * Used to switch off missile firing.
 */
public class FireDisabled implements FireBehaviour
{
    @Override
    public boolean readyToFire(float deltaTime)
    {
        // alien is never ready to fire
        return false;
    }

    @Override
    public void fire(SpriteAlien alien)
    {
        // no implementation
    }
}
