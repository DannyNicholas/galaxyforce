package com.danosoftware.galaxyforce.sprites.properties;

import com.danosoftware.galaxyforce.textures.Texture;

public enum GameSpriteIdentifier implements ISpriteIdentifier {
    /* Alien sprites */

    ALIEN_OCTOPUS_LEFT("alienOctoLeft_64px"), ALIEN_OCTOPUS_RIGHT("alienOctoRight_64px"),

    ALIEN_MINION_NORMAL("Yellow_64px"), ALIEN_MINION_FUZZ1("YellowB_64px"), ALIEN_MINION_FUZZ2("YellowC_64px"),

    ASTEROID("asteroid_64px"),

    DRAGON_HEAD("alienDragon78px"), DRAGON_BODY("dragonBody2"),

    STORK_1("Stork1"), STORK_2("Stork2"),

    INSECT_WINGS_UP("alienBuzzUp64px"), INSECT_WINGS_DOWN("alienBuzzDown64px"),

    DROID("alienHead48px"),

    ALIEN_PEARL("alienPearl48px"),

    ALIEN_HELMET("alienHelmet64px"),

    ALIEN_GOBBY_LEFT("alienGobbyLeft48px"), ALIEN_GOBBY_RIGHT("alienGobbyRight48px"),

    // bases
    BASE("base"),
    BASE_RIGHT("baseRight"),
    BASE_LEFT("baseLeft"),
    HELPER("baseHelper"),

    // pulsing shield when base stationary
    BASE_SHIELD_ONE("shieldOutlineOne"),
    BASE_SHIELD_TWO("shieldOutlineTwo"),
    BASE_SHIELD_THREE("shieldOutlineThree"),
    BASE_SHIELD_FOUR("shieldOutlineFour"),

    // pulsing shield when base turning left
    BASE_LEFT_SHIELD_ONE("leftShieldOutlineOne"),
    BASE_LEFT_SHIELD_TWO("leftShieldOutlineTwo"),
    BASE_LEFT_SHIELD_THREE("leftShieldOutlineThree"),
    BASE_LEFT_SHIELD_FOUR("leftShieldOutlineFour"),

    // pulsing shield when base turning right
    BASE_RIGHT_SHIELD_ONE("rightShieldOutlineOne"),
    BASE_RIGHT_SHIELD_TWO("rightShieldOutlineTwo"),
    BASE_RIGHT_SHIELD_THREE("rightShieldOutlineThree"),
    BASE_RIGHT_SHIELD_FOUR("rightShieldOutlineFour"),

    // pulsing shield for helper base
    HELPER_SHIELD_ONE("shieldOutlineHelperOne"),
    HELPER_SHIELD_TWO("shieldOutlineHelperTwo"),
    HELPER_SHIELD_THREE("shieldOutlineHelperThree"),
    HELPER_SHIELD_FOUR("shieldOutlineHelperFour"),

    // power ups
    POWERUP_LIFE("pwrUpLife34px"),
    POWERUP_MISSILE_FAST("pwrUpFast34px"),
    POWERUP_MISSILE_BLAST("pwrUpBlast34px"),
    POWERUP_MISSILE_GUIDED("pwrUpDirectional34px"),
    POWERUP_MISSILE_PARALLEL("pwrUpParallel34px"),
    POWERUP_MISSILE_SPRAY("pwrUpSpray34px"),
    POWERUP_MISSILE_LASER("pwrUpLaser34px"),
    POWERUP_SHIELD("pwrUpShield34px"),
    POWERUP_HELPER_BASES("pwrUpHelper34px"),

    // lasers
    LASER_ALIEN("laserInvaderGreen9px"),
    LASER_BASE("laserHeroNew9px"),

    // explosions
    EXPLODE_01("bang1aSmall"),
    EXPLODE_02("bang2aSmall"),
    EXPLODE_03("bang3aSmall"),

    // stars
    STAR("Star"),
    STAR_BLACK("StarBLACK"),
    STAR_RED("StarRED"),
    STAR_BLUE("StarBLUE"),
    STAR_SPARKLE("StarSPARKLE"),

    // pause buttons
    PAUSE_BUTTON_UP("PauseButtonUp100pxMasked"), PAUSE_BUTTON_DOWN("PauseButtonDown100pxMasked"),

    // wave flags
    FLAG_1("Flag_1"),
    FLAG_5("Flag_5"),
    FLAG_10("Flag_10"),
    FLAG_50("Flag_50"),

    // menu buttons
    MENU_BUTTON_UP("BlueButton360px"),
    MENU_BUTTON_DOWN("BlueButton360pxPressed"),

    // lives remaining
    LIVES("Life_32px"),

    // fonts
    FONT_MAP("GalaxyForceFont_30x38-crop");


    private final String name;
    private ISpriteProperties properties;

    GameSpriteIdentifier(String name) {
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
