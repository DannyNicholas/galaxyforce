package com.danosoftware.galaxyforce.sprites.properties;

import com.danosoftware.galaxyforce.textures.Texture;

public enum GameSpriteIdentifier implements ISpriteIdentifier {
    /* Alien sprites */

    ALIEN_OCTOPUS_LEFT("alienOctoLeft_64px"), ALIEN_OCTOPUS_RIGHT("alienOctoRight_64px"),

    // octopus
    OCTOPUS_LEFT("octopusBigLeft"), OCTOPUS_RIGHT("octopusBigRight"),
    OCTOPUS_LEFT_HIT("octopusBigHitLeft"), OCTOPUS_RIGHT_HIT("octopusBigHitRight"),

    ALIEN_MINION_NORMAL("Yellow_64px"), ALIEN_MINION_FUZZ1("YellowB_64px"), ALIEN_MINION_FUZZ2("YellowC_64px"),

    ASTEROID("asteroid_64px"),

    // dragon - head and body
    DRAGON_HEAD_LEFT("dragonLeft"),
    DRAGON_HEAD_RIGHT("dragonRight"),
    DRAGON_HEAD_LEFT_HIT("dragonHitLeft"),
    DRAGON_HEAD_RIGHT_HIT("dragonHitRight"),
    DRAGON_BODY("dragonBody"),
    DRAGON_BODY_HIT("dragonBodyHit"),

    STORK_1("Stork1"), STORK_2("Stork2"),

    // buzzer - small flying insect
    INSECT_WINGS_UP("buzzerWingsUp"),
    INSECT_WINGS_DOWN("buzzerWingsDown"),
    INSECT_WINGS_UP_HIT("buzzerWingsUpHit"),
    INSECT_WINGS_DOWN_HIT("buzzerWingsDownHit"),

    // mother buzzer - big flying insect
    MOTHER_BUZZER_WINGS_DOWN("motherBuzzerWingsDown"),
    MOTHER_BUZZER_WINGS_UP("motherBuzzerWingsUp"),
    MOTHER_BUZZER_WINGS_DOWN_HIT("motherBuzzerWingsDownHit"),
    MOTHER_BUZZER_WINGS_UP_HIT("motherBuzzerWingsUpHit"),

    // droid alien
    DROID("skull"),
    DROID_HIT("skullHit"),

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
    EXPLODE_01("explodeOne"),
    EXPLODE_02("explodeTwo"),
    EXPLODE_03("explodeThree"),
    EXPLODE_04("explodeFour"),
    EXPLODE_05("explodeFive"),

    // big-explosions
    EXPLODE_BIG_01("bigExplosionOne"),
    EXPLODE_BIG_02("bigExplosionTwo"),
    EXPLODE_BIG_03("bigExplosionThree"),
    EXPLODE_BIG_04("bigExplosionFour"),
    EXPLODE_BIG_05("bigExplosionFive"),

    // massive-explosions
    BASE_EXPLODE_01("baseExplosion01"),
    BASE_EXPLODE_02("baseExplosion02"),
    BASE_EXPLODE_03("baseExplosion03"),
    BASE_EXPLODE_04("baseExplosion04"),
    BASE_EXPLODE_05("baseExplosion05"),
    BASE_EXPLODE_06("baseExplosion06"),
    BASE_EXPLODE_07("baseExplosion07"),
    BASE_EXPLODE_08("baseExplosion08"),
    BASE_EXPLODE_09("baseExplosion09"),
    BASE_EXPLODE_10("baseExplosion10"),
    BASE_EXPLODE_11("baseExplosion11"),

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
