package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.utilities.Rectangle;

public abstract class AbstractCollidingSprite extends AbstractMovingSprite implements ICollidingSprite {

    // sprite bounds for collision detection
    private Rectangle bounds;

    // sprite's half-width and half-width in pixels.
    // cached to speed up bounds recalculations
    private final int halfHeight;
    private final int halfWidth;

    public AbstractCollidingSprite(
            final ISpriteIdentifier spriteId,
            final int x,
            final int y,
            final int rotation) {

        super(spriteId, x, y, rotation);
        this.halfHeight = this.height() / 2;
        this.halfWidth = this.width() / 2;
        updateBounds();
    }

    public AbstractCollidingSprite(
            final ISpriteIdentifier spriteId,
            final int x,
            final int y) {

        this(spriteId, x, y,0);
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void move(int x, int y) {
        super.move(x, y);
        updateBounds();
    }

    /**
     * Recalculates sprite's bounding rectangle.
     * Used for collision detection.
     * Must be updated on sprite creation and after
     * every position move.
     */
    private void updateBounds()
    {
        this.bounds = new Rectangle(
                x - halfWidth,
                y - halfHeight,
                width(),
                height());
    }
}
