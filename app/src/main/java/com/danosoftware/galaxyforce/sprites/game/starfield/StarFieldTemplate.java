package com.danosoftware.galaxyforce.sprites.game.starfield;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a template for a starfield, from which a new starfield can be created.
 * <p>
 * Holds each star's initial state and animation properties.
 * Holds the time-elapsed since the initial starfield animation started.
 * <p>
 * Both allow a new starfield to be fast-forwarded to the current state
 * from the template's initial state.
 */
public class StarFieldTemplate {

    /* max and min speed of stars - how many pixels star moves in 1 second */
    private static final int STAR_MOVE_PIXELS_MIN = 60;
    private static final int STAR_MOVE_PIXELS_MAX = 60 * 3;

    /* number of stars to show */
    private static final int MAX_STARS = 250;

    // list of templates that can be used to create identical stars
    // contains each star's initial state and properties.
    private final List<StarTemplate> starTemplates;

    // time elapsed since initial starfield was created.
    private float timeElapsed;

    // height of playing area
    private final int height;

    public StarFieldTemplate(int width, int height) {
        this.height = height;
        this.starTemplates = setupStars(width, height);
        this.timeElapsed = 0f;
    }

    List<StarTemplate> getStarTemplates() {
        return starTemplates;
    }

    float getTimeElapsed() {
        return timeElapsed;
    }

    public int getHeight() {
        return height;
    }

    void increaseTimeElapsed(float deltaTime) {
        timeElapsed += deltaTime;
    }

    /**
     * initialise background stars. returns a list of stars in random positions.
     */
    private List<StarTemplate> setupStars(int width, int height) {
        List<StarTemplate> stars = new ArrayList<>();

        for (int i = 0; i < MAX_STARS; i++) {
            int x = (int) (width * Math.random());
            int y = (int) (height * Math.random());

            int animationIndex = getRandomAnimationIndex();
            float animationStateTime = getRandomAnimationStartTime();
            float speed = getSpeedRandom();

            stars.add(new StarTemplate(
                    x,
                    y,
                    animationIndex,
                    animationStateTime,
                    speed));
        }

        return stars;
    }

    /**
     * set random speed between STAR_MOVE_PIXELS_MIN and STAR_MOVE_PIXELS_MAX
     */
    private float getSpeedRandom() {
        return ((int) (((STAR_MOVE_PIXELS_MAX + 1 - STAR_MOVE_PIXELS_MIN) * Math.random()) + STAR_MOVE_PIXELS_MIN));
    }

    /**
     * get random animation index.
     * when star is created, this will select the appropriate star animation
     */
    private int getRandomAnimationIndex() {
        return (int) (StarAnimationType.ANIMATION_TYPES * Math.random());
    }

    /**
     * get random animation start time so all stars' animations are not synchronised.
     * makes animation look more natural.
     */
    private float getRandomAnimationStartTime() {
        return (float) (1f * Math.random());
    }
}
