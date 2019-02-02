package com.danosoftware.galaxyforce.sprites.game.starfield;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    // random generator
    private static final Random RANDOM = new Random();

    /* number of stars to show */
    private static final int MAX_STARS = 250;

    // list of templates that can be used to create identical stars
    // contains each star's initial state and properties.
    private final List<StarTemplate> starTemplates;

    // time elapsed since initial starfield was created.
    private float timeElapsed;

    // height of playing area
    private final int height;

    /**
     * Default constructor with random starfield properties
     */
    public StarFieldTemplate(int width, int height) {
        this.height = height;
        this.starTemplates = setupStars(width, height);
        this.timeElapsed = 0f;
    }

    /**
     * Constructor with predictable initial starfield properties.
     * Normally only used for testing.
     */
    public StarFieldTemplate(
            int height,
            int x,
            int y,
            int animationIndex,
            int animationStateTime,
            StarSpeed starSpeed) {
        this.height = height;
        this.timeElapsed = 0f;

        this.starTemplates = new ArrayList<>();
        for (int i = 0; i < MAX_STARS; i++) {
            starTemplates.add(new StarTemplate(
                    x,
                    y,
                    animationIndex,
                    animationStateTime,
                    starSpeed));
        }
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
            StarSpeed speed = getSpeedRandom();

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
     * return a random star speed
     */
    private StarSpeed getSpeedRandom() {
        StarSpeed[] possibleSpeeds = StarSpeed.values();
        int speedIndex = RANDOM.nextInt(possibleSpeeds.length);
        return possibleSpeeds[speedIndex];
    }

    /**
     * get random animation index.
     * when star is created, this will select the appropriate star animation
     */
    private int getRandomAnimationIndex() {
        return RANDOM.nextInt(StarAnimationType.ANIMATION_TYPES);
    }

    /**
     * get random animation start time so all stars' animations are not synchronised.
     * makes animation look more natural.
     */
    private float getRandomAnimationStartTime() {
        return (1.5f * RANDOM.nextFloat());
    }
}
