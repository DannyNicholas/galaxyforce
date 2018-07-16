package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.utilities.Rectangle;

public abstract class AbstractMovingSprite extends AbstractSprite implements IMovingSprite {

    // sprite's angle rotation
    private int rotation;

    public AbstractMovingSprite(
            ISpriteIdentifier spriteId,
            int x,
            int y,
            int rotation) {

        super(spriteId, x, y);
        this.rotation = rotation;
    }

    public AbstractMovingSprite(
            ISpriteIdentifier spriteId,
            int x,
            int y) {

        this(spriteId, x, y,0);
    }

    @Override
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void rotate(int rotation) {
        this.rotation = rotation;
    }
}
