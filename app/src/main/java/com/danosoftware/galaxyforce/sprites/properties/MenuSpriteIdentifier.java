package com.danosoftware.galaxyforce.sprites.properties;

import com.danosoftware.galaxyforce.textures.Texture;

public enum MenuSpriteIdentifier implements ISpriteIdentifier {

    GALAXY_FORCE("PlutoPanicLogo"),

    MAIN_MENU("BlueButton360px"), MAIN_MENU_PRESSED("BlueButton360pxPressed"),

    LEVEL_FRAME("GreenButton128px"), LEVEL_FRAME_PRESSED("GreenButton128pxPressed"),

    LEVEL_FRAME_LOCKED("LockedButton128px"), LEVEL_FRAME_LOCKED_PRESSED("LockedButton128pxPressed"),

    NEXT_LEVEL("GreenRightArrow128px"), NEXT_LEVEL_PRESSED("GreenRightArrowPressed128px"),

    PREVIOUS_LEVEL("GreenLeftArrow128px"), PREVIOUS_LEVEL_PRESSED("GreenLeftArrowPressed128px"),

    OPTION_UNSELECTED("GreyButton180px"), OPTION_SELECTED("GoldButton180pxPressed"),

    STAR("Star"), STAR_BLACK("StarBLACK"), STAR_RED("StarRED"), STAR_BLUE("StarBLUE"), STAR_SPARKLE("StarSPARKLE"),

    //TODO - remove from game complete screen
    BASE("BlueButton360px"),

    PLUTO("pluto"),

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
}
