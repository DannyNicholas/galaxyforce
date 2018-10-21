package com.danosoftware.galaxyforce.sprites.game.behaviours.powerup;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.game.handlers.GameHandler;
import com.danosoftware.galaxyforce.sprites.game.factories.PowerUpFactory;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteAlien;

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
     * @param model
     * @param powerUpChance - chance that this alien will generate a power-up when
     *                      destroyed
     */
    public PowerUpRandom(GameHandler model, double powerUpChance) {
        this.model = model;

        // decide whether this alien should generate a power-up when destroyed
        if (Math.random() < powerUpChance) {
            this.hasPowerUp = true;
        } else {
            this.hasPowerUp = false;
        }
    }

    /**
     * Behaviour that will create a random power-up from a supplied list when
     * alien is destroyed.
     *
     * @param model
     * @param powerUpChance - chance that this alien will generate a power-up when
     *                      destroyed
     * @param powerUps      - list of power-ups to choose from
     */
    public PowerUpRandom(GameHandler model, double powerUpChance, PowerUpType... powerUps) {
        this(model, powerUpChance);
        this.powerUps = powerUps;
    }

    @Override
    public void releasePowerUp(SpriteAlien alien) {
        if (hasPowerUp) {
            /*
             * Choose from supplied list of power-ups if one exists. Otherwise
             * choose a completely random power-up.
             */
            if (powerUps != null) {
                model.addPowerUp(PowerUpFactory.generateRandomPowerUp(alien.getX(), alien.getY(), powerUps));
            } else {
                model.addPowerUp(PowerUpFactory.generateRandomPowerUp(alien.getX(), alien.getY()));
            }
        }
    }
}
