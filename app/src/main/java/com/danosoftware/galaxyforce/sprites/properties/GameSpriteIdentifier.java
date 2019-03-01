package com.danosoftware.galaxyforce.sprites.properties;

import com.danosoftware.galaxyforce.textures.Texture;

public enum GameSpriteIdentifier implements ISpriteIdentifier {
    /* Alien sprites */

    ALIEN_OCTOPUS_LEFT("alienOctoLeft_64px.png"), ALIEN_OCTOPUS_RIGHT("alienOctoRight_64px.png"),

    ALIEN_MINION_NORMAL("Yellow_64px.png"), ALIEN_MINION_FUZZ1("YellowB_64px.png"), ALIEN_MINION_FUZZ2("YellowC_64px.png"),

    ASTEROID("asteroid_64px.png"),

    DRAGON_HEAD("alienDragon78px.png"), DRAGON_BODY("dragonBody2.png"),

    STORK_1("Stork1.png"), STORK_2("Stork2.png"),

    INSECT_WINGS_UP("alienBuzzUp64px.png"), INSECT_WINGS_DOWN("alienBuzzDown64px.png"),

    DROID("alienHead48px.png"),

    ALIEN_PEARL("alienPearl48px.png"),

    ALIEN_HELMET("alienHelmet64px.png"),

    ALIEN_GOBBY_LEFT("alienGobbyLeft48px.png"), ALIEN_GOBBY_RIGHT("alienGobbyRight48px.png"),



    /* Base Sprites */

    BASE("base.png"),
    BASE_RIGHT("BaseUpRight_64px.png"),
    BASE_LEFT("baseLeft.png"),
    HELPER("baseHelper.png"),

//    BASE_SHIELD_ONE("bigShieldFour.png"), BASE_SHIELD_TWO("bigShieldTwo.png"), BASE_SHIELD_THREE("bigShieldThree.png"), BASE_SHIELD_FOUR("bigShieldFour.png"),
//    BASE_SHIELD_ONE("shieldOne.png"), BASE_SHIELD_TWO("shieldTwo.png"), BASE_SHIELD_THREE("shieldThree.png"),


    // pulsing shield when base stationary
    BASE_SHIELD_ONE("shieldOutlineOne.png"),
    BASE_SHIELD_TWO("shieldOutlineTwo.png"),
    BASE_SHIELD_THREE("shieldOutlineThree.png"),
    BASE_SHIELD_FOUR("shieldOutlineFour.png"),

    // pulsing shield when base turning left
    BASE_LEFT_SHIELD_ONE("leftShieldOutlineOne.png"),
    BASE_LEFT_SHIELD_TWO("leftShieldOutlineTwo.png"),
    BASE_LEFT_SHIELD_THREE("leftShieldOutlineThree.png"),
    BASE_LEFT_SHIELD_FOUR("leftShieldOutlineFour.png"),

    // pulsing shield when base turning right
    BASE_RIGHT_SHIELD_ONE("rightShieldOutlineOne.png"),
    BASE_RIGHT_SHIELD_TWO("rightShieldOutlineTwo.png"),
    BASE_RIGHT_SHIELD_THREE("rightShieldOutlineThree.png"),
    BASE_RIGHT_SHIELD_FOUR("rightShieldOutlineFour.png"),

    // pulsing shield for helper base
    HELPER_SHIELD_ONE("shieldOutlineHelperOne.png"),
    HELPER_SHIELD_TWO("shieldOutlineHelperTwo.png"),
    HELPER_SHIELD_THREE("shieldOutlineHelperThree.png"),
    HELPER_SHIELD_FOUR("shieldOutlineHelperFour.png"),

//    BASE_SPIN_1("BaseUpSpin1_64px.png"), BASE_SPIN_2("BaseUpSpin2_64px.png"), BASE_SPIN_3("BaseUpSpin3_64px.png"),

//    BASE_FLAT("BaseFlat_64px.png"),

//    BASE_FLIP("BaseDown_64px.png"), BASE_FLIP_SPIN_1("BaseDownSpin1_64px.png"), BASE_FLIP_SPIN_2("BaseDownSpin2_64px.png"), BASE_FLIP_SPIN_3(
//            "BaseDownSpin3_64px.png"),

//    BASE_RIGHT("BaseUpRight_64px.png"), BASE_LEFT("baseLeft.png"),
//    BASE_FLIP_RIGHT("BaseDownRight_64px.png"), BASE_FLIP_LEFT(
//            "BaseDownLeft_64px.png"),

