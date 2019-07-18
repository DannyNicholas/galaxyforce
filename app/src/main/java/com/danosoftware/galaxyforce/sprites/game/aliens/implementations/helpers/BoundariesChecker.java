package com.danosoftware.galaxyforce.sprites.game.aliens.implementations.helpers;

import com.danosoftware.galaxyforce.sprites.common.ISprite;

/**
 * Checks if alien is within supplied boundaries.
 */
public class BoundariesChecker {

    private final ISprite sprite;
    private final int minX;
    private final int maxX;
    private final int minY;
    private final int maxY;
    private final int centreX;
    private final int centreY;

    public BoundariesChecker(ISprite sprite, int minX, int maxX, int minY, int maxY) {
        this.sprite = sprite;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.centreX = minX + ((maxX - minX) / 2);
        this.centreY = minY + ((maxY - minY) / 2);
    }

    /**
     * Tests if the sprite is outside supplied boundaries by testing it's
     * position/dimensions against all boundary edges.
     */
    public boolean isOutsideBoundaries() {
        return (sprite.y() >= maxY + sprite.halfHeight()
                || sprite.y() <= minY - sprite.halfHeight()
                || sprite.x() >= maxX + sprite.halfWidth()
                || sprite.x() <= minX - sprite.halfWidth());
    }

    /**
     * X centre position of boundary
     */
    public int centreX() {
        return centreX;
    }

    /**
     * Y centre position of boundary
     */
    public int centreY() {
        return centreY;
    }
}
