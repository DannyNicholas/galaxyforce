package com.danosoftware.galaxyforce.sprites.common;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;

public abstract class AbstractMovingSprite extends AbstractSprite implements IMovingSprite {

    AbstractMovingSprite(
            ISpriteIdentifier spriteId,
            int x,
            int y,
            int rotation) {

        super(spriteId, x, y, rotation);
    }

    protected AbstractMovingSprite(
            ISpriteIdentifier spriteId,
            int x,
            int y) {

        this(spriteId, x, y, 0);
    }

    @Override
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected void rotate(int rotation) {
        this.rotation = rotation;
    }
}
