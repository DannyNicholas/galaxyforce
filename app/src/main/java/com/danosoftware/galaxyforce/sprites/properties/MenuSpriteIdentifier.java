package com.danosoftware.galaxyforce.sprites.properties;

import com.danosoftware.galaxyforce.textures.Texture;

public enum MenuSpriteIdentifier implements ISpriteIdentifier {

    // main logo
    GALAXY_FORCE("PlutoPanicLogo"),

    // menu buttons
    MAIN_MENU("BlueButton360px"),
    MAIN_MENU_PRESSED("BlueButton360pxPressed"),

    // wave select buttons
    LEVEL_FRAME("GreenButton128px"),
    LEVEL_FRAME_PRESSED("GreenButton128pxPressed"),

    // locked wave buttons
    LEVEL_FRAME_LOCKED("LockedButton128px"),
    LEVEL_FRAME_LOCKED_PRESSED("LockedButton128pxPressed"),

    // next zone buttons
    NEXT_LEVEL("GreenRightArrow96px"),
    NEXT_LEVEL_PRESSED("GreenRightArrowPressed80px"),

    // previous zone buttons
    PREVIOUS_LEVEL("GreenLeftArrow96px"),
    PREVIOUS_LEVEL_PRESSED("GreenLeftArrowPressed80px"),

    // option buttons
    OPTION_UNSELECTED("GreyButton180px"),
    OPTION_SELECTED("GoldButton180pxPressed"),

    // stars
    STAR("Star"),
    STAR_BLACK("StarBLACK"),
    STAR_RED("StarRED"),
    STAR_BLUE("StarBLUE"),
    STAR_SPARKLE("StarSPARKLE"),

    // pluto
    PLUTO("Pluto"),

    // bases
    BASE("BaseLarge"),
    BASE_TILT("BaseTiltLarge"),

    // fonts
    FONT_MAP("GalaxyForceFont_30x38");


    private final String name;
    private SpriteProperties properties;

    MenuSpriteIdentifier(String name) {
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
     */
    @Override
    public void updateProperties(Texture texture) {
        this.properties = new SpriteProperties(name, texture);
    }

    @Override
    public String getName() {
        return name;
    }
}
