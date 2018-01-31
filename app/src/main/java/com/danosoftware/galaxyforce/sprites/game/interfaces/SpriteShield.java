package com.danosoftware.galaxyforce.sprites.game.interfaces;

import com.danosoftware.galaxyforce.view.Animation;

public abstract class SpriteShield extends MovingSprite
{
    // state time used to help select the current animation frame
    private float stateTime;

    // shield animation frames
    private Animation animation;

    public SpriteShield(int xStart, int yStart, Animation animation, float syncTime)
    {
        super(xStart, yStart, animation.getKeyFrame(0, Animation.ANIMATION_LOOPING), 0, 0, true);

        this.animation = animation;
        this.stateTime = syncTime;
    }

    @Override
    public void move(float deltaTime)
    {
        // increase state time by delta
        stateTime += deltaTime;

        // set base sprite using animation loop and time through animation
        setSpriteIdentifier(animation.getKeyFrame(stateTime, Animation.ANIMATION_LOOPING));
    }

    /**
     * Shields can be added at different times. A base with a shield may gain
     * some shielded helpers. In order to keep all shields animating in sync, it
     * should be possible to share the state time used for synchronisation.
     * 
     * Normally the helper bases will be synchronised to the primary base.
     * 
     * @return synchronisation timing
     */
    public float getSynchronisation()
    {
        return stateTime;
    }
}
