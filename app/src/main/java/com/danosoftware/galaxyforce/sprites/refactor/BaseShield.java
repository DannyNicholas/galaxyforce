package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.view.Animation;

public class BaseShield extends AbstractMovingSprite implements IBaseShield
{
    // state time used to help select the current animation frame
    private float stateTime;

    // shield animation frames
    private Animation animation;

    public BaseShield(int xStart, int yStart, Animation animation, float syncTime)
    {
        super(animation.getKeyFrame(syncTime, Animation.ANIMATION_LOOPING), xStart, yStart);
        this.animation = animation;
        this.stateTime = syncTime;
    }

    @Override
    public void animate(float deltaTime)
    {
        // increase state time by delta
        stateTime += deltaTime;

        // set base sprite using animation loop and time through animation
        changeType(animation.getKeyFrame(stateTime, Animation.ANIMATION_LOOPING));
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
    @Override
    public float getSynchronisation()
    {
        return stateTime;
    }
}
