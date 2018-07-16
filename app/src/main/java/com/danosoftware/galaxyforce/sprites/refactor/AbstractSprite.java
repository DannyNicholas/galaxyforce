package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteState;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.textures.TextureRegion;

public abstract class AbstractSprite implements ISprite {

    // sprite's x,y position
    protected int x, y;

    // sprite's angle rotation
    private int rotation;

    // sprite's width and height
    protected int width, height;

    // sprite's texture to allow image to be drawn
    private TextureRegion textureRegion;

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
    }

    public AbstractSprite(
            ISpriteIdentifier spriteId,
            int x,
            int y) {
        this(spriteId, x, y,0);
    }

    @Override
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void rotate(int rotation) {
        this.rotation = rotation;
    }

    @Override
    public void changeType(ISpriteIdentifier spriteId) {
        this.spriteId = spriteId;
    }

    @Override
    public void changeStatus(SpriteState state) {

    }

    public void refresh() {
        this.width = spriteId.getProperties().getWidth();
        this.height = spriteId.getProperties().getHeight();
        this.textureRegion = spriteId.getProperties().getTextureRegion();
    }

    @Override
    public int width() {
        return spriteId.getProperties().getWidth();
    }

    @Override
    public int height() {
        return spriteId.getProperties().getHeight();
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
    public TextureRegion textureRegion() {
        return spriteId.getProperties().getTextureRegion();
    }
}
