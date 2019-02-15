package com.danosoftware.galaxyforce.sprites.properties;

import android.util.Log;

import com.danosoftware.galaxyforce.textures.Texture;
import com.danosoftware.galaxyforce.textures.TextureDetail;
import com.danosoftware.galaxyforce.textures.TextureRegion;

public class SpriteProperties implements ISpriteProperties {
    private static final String TAG = "SpriteProperty";

    // details for texture region
    private final int width;
    private final int height;
    private final int xPos;
    private final int yPos;

    private final TextureRegion textureRegion;

    /**
     * Create a sprite property from the supplied sprite name and texture map.
     *
     * @param name    - name of sprite
     * @param texture - texture map containing sprite
     */
    public SpriteProperties(String name, Texture texture) {

        TextureDetail textureDetails = texture.getTextureDetail(name);

        // populate texture region properties
        this.xPos = textureDetails.xPos;
        this.yPos = textureDetails.yPos;
        this.width = textureDetails.width;
        this.height = textureDetails.height;

        // create texture region
        this.textureRegion = new TextureRegion(texture, xPos, yPos, width, height);

        Log.v(TAG, "Name: " + name + ". x: " + xPos + ". y: " + yPos + ". w: " + width + ". h : " + height + ".");
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getxPos() {
        return xPos;
    }

    @Override
    public int getyPos() {
        return yPos;
    }

    @Override
    public TextureRegion getTextureRegion() {
        return textureRegion;
    }
}
