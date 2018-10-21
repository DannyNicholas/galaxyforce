package com.danosoftware.galaxyforce.sprites.game.interfaces;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.AbstractCollidingSprite;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package invaders;

/**
 * 
 * @author Danny
 */
public abstract class SpritePowerUp extends AbstractCollidingSprite
{

    public SpritePowerUp(int xStart, int yStart, ISpriteIdentifier spriteId)
    {
        super(spriteId, xStart, yStart);
    }

    /**
     * Return the power-up type for this power-up
     * 
     * @return power up type
     */
    public abstract PowerUpType getPowerUpType();
}
