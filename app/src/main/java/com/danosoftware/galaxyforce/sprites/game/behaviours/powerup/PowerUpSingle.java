package com.danosoftware.galaxyforce.sprites.game.behaviours.powerup;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.game.handlers.GameHandler;
import com.danosoftware.galaxyforce.sprites.game.factories.PowerUpFactory;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteAlien;

public class PowerUpSingle implements PowerUpBehaviour {
    /*
     * ******************************************************
     * PRIVATE INSTANCE VARIABLES
     * ******************************************************
     */

    /* reference to game model */
    private final GameHandler model;

    /* power-up type */
    private final PowerUpType powerUp;

    /**
     * Behaviour that will create the supplied power-up when
     * alien is destroyed.
     *
     * @param model
     * @param powerUp
     */
    public PowerUpSingle(GameHandler model, PowerUpType powerUp) {
        this.model = model;
        this.powerUp = powerUp;
    }

    @Override
    public void releasePowerUp(SpriteAlien alien) {
        // Add power-up (if one exists).
        if (powerUp != null) {
            model.addPowerUp(PowerUpFactory.newPowerUp(powerUp, alien.getX(), alien.getY()));
        }
    }
}
