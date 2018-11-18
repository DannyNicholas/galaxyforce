package com.danosoftware.galaxyforce.sprites.properties;

import com.danosoftware.galaxyforce.textures.Texture;
import com.danosoftware.galaxyforce.view.Animation;

public enum MenuSpriteIdentifier implements ISpriteIdentifier {

    GALAXY_FORCE("GalaxyForceLogo.png"),

    MAIN_MENU("BlueButton360px.png"), MAIN_MENU_PRESSED("BlueButton360pxPressed.png"),

    LEVEL_FRAME("GreenButton128px.png"), LEVEL_FRAME_PRESSED("GreenButton128pxPressed.png"),

    LEVEL_FRAME_LOCKED("LockedButton128px.png"), LEVEL_FRAME_LOCKED_PRESSED("LockedButton128pxPressed.png"),

    NEXT_LEVEL("NextLevels.png"), NEXT_LEVEL_PRESSED("NextLevelsPressed.png"),

    PREVIOUS_LEVEL("PreviousLevels.png"), PREVIOUS_LEVEL_PRESSED("PreviousLevelsPressed.png"),

    OPTION_UNSELECTED("GreyButton180px.png"), OPTION_SELECTED("GoldButton180pxPressed.png"),

    FACEBOOK("FaceBookOriginalButton128px.png"), FACEBOOK_PRESSED("FaceBookOriginalButton128pxPressed.png"),

    TWITTER("TwitterOriginalButton128px.png"), TWITTER_PRESSED("TwitterButtonOriginal128pxPressed.png"),

    STAR("Star.png"), STAR_BLACK("StarBLACK.png"), STAR_RED("StarRED.png"), STAR_BLUE("StarBLUE.png"), STAR_SPARKLE("StarSPARKLE.png"),

    BASE("shooterSmall.png"),

    FONT_MAP("GalaxyForceFont_30x38.png");

    // star animation #1
    private static final Animation starAnimation1 = new Animation(0.5f, MenuSpriteIdentifier.STAR, MenuSpriteIdentifier.STAR_SPARKLE,
            MenuSpriteIdentifier.STAR);

    // star animation #2
    private static final Animation starAnimation2 = new Animation(0.5f, MenuSpriteIdentifier.STAR_RED, MenuSpriteIdentifier.STAR_BLACK,
            MenuSpriteIdentifier.STAR_RED);

    // star animation #3
    private static final Animation starAnimation3 = new Animation(0.5f, MenuSpriteIdentifier.STAR_BLUE, MenuSpriteIdentifier.STAR_BLACK,
            MenuSpriteIdentifier.STAR_BLUE);

    // all possible star animations
    public static final Animation[] STAR_ANIMATIONS =
            {MenuSpriteIdentifier.starAnimation1, MenuSpriteIdentifier.starAnimation2, MenuSpriteIdentifier.starAnimation3};

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
     *
     * @param texture
     */
    @Override
    public void updateProperties(Texture texture) {
        this.properties = new SpriteProperties(name, texture);
    }
}
