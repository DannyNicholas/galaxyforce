package com.danosoftware.galaxyforce.sprites.buttons;

import com.danosoftware.galaxyforce.sprites.common.AbstractSprite;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.utilities.Rectangle;

public class ButtonSprite extends AbstractSprite implements IButtonSprite {

  // sprite bounds for button
  private Rectangle bounds;

  // buffer increases the clickable area of button
  private final int buffer;

  // are bounds cached?
  private boolean boundsCached;

  public ButtonSprite(
      SpriteDetails spriteId,
      int x,
      int y) {
    this(spriteId, x, y, 0);
  }

  public ButtonSprite(
      SpriteDetails spriteId,
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
  private Rectangle bounds() {

    Rectangle tmpBounds = new Rectangle(
        x,
        y,
        halfWidth() + buffer,
        halfHeight() + buffer);

    // only cache if dimensions available
    if (width() > 0 && height() > 0) {
      bounds = tmpBounds;
      boundsCached = true;
    }
    return tmpBounds;
  }
}
