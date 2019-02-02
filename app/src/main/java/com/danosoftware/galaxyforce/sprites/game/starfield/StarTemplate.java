package com.danosoftware.galaxyforce.sprites.game.starfield;

/**
 * Represents a template for a star, from which a new star can be created.
 * <p>
 * Holds each star's initial state and animation properties.
 * <p>
 * Allows duplicate stars to be created from the same template using different
 * sprite identifiers. To the player, the stars look identical and animation
 * seamless when screens are changed.
 */
public class StarTemplate {

    // star's initial x,y position
    private final int initialX;
    private final int initialY;

    // index of animation used by star
    private final int animationIndex;

    // inital animation elapsed time
    private final float animationStateTime;

    // speed of star
    private final float speed;

    public StarTemplate(
            int initialX,
            int initialY,
            int animationIndex,
            float animationStateTime,
            float speed) {
        this.initialX = initialX;
        this.initialY = initialY;
        this.animationIndex = animationIndex;
        this.animationStateTime = animationStateTime;
        this.speed = speed;
    }

    public int getInitialX() {
        return initialX;
    }

    public int getInitialY() {
        return initialY;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public float getAnimationStateTime() {
        return animationStateTime;
    }

    public float getSpeed() {
        return speed;
    }
}
