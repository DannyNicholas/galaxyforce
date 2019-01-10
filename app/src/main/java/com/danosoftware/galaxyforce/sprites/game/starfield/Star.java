package com.danosoftware.galaxyforce.sprites.game.starfield;

import com.danosoftware.galaxyforce.sprites.common.AbstractMovingSprite;
import com.danosoftware.galaxyforce.view.Animation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Danny
 */
public class Star extends AbstractMovingSprite {
    /* number of stars to show */
    private static final int MAX_STARS = 250;

    /* max and min speed of stars - how many pixels star moves in 1 second */
    private static final int STAR_MOVE_PIXELS_MIN = 60;
    private static final int STAR_MOVE_PIXELS_MAX = 60 * 3;

    // stores the height of the visible screen area
    private final int height;

    // speed of star
    private final float speed;

    // star's animation loop
    private final Animation animation;

    // state time used for how far we are through animation cycles
    private float animationStateTime;

    // private constructor
    private Star(
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
            starY = height + starY;
        }

        this.move(x(), starY);

        // set sprite using animation loop
        animationStateTime += deltaTime;
        this.changeType(animation.getKeyFrame(animationStateTime, Animation.ANIMATION_LOOPING));
    }

    /**
     * initialise background stars. returns a list of stars in random positions.
     */
    public static List<Star> setupStars(int width, int height, Animation[] starAnimations) {
        List<Star> stars = new ArrayList<>();

        for (int i = 0; i < Star.MAX_STARS; i++) {
            int x = (int) (width * Math.random());
            int y = (int) (height * Math.random());

            Animation animation = getRandomAnimation(starAnimations);
            float animationStateTime = getRandomAnimationStartTime();
            float speed = getSpeedRandom();

            stars.add(new Star(x, y, height, animation, animationStateTime, speed));
        }

        return stars;
    }

    /**
     * set random speed between STAR_MOVE_PIXELS_MIN and STAR_MOVE_PIXELS_MAX
     */
    private static float getSpeedRandom() {
        return ((int) (((STAR_MOVE_PIXELS_MAX + 1 - STAR_MOVE_PIXELS_MIN) * Math.random()) + STAR_MOVE_PIXELS_MIN));
    }

    /**
     * get random animation
     */
    private static Animation getRandomAnimation(Animation[] starAnimations) {
        int index = (int) (starAnimations.length * Math.random());
        return (starAnimations[index]);
    }

    /**
     * get random animation start time so all stars are not synchronised
     */
    private static float getRandomAnimationStartTime() {
        return (float) (1f * Math.random());
    }
}
