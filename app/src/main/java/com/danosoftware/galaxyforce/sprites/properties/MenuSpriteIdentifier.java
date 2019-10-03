package com.danosoftware.galaxyforce.sprites.properties;

import com.danosoftware.galaxyforce.textures.Texture;

public enum MenuSpriteIdentifier implements ISpriteIdentifier {

    // main logo
    GALAXY_FORCE("logo"),

    // google play icon
    GOOGLE_PLAY("googlePlay"),

    // menu buttons
    MAIN_MENU("blueButtonUp"),
    MAIN_MENU_PRESSED("blueButtonDown"),

    // wave select buttons
    LEVEL_FRAME("greenButtonUp"),
    LEVEL_FRAME_PRESSED("greenButtonDown"),

    // locked wave buttons
    LEVEL_FRAME_LOCKED("lockedButtonUp"),
    LEVEL_FRAME_LOCKED_PRESSED("lockedButtonDown"),

    // next zone buttons
    NEXT_LEVEL("greenRightArrowUp"),
    NEXT_LEVEL_PRESSED("greenRightArrowDown"),

    // previous zone buttons
    PREVIOUS_LEVEL("greenLeftArrowUp"),
    PREVIOUS_LEVEL_PRESSED("greenLeftArrowDown"),

    // option buttons
    OPTION_UNSELECTED("greyButtonUp"),
    OPTION_SELECTED("goldButtonDown"),

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
    FONT_MAP("GalaxyForceFont_30x38-crop");


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
