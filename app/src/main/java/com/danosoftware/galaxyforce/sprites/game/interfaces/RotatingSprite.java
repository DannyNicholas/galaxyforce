package com.danosoftware.galaxyforce.sprites.game.interfaces;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.AbstractSprite;

public class RotatingSprite extends AbstractSprite
{
    public RotatingSprite(int xStart, int yStart, ISpriteIdentifier spriteId)
    {
        super(spriteId, xStart, yStart);
    }
}
