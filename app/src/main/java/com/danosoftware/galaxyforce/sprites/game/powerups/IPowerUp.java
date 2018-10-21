package com.danosoftware.galaxyforce.sprites.game.powerups;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.sprites.refactor.ICollidingSprite;

public interface IPowerUp extends ICollidingSprite {

    /**
     * Return the power-up type for this power-up
     */
    PowerUpType getPowerUpType();
}
