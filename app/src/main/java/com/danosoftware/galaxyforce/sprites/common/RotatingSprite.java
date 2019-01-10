package com.danosoftware.galaxyforce.sprites.common;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;

public class RotatingSprite extends AbstractMovingSprite {

    /* angle of sprite rotation */
    private float angle = 0f;


    public RotatingSprite(int xStart, int yStart, ISpriteIdentifier spriteId) {
        super(spriteId, xStart, yStart);
    }

    @Override
    public void animate(float deltaTime) {
        // increase angle rotation
        angle = (angle + (deltaTime * 75)) % 360;
        rotate((int) angle);
    }
}
