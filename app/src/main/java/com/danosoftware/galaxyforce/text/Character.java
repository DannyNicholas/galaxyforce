package com.danosoftware.galaxyforce.text;

import com.danosoftware.galaxyforce.textures.TextureRegion;

/**
 * Represents a text character to be drawn to the screen.
 */
public class Character {

  private final float x, y;
  private final TextureRegion region;

  public Character(
      float x,
      float y,
      TextureRegion region) {
    this.x = x;
    this.y = y;
    this.region = region;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public TextureRegion getRegion() {
    return region;
  }
}
