package com.danosoftware.galaxyforce.sprites.properties;

import com.danosoftware.galaxyforce.textures.Texture;
import com.danosoftware.galaxyforce.textures.TextureDetail;

public class SpriteProperties implements ISpriteProperties {

    // dimensions for texture region
    private final int width;
    private final int height;

    /**
     * Create a sprite property from the supplied sprite name and texture map.
     *
     * @param name    - name of sprite
     * @param texture - texture map containing sprite
     */
    public SpriteProperties(String name, Texture texture) {
        TextureDetail textureDetails = texture.getTextureDetail(name);
        this.width = textureDetails.getWidth();
        this.height = textureDetails.getHeight();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
