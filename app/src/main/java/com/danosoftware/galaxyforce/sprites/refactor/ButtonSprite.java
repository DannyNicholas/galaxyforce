package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.utilities.Rectangle;

public class ButtonSprite extends AbstractSprite implements IButtonSprite {

    // sprite bounds for button
    private Rectangle bounds;

    // buffer increases the clickable area of button
    private final int buffer;

    // are bounds cached?
    private boolean boundsCached;

    public ButtonSprite(
            ISpriteIdentifier spriteId,
            int x,
            int y) {
        this(spriteId, x, y, 0);
    }

    public ButtonSprite(
            ISpriteIdentifier spriteId,
            int x,
            int y,
            int buffer) {
        super(spriteId, x, y);
        this.buffer = buffer;
        this.boundsCached = false;
    }

    @Override
    public Rectangle getBounds() {
        if (boundsCached) {
            return bounds;
        }
        return bounds();
    }

    // create and return bounds.
    // will try to create bounds from sprite properties (if available) and cache result.
    // otherwise zero width/height bounds are returned.
    private Rectangle bounds() {
        if (spriteId().getProperties() != null) {
            bounds = new Rectangle(
                    x - (width() / 2) - buffer,
                    y - (height() / 2) - buffer,
                    width() + (buffer * 2),
                    height() + (buffer * 2));
            boundsCached = true;
            return bounds;
        }
        return new Rectangle(
                x(),
                y(),
                0,
                0);
    }
}
