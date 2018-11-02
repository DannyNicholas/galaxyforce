package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.utilities.Rectangle;

public abstract class AbstractCollidingSprite extends AbstractMovingSprite implements ICollidingSprite {

    // sprite bounds for collision detection
    private Rectangle bounds;

    // sprite's half-width and half-width in pixels.
    // cached to speed up bounds recalculations.
    // this is calculated from the sprite's properties
    // but not available until the sprite is loaded.
    // these may not be available on construction
    // but will be cached once available.
    private int halfHeight;
    private int halfWidth;
    private boolean cachedDimensions = false;

    public AbstractCollidingSprite(
            final ISpriteIdentifier spriteId,
            final int x,
            final int y,
            final int rotation) {
        super(spriteId, x, y, rotation);
        cacheDimensions();
    }

    public AbstractCollidingSprite(
            final ISpriteIdentifier spriteId,
            final int x,
            final int y) {
        this(spriteId, x, y, 0);
    }

    @Override
    public Rectangle getBounds() {
        if (cachedDimensions) {
            return bounds;
        }
        return cacheDimensions();
    }

    @Override
    public void move(int x, int y) {
        super.move(x, y);
        updateBounds();
    }

    @Override
    public void changeType(ISpriteIdentifier newSpriteId) {
        ISpriteIdentifier oldSprite = spriteId();
        super.changeType(newSpriteId);
        if (oldSprite != newSpriteId) {
            cacheDimensions();
        }
    }

    // cache bounds using sprite properties if available
    private Rectangle cacheDimensions() {
        if (spriteId().getProperties() != null) {
            this.halfHeight = this.height() / 2;
            this.halfWidth = this.width() / 2;
            this.cachedDimensions = true;
            updateBounds();
            return bounds;
        }

        return new Rectangle(
                0,
                0,
                0,
                0);
    }

    /**
     * Recalculates sprite's bounding rectangle.
     * Used for collision detection.
     * Must be updated on sprite creation,
     * sprite change and after every position move.
     */
    private void updateBounds() {
        if (cachedDimensions) {
            this.bounds = new Rectangle(
                    x() - halfWidth,
                    y() - halfHeight,
                    width(),
                    height());
        }
    }
}
