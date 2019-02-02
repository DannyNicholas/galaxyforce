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

    // star's animation loop
    private final Animation animation;

    // state time used for how far we are through animation cycles
    private float animationStateTime;

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
        this.animationStateTime = animationStateTime;
        this.speed = speed;
    }

    @Override
    public void animate(float deltaTime) {

        // calculate new star position based
        int starY = y() - (int) (speed * deltaTime);

        // if star has reached the bottom of screen then re-position on the other side.
        if (starY < 0) {
            starY = height + (starY % height);
        }
        this.move(x(), starY);

        // update animation frame
        animationStateTime += deltaTime;
        this.changeType(animation.getKeyFrame(animationStateTime, Animation.ANIMATION_LOOPING));
    }
}
