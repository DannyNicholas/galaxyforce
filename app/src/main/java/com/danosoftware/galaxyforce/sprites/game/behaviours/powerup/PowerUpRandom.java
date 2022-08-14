package com.danosoftware.galaxyforce.sprites.game.behaviours.powerup;

import static com.danosoftware.galaxyforce.waves.utilities.Randomiser.random;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.factories.PowerUpFactory;

public class PowerUpRandom implements PowerUpBehaviour {
  /*
   * ******************************************************
   * PRIVATE INSTANCE VARIABLES
   * ******************************************************
   */

  /* reference to game model */
  private final GameModel model;

  /* will this alien generate a power-up when destroyed */
  private final boolean hasPowerUp;

  /* optional list of power-ups to randomly choose from */
  private PowerUpType[] powerUps;

  /**
   * Behaviour that will create a random power-up when alien is destroyed.
   *
   * @param model         - game handler to receive power-up
   * @param powerUpChance - chance that this alien will generate a power-up when destroyed
   */
  private PowerUpRandom(GameModel model, double powerUpChance) {
    this.model = model;

    // decide whether this alien should generate a power-up when destroyed
    this.hasPowerUp = random() < powerUpChance;
  }

  /**
   * Behaviour that will create a random power-up from a supplied list when alien is destroyed.
   *
   * @param model         - game handler to receive power-up
   * @param powerUpChance - chance that this alien will generate a power-up when destroyed
   * @param powerUps      - list of power-ups to choose from
   */
  public PowerUpRandom(GameModel model, double powerUpChance, PowerUpType... powerUps) {
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
