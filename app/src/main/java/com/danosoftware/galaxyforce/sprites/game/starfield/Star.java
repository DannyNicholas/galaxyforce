package com.danosoftware.galaxyforce.sprites.game.starfield;

import com.danosoftware.galaxyforce.sprites.common.AbstractMovingSprite;
import com.danosoftware.galaxyforce.view.Animation;

/**
 * Star sprite that drifts down the screen over time and animates.
 * When a star reaches the bottom of the screen, it returns to the top.
 * Many stars will be contained within an animated star-field.
 */
public class Star extends AbstractMovingSprite {

    // stores the height of the visible screen area
    private final int height;

    // speed of star
    private final float speed;

    // star's initial Y position
    private final int initialY;

    // star's initial animation cycle timing offset
    private final float initialAnimationStateTime;

    // star's animation loop
    private final Animation animation;

    Star(
            int x,
            int y,
            int height,
            Animation animation,
            float animationStateTime,
            float speed) {

        // set star's start position and initial sprite from animation
        super(
                animation.getKeyFrame(animationStateTime, Animation.ANIMATION_LOOPING),
                x,
                y);

        this.height = height;
        this.animation = animation;
        this.initialAnimationStateTime = animationStateTime;
        this.speed = speed;
        this.initialY = y;
    }

    /**
     * Update the position and animation for star.
     *
     * @param deltaTime - total time since initial star-field was first created
     */
    @Override
    public void animate(float deltaTime) {

        // normally deltaTime holds the time since the last update.
        // for Stars, deltaTime represents total time since the initial star-field
        // was first created.
        // calculate new star position based on initial position and deltaTime.
        int starY = initialY - (int) ((speed * deltaTime) % height);

        // if star has reached the bottom of screen then re-position on the other side.
        if (starY < 0) {
            starY = height + starY;
        }
        this.move(x(), starY);

        // update animation frame
        this.changeType(
                animation.getKeyFrame(
                        initialAnimationStateTime + deltaTime,
                        Animation.ANIMATION_LOOPING));
    }
}
