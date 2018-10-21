package com.danosoftware.galaxyforce.sprites.game.behaviours.powerup;

import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteAlien;

public interface PowerUpBehaviour
{
    /**
     * Checks to see if power-up should be released when alien is destroyed. If
     * so a new power-up is sent to model.
     */
    public void releasePowerUp(SpriteAlien alien);
}
