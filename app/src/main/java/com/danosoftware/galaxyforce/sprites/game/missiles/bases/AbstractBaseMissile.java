package com.danosoftware.galaxyforce.sprites.game.missiles.bases;

import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.AbstractCollidingSprite;


public abstract class AbstractBaseMissile extends AbstractCollidingSprite implements IBaseMissile
{

    // how much energy will be lost by alien when this missile hits it
    private final int hitEnergy;

    private boolean isDestroyed;

    public AbstractBaseMissile(
            ISpriteIdentifier spriteId,
            int x,
            int y,
            int hitEnergy) {

        // adjust missile starting position by half the missile's height
        super(spriteId, x, y + (spriteId.getProperties().getHeight()/2));
        this.hitEnergy = hitEnergy;
        this.isDestroyed = false;
    }

    // by default, most base missiles will only hit an alien once and destroy themselves
    @Override
    public boolean hitBefore(IAlien alien) {
        return false;
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
