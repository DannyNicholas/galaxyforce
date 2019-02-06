package com.danosoftware.galaxyforce.sprites.common;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.utilities.Rectangle;

public abstract class AbstractCollidingSprite extends AbstractMovingSprite implements ICollidingSprite {

    // sprite bounds for collision detection
    private Rectangle bounds;

    // are bounds cached?
    // once cached, bounds are valid until sprite moves or changes size
    private boolean boundsCached;

    AbstractCollidingSprite(
            final ISpriteIdentifier spriteId,
            final int x,
            final int y,
            final int rotation) {
        super(spriteId, x, y, rotation);
        this.boundsCached = false;
    }

    protected AbstractCollidingSprite(
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
    public void moveY(int y) {
        super.moveY(y);
        this.boundsCached = false;
    }

    @Override
    public void moveByDelta(int xDelta, int yDelta) {
        super.moveByDelta(xDelta, yDelta);
        this.boundsCached = false;
    }

    @Override
    public void moveYByDelta(int yDelta) {
        super.moveYByDelta(yDelta);
        this.boundsCached = false;
    }

    @Override
    public void changeType(ISpriteIdentifier newSpriteId) {
        if (this.spriteId() != newSpriteId) {
            this.boundsCached = false;
        }
        super.changeType(newSpriteId);
    }

    // create and return bounds.
    // will try to create bounds from sprite properties (if available) and cache result
    // otherwise zero width/height bounds are returned
    private Rectangle bounds() {
        if (spriteId().getProperties() != null) {
            cacheBounds();
            return bounds;
        }
        return new Rectangle(x(), y(), 0, 0);
    }

    // cache dimensions and bounds
    private void cacheBounds() {

        this.bounds = new Rectangle(
                x() - halfWidth(),
                y() - halfHeight(),
                width(),
                height());
        this.boundsCached = true;
    }
}
