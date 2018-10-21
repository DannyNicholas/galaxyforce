package com.danosoftware.galaxyforce.sprites.game.missiles.aliens;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.AbstractCollidingSprite;

public abstract class AbstractAlienMissile extends AbstractCollidingSprite implements IAlienMissile {

    // how much energy will be lost by base when this missile hits it
    private final int hitEnergy;

    private boolean isDestroyed;

    public AbstractAlienMissile(
            ISpriteIdentifier spriteId,
            int x,
            int y,
            int hitEnergy) {

        super(spriteId, x, y);
        this.hitEnergy = hitEnergy;
        this.isDestroyed = isDestroyed;
    }

    @Override
    public int energyDamage() {
        return hitEnergy;
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
