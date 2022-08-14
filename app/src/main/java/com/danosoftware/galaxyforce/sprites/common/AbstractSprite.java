package com.danosoftware.galaxyforce.sprites.common;

import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDimensions;

public abstract class AbstractSprite implements ISprite {

  // sprite's x,y position
  protected float x, y;

  // sprite's angle rotation
  float rotation;

  // sprite's width and height.
  // this is obtained from the sprite's properties
  // but not available until the sprite is loaded.
  // these may not be available on construction
  // but will be cached once available.
  private int width, height;
  private int halfWidth, halfHeight;
  private boolean dimensionsCached;

  // sprite properties
  private SpriteDetails spriteDetails;

  AbstractSprite(
      SpriteDetails spriteId,
      float x,
      float y,
      float rotation) {
    this.spriteDetails = spriteId;
    this.x = x;
    this.y = y;
    this.rotation = rotation;
    this.dimensionsCached = false;
  }

  protected AbstractSprite(
      SpriteDetails spriteId,
      float x,
      float y) {
    this(spriteId, x, y, 0);
  }

  @Override
  public void changeType(SpriteDetails spriteDetails) {
    if (this.spriteDetails != spriteDetails) {
      this.spriteDetails = spriteDetails;
      this.dimensionsCached = false;
    }
  }

  @Override
  public int width() {
    if (dimensionsCached) {
      return width;
    }
    return cacheWidth();
  }

  @Override
  public int height() {
    if (dimensionsCached) {
      return height;
    }
    return cacheHeight();
  }

  @Override
  public int halfWidth() {
    if (dimensionsCached) {
      return halfWidth;
    }
    return cacheHalfWidth();
  }

  @Override
  public int halfHeight() {
    if (dimensionsCached) {
      return halfHeight;
    }
    return cacheHalfHeight();
  }

  @Override
  public float rotation() {
    return rotation;
  }

  @Override
  public float x() {
    return x;
  }

  @Override
  public float y() {
    return y;
  }

  @Override
  public SpriteDetails spriteDetails() {
    return spriteDetails;
  }

  // cache and return width from sprite properties if available
  private int cacheWidth() {
    SpriteDimensions dimensions = spriteDetails.getDimensions();
    if (dimensions != null) {
      cacheDimensions(dimensions);
      return width;
    }
    return 0;
  }

  // cache and return height from sprite properties if available
  private int cacheHeight() {
    SpriteDimensions dimensions = spriteDetails.getDimensions();
    if (dimensions != null) {
      cacheDimensions(dimensions);
      return height;
    }
    return 0;
  }

  // cache and return half-width from sprite properties if available
  private int cacheHalfWidth() {
    SpriteDimensions dimensions = spriteDetails.getDimensions();
    if (dimensions != null) {
      cacheDimensions(dimensions);
      return halfWidth;
    }
    return 0;
  }

  // cache and return half-height from sprite properties if available
  private int cacheHalfHeight() {
    SpriteDimensions dimensions = spriteDetails.getDimensions();
    if (dimensions != null) {
      cacheDimensions(dimensions);
      return halfHeight;
    }
    return 0;
  }

  private void cacheDimensions(SpriteDimensions dimensions) {
    this.width = dimensions.getWidth();
    this.height = dimensions.getHeight();
    this.halfWidth = width / 2;
    this.halfHeight = height / 2;
    this.dimensionsCached = true;
  }
}
