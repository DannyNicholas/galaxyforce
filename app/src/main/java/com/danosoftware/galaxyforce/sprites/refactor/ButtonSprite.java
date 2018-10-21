package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.utilities.Rectangle;

public class ButtonSprite extends AbstractSprite implements IButtonSprite {

    // sprite bounds for button
    private final Rectangle bounds;

    public ButtonSprite(
            ISpriteIdentifier spriteId,
            int x,
            int y) {
        super(spriteId, x, y);
        this.bounds = createBounds();
    }

    public ButtonSprite(
            ISpriteIdentifier spriteId,
            int x,
            int y,
            int buffer) {
        super(spriteId, x, y);
        this.bounds = createBounds(buffer);
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Calculates sprite's bounding rectangle.
     * Used for button click detection.
     */
    private Rectangle createBounds()
    {
        return new Rectangle(
                x - (this.height() / 2),
                y - (this.width() / 2),
                width(),
                height());
    }

    /**
     * Calculates sprite's bounding rectangle plus a buffer.
     * Used for button click detection.
     * Buffer increases the clickable area of button.
     */
    private Rectangle createBounds(int buffer)
    {
        return new Rectangle(
                x - (this.height() / 2) - buffer,
                y - (this.width() / 2) - buffer,
                width() + (buffer * 2),
                height() + (buffer * 2));
    }
}
