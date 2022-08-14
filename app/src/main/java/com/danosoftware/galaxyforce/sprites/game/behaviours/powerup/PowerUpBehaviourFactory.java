package com.danosoftware.galaxyforce.sprites.game.behaviours.powerup;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;

public class PowerUpBehaviourFactory {

  private final GameModel model;

  public PowerUpBehaviourFactory(final GameModel model) {
    this.model = model;
  }

  public PowerUpBehaviour createPowerUpBehaviour(final PowerUpType powerUpType) {
    return new PowerUpSingle(
        model,
        powerUpType);
  }
}
