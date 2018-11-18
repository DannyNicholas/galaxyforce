package com.danosoftware.galaxyforce.sprites.properties;

import com.danosoftware.galaxyforce.textures.Texture;

public enum SplashSpriteIdentifier implements ISpriteIdentifier {

    SPLASH_SCREEN("GalaxyForce7-textEffect.png");

    private final String name;
    private SpriteProperties properties;

    SplashSpriteIdentifier(String name) {
        this.name = name;
    }

    @Override
    public ISpriteProperties getProperties() {
        return properties;
    }

    /**
     * Update the sprite properties using this sprite's name and the supplied
     * texture map.
     * <p>
     * This method can be called once a texture is available or refreshed after
     * a resume.
     *
     * @param texture
     */
    @Override
    public void updateProperties(Texture texture) {
        this.properties = new SpriteProperties(name, texture);
    }

}
