package com.danosoftware.galaxyforce.sprites.game.starfield;

import com.danosoftware.galaxyforce.sprites.common.AbstractMovingSprite;
import com.danosoftware.galaxyforce.view.Animation;

/**
 * Star sprite that drifts down the screen over time and animates.
 * When a star reaches the bottom of the screen, it returns to the top.
 * Many stars will be contained within an animated star-field.
 */
public class Star extends AbstractMovingSprite {

    // star's initial animation cycle timing offset
    private final float initialAnimationStateTime;

    // star's animation loop
    private final Animation animation;

    Star(
            int x,
            int y,
            Animation animation,
            float animationStateTime) {

        // set star's start position and initial sprite from animation
        super(
                animation.getKeyFrame(animationStateTime, Animation.ANIMATION_LOOPING),
                x,
                y);

        this.animation = animation;
        this.initialAnimationStateTime = animationStateTime;
    }

    /**
     * Update animation for star.
     * Position changes are more efficiently handled by star-field.
     *
     * NOTE: deltaTime represents total time since the initial star-field
     * was created (not time since the last update). Helps seamless star-field
     * animation when switching screens.
     *
     * @param deltaTime - total time since initial star-field was first created
     */
    @Override
    public void animate(float deltaTime) {

        // update animation frame
        this.changeType(
                animation.getKeyFrame(
                        initialAnimationStateTime + deltaTime,
                        Animation.ANIMATION_LOOPING));
    }
}
