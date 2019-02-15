package com.danosoftware.galaxyforce.textures;

/**
 * Holds pre-calculated properties for an individual texture region.
 * Used to aid sprite drawing.
 */
public class TextureRegion {
    private final float u1, v1;
    private final float u2, v2;

    public TextureRegion(Texture texture, float x, float y, float width, float height) {
        this.u1 = x / texture.width();
        this.v1 = y / texture.height();
        this.u2 = this.u1 + width / texture.width();
        this.v2 = this.v1 + height / texture.height();
    }

    public float getU1() {
        return u1;
    }

    public float getV1() {
        return v1;
    }

    public float getU2() {
        return u2;
    }

    public float getV2() {
        return v2;
    }
}
