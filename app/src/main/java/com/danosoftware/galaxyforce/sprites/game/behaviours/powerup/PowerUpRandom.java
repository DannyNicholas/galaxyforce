package com.danosoftware.galaxyforce.sprites.game.behaviours.powerup;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.play.game_handler.GameHandler;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.factories.PowerUpFactory;

public class PowerUpRandom implements PowerUpBehaviour {
    /*
     * ******************************************************
     * PRIVATE INSTANCE VARIABLES
     * ******************************************************
     */

    /* reference to game model */
    private final GameHandler model;

    /* will this alien generate a power-up when destroyed */
    private final boolean hasPowerUp;

    /* optional list of power-ups to randomly choose from */
    private PowerUpType[] powerUps;

    /**
     * Behaviour that will create a random power-up when alien is destroyed.
     *
     * @param model - game handler to receive power-up
     * @param powerUpChance - chance that this alien will generate a power-up when
     *                      destroyed
     */
    public PowerUpRandom(GameHandler model, double powerUpChance) {
        this.model = model;

        // decide whether this alien should generate a power-up when destroyed
        this.hasPowerUp = Math.random() < powerUpChance;
    }

    /**
     * Behaviour that will create a random power-up from a supplied list when
     * alien is destroyed.
     *
     * @param model - game handler to receive power-up
     * @param powerUpChance - chance that this alien will generate a power-up when
     *                      destroyed
     * @param powerUps      - list of power-ups to choose from
     */
    public PowerUpRandom(GameHandler model, double powerUpChance, PowerUpType... powerUps) {
        this(model, powerUpChance);
        this.powerUps = powerUps;
    }

    @Override
    public void releasePowerUp(IAlien alien) {
        if (hasPowerUp) {
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
}