    /* Base Helper */

//    HELPER("baseHelper.png"),

//    HELPER_SPIN_1("BaseHelperSpin1_64px.png"), HELPER_SPIN_2("BaseHelperSpin2_64px.png"), HELPER_SPIN_3(
//            "BaseHelperSpin3_64px.png"),
//
//    HELPER_FLAT("BaseHelperFlat_64px.png"),
//
//    HELPER_FLIP("BaseHelperDown_64px.png"), HELPER_FLIP_SPIN_1("BaseHelperDownSpin1_64px.png"), HELPER_FLIP_SPIN_2(
//            "BaseHelperDownSpin2_64px.png"), HELPER_FLIP_SPIN_3("BaseHelperDownSpin3_64px.png"),

    /* Power Up Sprites - TODO Laser needs it's own sprite */

//    POWERUP_BATTERY("pwrUpBattery34px.png"),

    POWERUP_LIFE("pwrUpLife34px.png"),
    POWERUP_MISSILE_FAST("pwrUpFast34px.png"), POWERUP_MISSILE_BLAST("pwrUpBlast34px.png"),
    POWERUP_MISSILE_GUIDED("pwrUpDirectional34px.png"), POWERUP_MISSILE_PARALLEL("pwrUpParallel34px.png"),
    POWERUP_MISSILE_SPRAY("pwrUpSpray34px.png"), POWERUP_MISSILE_LASER("pwrUpLaser34px.png"),
    POWERUP_SHIELD("pwrUpShield34px.png"), POWERUP_HELPER_BASES("pwrUpHelper34px.png"),

//    MISSILE_HIT_1("hit1-54px.png"), MISSILE_HIT_2("hit2-54px.png"), MISSILE_HIT_3("hit3-54px.png"),

    LASER_ALIEN("laserInvaderGreen9px.png"), LASER_BASE("laserHeroNew9px.png"),

    EXPLODE_01("bang1aSmall.png"), EXPLODE_02("bang2aSmall.png"), EXPLODE_03("bang3aSmall.png"),

    STAR("Star.png"), STAR_BLACK("StarBLACK.png"), STAR_RED("StarRED.png"), STAR_BLUE("StarBLUE.png"), STAR_SPARKLE("StarSPARKLE.png"),

//    CONTROL("controlOpaque.png"), JOYSTICK("joystickOpaque.png"),

//    DRAG_1("Drag_1_54px_simple.png"), DRAG_2("Drag_2_54px_simple.png"),

    PAUSE_BUTTON_UP("PauseButtonUp100pxMasked.png"), PAUSE_BUTTON_DOWN("PauseButtonDown100pxMasked.png"),

//    FLIP_BUTTON_UP("FlipButtonUp100pxMasked.png"), FLIP_BUTTON_DOWN("FlipButtonDown100pxMasked.png"),

    FLAG_1("Flag_1.png"), FLAG_5("Flag_5.png"), FLAG_10("Flag_10.png"), FLAG_50("Flag_50.png"),
//    FLAG_100("Flag_100.png"),

//    ENERGY_GREEN("Energy_Green28px.png"), ENERGY_ORANGE("Energy_Orange28px.png"), ENERGY_RED("Energy_Red28px.png"), ENERGY_YELLOW(
//            "Energy_Yellow28px.png"),
//
//    ENERGY_BAR_LEFT("EnergyBarLeft.png"), ENERGY_BAR_MIDDLE("EnergyBarMiddle.png"), ENERGY_BAR_RIGHT("EnergyBarRight.png"),

    MENU_BUTTON_UP("BlueButton360px.png"), MENU_BUTTON_DOWN("BlueButton360pxPressed.png"),

    LIVES("Life_32px.png"),

    FONT_MAP("GalaxyForceFont_30x38-crop.png");

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
