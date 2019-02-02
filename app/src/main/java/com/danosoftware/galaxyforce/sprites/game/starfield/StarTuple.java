package com.danosoftware.galaxyforce.sprites.game.starfield;

/**
 * Tuple of Star with it's initial Y position.
 * Helps computation and movement to a new Y position.
 */
class StarTuple {

    private final Star star;
    private final int initialY;

    StarTuple(
            Star star,
            int initialY) {
        this.star = star;
        this.initialY = initialY;
    }

    Star getStar() {
        return star;
    }

    int getInitialY() {
        return initialY;
    }
}
