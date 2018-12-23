package com.danosoftware.galaxyforce.sprites.game.behaviours.powerup;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.screens.game.handlers.IGameHandler;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.factories.PowerUpFactory;

public class PowerUpSimple implements PowerUpBehaviour {
    /*
     * ******************************************************
     * PRIVATE INSTANCE VARIABLES
     * ******************************************************
     */

    /* reference to game model */
    private final IGameHandler model;

    /* optional list of power-ups to randomly choose from */
    private PowerUpType[] powerUps;

    /**
     * Behaviour that will create a random power-up when alien is destroyed.
     */
    public PowerUpSimple(IGameHandler model) {
        this.model = model;
    }

    /**
     * Behaviour that will create a random power-up from a supplied list when
     * alien is destroyed.
     *
     * @param model - game handler to receive power-up
     * @param powerUps - list of power-ups to choose from
     */
    public PowerUpSimple(IGameHandler model, PowerUpType... powerUps) {
        this(model);
        this.powerUps = powerUps;
    }

    @Override
    public void releasePowerUp(IAlien alien) {
        /*
         * Choose from supplied list of power-ups if one exists. Otherwise
         * choose a completely random power-up.
         */
        if (powerUps != null) {
            model.addPowerUp(PowerUpFactory.generateRandomPowerUp(alien.x(), alien.y(), powerUps));
        } else {
            model.addPowerUp(PowerUpFactory.generateRandomPowerUp(alien.x(), alien.y()));
        }
    }
}
