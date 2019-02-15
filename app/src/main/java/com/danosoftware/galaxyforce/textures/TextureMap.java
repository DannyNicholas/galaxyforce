package com.danosoftware.galaxyforce.textures;

import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.MenuSpriteIdentifier;

/**
 * Enum representing each texture map.
 * <p>
 * Each texture map describes the PNG image file, XML property file,
 * associated sprite identifiers and font identifiers.
 */
public enum TextureMap {

    GAME(
            "game.png",
            "game.xml",
            GameSpriteIdentifier.values(),
            GameSpriteIdentifier.FONT_MAP),
    MENU(
            "menuTextures.png",
            "menuTextures.xml",
            MenuSpriteIdentifier.values(),
            MenuSpriteIdentifier.FONT_MAP);

    private final String textureImage;
    private final String textureXml;
    private final ISpriteIdentifier[] spriteIdentifiers;
    private final ISpriteIdentifier fontIdentifier;

    TextureMap(
            String textureImage,
            String textureXml,
            ISpriteIdentifier[] spriteIdentifiers,
            ISpriteIdentifier fontIdentifier) {
        this.textureImage = textureImage;
        this.textureXml = textureXml;
        this.spriteIdentifiers = spriteIdentifiers;
        this.fontIdentifier = fontIdentifier;
    }

    public String getTextureImage() {
        return textureImage;
    }

    public String getTextureXml() {
        return textureXml;
    }

    public ISpriteIdentifier[] getSpriteIdentifiers() {
        return spriteIdentifiers;
    }

    public ISpriteIdentifier getFontIdentifier() {
        return fontIdentifier;
    }
}
