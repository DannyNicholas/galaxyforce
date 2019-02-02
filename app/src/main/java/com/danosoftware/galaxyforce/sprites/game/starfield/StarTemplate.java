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
class StarTemplate {

    // star's initial x,y position
    private final int initialX;
    private final int initialY;

    // index of animation used by star
    private final int animationIndex;

    // inital animation elapsed time
    private final float animationStateTime;

    // speed of star
    private final StarSpeed speed;

    StarTemplate(
            int initialX,
            int initialY,
            int animationIndex,
            float animationStateTime,
            StarSpeed speed) {
        this.initialX = initialX;
        this.initialY = initialY;
        this.animationIndex = animationIndex;
        this.animationStateTime = animationStateTime;
        this.speed = speed;
    }

    int getInitialX() {
        return initialX;
    }

    int getInitialY() {
        return initialY;
    }

    int getAnimationIndex() {
        return animationIndex;
    }

    float getAnimationStateTime() {
        return animationStateTime;
    }

    StarSpeed getSpeed() {
        return speed;
    }
}
