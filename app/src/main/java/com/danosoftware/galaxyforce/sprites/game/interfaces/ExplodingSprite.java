package com.danosoftware.galaxyforce.sprites.game.interfaces;

import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitFlash;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;

public abstract class ExplodingSprite extends MovingSprite
{
    // energy of this sprite
    private int energy;

    // explosion behaviour
    private final ExplodeBehaviour explodeBehaviour;

    // hit behaviour
    private final HitBehaviour hitBehaviour;

    public ExplodingSprite(int xStart, int yStart, ISpriteIdentifier spriteId, ExplodeBehaviour explodeBehaviour, int energy, int hitEnergy,
            boolean visible)
    {
        super(
                xStart,
                yStart,
                spriteId,
                energy,
                hitEnergy,
                visible);

        this.explodeBehaviour = explodeBehaviour;
        this.hitBehaviour = new HitFlash(this);
        this.energy = energy;
    }

    /* handles explosions if sprite is hit */
    public void move(float deltaTime)
    {
        /* if exploding - choose the current explosion sprite */
        if (isExploding())
        {
            setSpriteIdentifier(explodeBehaviour.getExplosion(deltaTime));

            if (explodeBehaviour.finishedExploding())
            {
                setState(SpriteState.DESTROYED);
            }
        }

        // if hit then continue sprite flash
        if (hitBehaviour.isHit()) {
            hitBehaviour.updateHit(deltaTime);
        }
    }

    /**
     * If this sprite is hit by another object. The amount of energy this sprite
     * should lose is supplied as a parameter.
     * 
     * If this sprite's energy drops to 0 or below, it explodes.
     * 
     * @param loseEnergy
     *            - the amount of energy to lose
     */
    @Override
    public void loseEnergy(int loseEnergy)
    {
        energy = energy - loseEnergy;

        if (energy <= 0)
        {
            setExploding();
        } else {
            startHit();
        }
    }

    /**
     * sets hit is true which will trigger explosions and destroy object alien
     * can only be hit once delayStart <0 (once 'in-play') this stops waiting
     * aliens being destroyed
     */
    public void setExploding()
    {
        explodeBehaviour.startExplosion();
        setState(SpriteState.EXPLODING);
    }

    public boolean isExploding()
    {
        return (state == SpriteState.EXPLODING);
    }

    public void startHit()
    {
        hitBehaviour.startHit();
    }
}
