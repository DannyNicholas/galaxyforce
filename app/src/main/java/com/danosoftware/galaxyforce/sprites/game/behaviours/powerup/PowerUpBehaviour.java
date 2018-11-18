package com.danosoftware.galaxyforce.sprites.game.behaviours.powerup;

import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;

public interface PowerUpBehaviour {
    /**
     * Checks to see if power-up should be released when alien is destroyed. If
     * so a new power-up is sent to model.
     */
    void releasePowerUp(IAlien alien);
}
