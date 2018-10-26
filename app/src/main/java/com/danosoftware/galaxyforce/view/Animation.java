package com.danosoftware.galaxyforce.view;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;

public class Animation
{
    public static final int ANIMATION_LOOPING = 0;
    public static final int ANIMATION_NONLOOPING = 1;

    final ISpriteIdentifier[] keyFrames;
    final float frameDuration;
    boolean animationComplete;

    public Animation(float frameDuration, ISpriteIdentifier... keyFrames)
    {
        this.frameDuration = frameDuration;
        this.keyFrames = keyFrames;
        this.animationComplete = false;
    }

    public ISpriteIdentifier getKeyFrame(float stateTime, int mode)
    {
        int frameNumber;
        if (frameDuration == 0)
        {
            /*
             * if zero frame duration then always show first frame only. may be
             * zero for single sprite animations
             */
            frameNumber = 0;
        }
        else
        {
            frameNumber = (int) (stateTime / frameDuration);
        }

        if (mode == ANIMATION_NONLOOPING)
        {
            /*
             * if beyond last keyframe then set animation completed (only for
             * non-looping animations)
             */
            animationComplete = frameNumber >= keyFrames.length;

            /*
             * ensure we don't exceed the maximum array element. if we have gone
             * beyond last array element, just return the last element
             */
            frameNumber = Math.min(keyFrames.length - 1, frameNumber);
        }
        else
        {
            frameNumber = frameNumber % keyFrames.length;
        }

        return keyFrames[frameNumber];
    }

    public boolean isAnimationComplete()
    {
        return animationComplete;
    }
}
