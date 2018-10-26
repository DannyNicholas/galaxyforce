package com.danosoftware.galaxyforce.textures;

import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.MenuSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.SplashSpriteIdentifier;

public enum TextureMap
{

    /*
     * enum for each screen containing the linked texture map, sprite xml
     * details, array of the sprite identifiers and an.
     */
    GAME("game.png", "game.xml", GameSpriteIdentifier.values(), GameSpriteIdentifier.FONT_MAP),

    MENU("menus.png", "menus.xml", MenuSpriteIdentifier.values(), MenuSpriteIdentifier.FONT_MAP),

    SPLASH("splash.png", "splash.xml", SplashSpriteIdentifier.values(), null);

    // variables storing current texture and xml details
    private final String textureImage;
    private final String textureXml;

    // array of sprite identifiers for each screen
    private final ISpriteIdentifier[] spriteIdentifiers;

    // identifier of fonts sprite (if one exists)
    private final ISpriteIdentifier fontIdentifier;

    TextureMap(String textureImage, String textureXml, ISpriteIdentifier[] spriteIdentifiers, ISpriteIdentifier fontIdentifier)
    {
        this.textureImage = textureImage;
        this.textureXml = textureXml;
        this.spriteIdentifiers = spriteIdentifiers;
        this.fontIdentifier = fontIdentifier;
    }

    public String getTextureImage()
    {
        return textureImage;
    }

    public String getTextureXml()
    {
        return textureXml;
    }

    public ISpriteIdentifier[] getSpriteIdentifiers()
    {
        return spriteIdentifiers;
    }

    public ISpriteIdentifier getFontIdentifier()
    {
        return fontIdentifier;
    }
}
