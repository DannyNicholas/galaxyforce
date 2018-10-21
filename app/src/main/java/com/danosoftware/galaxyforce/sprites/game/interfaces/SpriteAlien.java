package com.danosoftware.galaxyforce.sprites.game.interfaces;

import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviour;
import com.danosoftware.galaxyforce.view.Animation;

/**
 * 
 * @author Danny
 */
public abstract class SpriteAlien extends ExplodingSprite
{

    /*
     * ******************************************************
     * PRIVATE INSTANCE VARIABLES
     * ******************************************************
     */

    /* reference to how and when alien fires missiles */
    private final FireBehaviour fireBehaviour;

    /* reference to how alien creates power-ups */
    private final PowerUpBehaviour powerUpBehaviour;

    /* reference to how alien spawns other aliens */
    private final SpawnBehaviour spawnBehaviour;

    /* state time used to help select the current animation frame */
    private float stateTime;

    /* animation sprites */
    private final Animation animation;

    /*
     * ******************************************************
     * CONSTRUCTOR
     * 
     * ******************************************************
     */

    public SpriteAlien(FireBehaviour fireBehaviour, PowerUpBehaviour powerUpBehaviour, SpawnBehaviour spawnBehaviour,
            ExplodeBehaviour explodeBehaviour, Animation animation, int xStart, int yStart, int energy, int hitEnergy, boolean visible)
    {
        super(xStart, yStart, animation.getKeyFrame(0, Animation.ANIMATION_LOOPING), explodeBehaviour, energy, hitEnergy, visible);

        super.updateBounds();

        this.fireBehaviour = fireBehaviour;
        this.powerUpBehaviour = powerUpBehaviour;
        this.spawnBehaviour = spawnBehaviour;
        this.animation = animation;
        this.stateTime = 0f;
    }

    /*
     * ******************************************************
     * PUBLIC METHODS
     * 
     * ******************************************************
     */

    @Override
    public void move(float deltaTime)
    {
        // call explosion super-class
        super.move(deltaTime);

        if (isActive())
        {
            // if alien is ready to fire - then fire!!
            if (fireBehaviour.readyToFire(deltaTime))
            {
                fireBehaviour.fire(this);
            }

            // if alien is ready to spawn - spawn!!
            if (spawnBehaviour.readyToSpawn(deltaTime))
            {
                spawnBehaviour.spawn(this);
            }

            // increase state time by delta
            stateTime += deltaTime;

            // set base sprite using animation loop and time through animation
            setSpriteIdentifier(animation.getKeyFrame(stateTime, Animation.ANIMATION_LOOPING));
        }
    }

    @Override
    public void setExploding()
    {
        // call superclass to handle alien exploding
        super.setExploding();

        // Checks to see if power-up should be released when alien is destroyed.
        powerUpBehaviour.releasePowerUp(this);
    }
}
