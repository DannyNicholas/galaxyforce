package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteProperties;

public abstract class AbstractSprite implements ISprite {

    // sprite's x,y position
    protected int x, y;

    // sprite's angle rotation
    protected int rotation;

    // sprite's width and height.
    // this is obtained from the sprite's properties
    // but not available until the sprite is loaded.
    // these may not be available on construction
    // but will be cached once available.
    private int width, height;
    private boolean dimensionsCached;

    // sprite properties
    private ISpriteIdentifier spriteId;

    public AbstractSprite(
            ISpriteIdentifier spriteId,
            int x,
            int y,
            int rotation) {
        this.spriteId = spriteId;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.dimensionsCached = false;
    }

    public AbstractSprite(
            ISpriteIdentifier spriteId,
            int x,
            int y) {
        this(spriteId, x, y,0);
    }

    @Override
    public void changeType(ISpriteIdentifier newSpriteId) {
        if (this.spriteId != newSpriteId) {
            this.spriteId = newSpriteId;
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
    public int rotation() {
        return rotation;
    }

    @Override
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return y;
    }

    @Override
    public ISpriteIdentifier spriteId() {
        return spriteId;
    }

    // cache and return width from sprite properties if available
    private int cacheWidth() {
        ISpriteProperties props = spriteId.getProperties();
        if (props != null) {
            cacheDimensions(props);
            return width;
        }
        return 0;
    }

    // cache and return height from sprite properties if available
    private int cacheHeight() {
        ISpriteProperties props = spriteId.getProperties();
        if (props != null) {
            cacheDimensions(props);
            return height;
        }
        return 0;
    }

    private void cacheDimensions(ISpriteProperties props) {
        this.width = props.getWidth();
        this.height = props.getHeight();
        this.dimensionsCached = true;
    }
}
