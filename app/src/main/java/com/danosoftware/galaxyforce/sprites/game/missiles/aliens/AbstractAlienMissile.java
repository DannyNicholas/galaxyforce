package com.danosoftware.galaxyforce.sprites.game.missiles.aliens;

import com.danosoftware.galaxyforce.sprites.common.AbstractCollidingSprite;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;

public abstract class AbstractAlienMissile extends AbstractCollidingSprite implements IAlienMissile {

    private boolean isDestroyed;

    AbstractAlienMissile(
            ISpriteIdentifier spriteId,
            int x,
            int y) {

        super(spriteId, x, y);
        this.isDestroyed = false;
    }

    @Override
    public void destroy() {
        this.isDestroyed = true;
    }

    @Override
    public boolean isDestroyed() {
        return isDestroyed;
    }
}
