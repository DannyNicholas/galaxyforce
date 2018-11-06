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

    // are bounds cached?
    // once cached, bounds are valid until sprite moves or changes size
    private boolean boundsCached;

    // are half-height/width dimensions cached?
    // once cached, dimensions are valid until sprite changes size
    private boolean dimensionsCached;

    public AbstractCollidingSprite(
            final ISpriteIdentifier spriteId,
            final int x,
            final int y,
            final int rotation) {
        super(spriteId, x, y, rotation);
        this.dimensionsCached = false;
        this.boundsCached = false;
    }

    public AbstractCollidingSprite(
            final ISpriteIdentifier spriteId,
            final int x,
            final int y) {
        this(spriteId, x, y, 0);
    }

    @Override
    public Rectangle getBounds() {
        if (boundsCached) {
            return bounds;
        }
        return bounds();
    }

    @Override
    public void move(int x, int y) {
        super.move(x, y);
        this.boundsCached = false;
    }

    @Override
    public void changeType(ISpriteIdentifier newSpriteId) {
        if (this.spriteId() != newSpriteId) {
            this.dimensionsCached = false;
            this.boundsCached = false;
        }
        super.changeType(newSpriteId);
    }

    // create and return bounds.
    // will try to create bounds from sprite properties (if available) and cache result
    // otherwise zero width/height bounds are returned
    private Rectangle bounds() {
        if (dimensionsCached || spriteId().getProperties() != null) {
            cacheBounds();
            return bounds;
        }
        return new Rectangle(x(), y(), 0, 0);
    }

    // cache dimensions and bounds
    private void cacheBounds() {
        if (!dimensionsCached) {
            this.halfHeight = height() / 2;
            this.halfWidth = width() / 2;
            this.dimensionsCached = true;
        }

        this.bounds = new Rectangle(
                x() - halfWidth,
                y() - halfHeight,
                width(),
                height());
        this.boundsCached = true;
    }
}
