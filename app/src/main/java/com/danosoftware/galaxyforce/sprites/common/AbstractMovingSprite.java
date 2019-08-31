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

    @Override
    public void moveY(int y) {
        this.y = y;
    }

    @Override
    public void moveByDelta(int xDelta, int yDelta) {
        this.x += xDelta;
        this.y += yDelta;
    }

    @Override
    public void moveYByDelta(int yDelta) {
        this.y += yDelta;
    }

    @Override
    public void rotate(int rotation) {
        this.rotation = rotation;
    }
}
