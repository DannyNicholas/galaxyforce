package com.danosoftware.galaxyforce.sprites.properties;

import com.danosoftware.galaxyforce.textures.Texture;

public interface ISpriteIdentifier {
    /**
     * Returns the sprite's properties (e.g. height or width). Properties will
     * not be available until after updateProperties() is called.
     */
    ISpriteProperties getProperties();

    /**
     * Update the sprite properties using the supplied texture map.
     * <p>
     * This method must be called once a new texture is available or refreshed
     * after a resume. Sprite properties will not be available until this is
     * called.
     */
    void updateProperties(Texture texture);

    String getName();
}