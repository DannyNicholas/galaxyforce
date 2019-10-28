package com.danosoftware.galaxyforce.sprites.properties;

import com.danosoftware.galaxyforce.textures.Texture;

public enum GameSpriteIdentifier implements ISpriteIdentifier {

    // octopus
    OCTOPUS_LEFT("octopusLeft"),
    OCTOPUS_RIGHT("octopusRight"),
    OCTOPUS_LEFT_HIT("octopusLeftHit"),
    OCTOPUS_RIGHT_HIT("octopusRightHit"),

    // yellow minion
    MINION("minion"),
    MINION_HIT("minionHit"),

    // green ball spinner
    SPINNER_GREEN_01("spinnerGreen01"),
    SPINNER_GREEN_02("spinnerGreen02"),
    SPINNER_GREEN_03("spinnerGreen03"),
    SPINNER_GREEN_01_HIT("spinnerGreenHit01"),
    SPINNER_GREEN_02_HIT("spinnerGreenHit02"),
    SPINNER_GREEN_03_HIT("spinnerGreenHit03"),

    // pulsating green ball spinner
    SPINNER_PULSE_GREEN_01("spinnerPulseGreen01"),
    SPINNER_PULSE_GREEN_02("spinnerPulseGreen02"),
    SPINNER_PULSE_GREEN_03("spinnerPulseGreen03"),
    SPINNER_PULSE_GREEN_01_HIT("spinnerPulseGreenHit01"),
    SPINNER_PULSE_GREEN_02_HIT("spinnerPulseGreenHit02"),
    SPINNER_PULSE_GREEN_03_HIT("spinnerPulseGreenHit03"),

    // molecule
    MOLECULE("molecule"),
    MOLECULE_HIT("moleculeHit"),

    // big boss
    BIG_BOSS_01("bigBoss01"),
    BIG_BOSS_02("bigBoss02"),
    BIG_BOSS_01_HIT("bigBossHit01"),
    BIG_BOSS_02_HIT("bigBossHit02"),

    // flapping lady bird
    LADYBIRD_01("ladyBird01"),
    LADYBIRD_02("ladyBird02"),
    LADYBIRD_01_HIT("ladyBirdHit01"),
    LADYBIRD_02_HIT("ladyBirdHit02"),

    // zogg
    ZOGG_UP("zogg01"),
    ZOGG_DOWN("zogg02"),
    ZOGG_UP_HIT("zoggHit01"),
    ZOGG_DOWN_HIT("zoggHit02"),

    // asteroid
    ASTEROID("asteroid"),
    ASTEROID_HIT("asteroidHit"),

    // mini-asteroid
    MINI_ASTEROID("miniAsteroid"),
    MINI_ASTEROID_HIT("miniAsteroidHit"),

    // egg
    EGG_CRACK_01("eggCrack01"),
    EGG_CRACK_02("eggCrack02"),
    EGG_CRACK_03("eggCrack03"),
    EGG_CRACK_01_HIT("eggCrackHit01"),
    EGG_CRACK_02_HIT("eggCrackHit02"),
    EGG_CRACK_03_HIT("eggCrackHit03"),

    // egg explosion
    EGG_CRACK_04("eggCrack04"),
    EGG_CRACK_05("eggCrack05"),
    EGG_CRACK_06("eggCrack06"),
    EGG_CRACK_07("eggCrack07"),

    // dragon - head and body
    DRAGON_HEAD_LEFT("dragonLeft"),
    DRAGON_HEAD_RIGHT("dragonRight"),
    DRAGON_HEAD_LEFT_HIT("dragonHitLeft"),
    DRAGON_HEAD_RIGHT_HIT("dragonHitRight"),
    DRAGON_BODY("dragonBody"),
    DRAGON_BODY_HIT("dragonBodyHit"),

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

    // gobby
    GOBBY_LEFT("gobby01"),
    GOBBY_RIGHT("gobby02"),
    GOBBY_LEFT_HIT("gobbyHit01"),
    GOBBY_RIGHT_HIT("gobbyHit02"),

    // bad cat
    BAD_CAT("badCat"),
    BAD_CAT_HIT("badCatHit"),

    // bomb
    BOMB_01("bomb01"),
    BOMB_02("bomb02"),
    BOMB_03("bomb03"),
    BOMB_01_HIT("bombHit01"),
    BOMB_02_HIT("bombHit02"),
    BOMB_03_HIT("bombHit03"),

    // batty
    BATTY_FLAP_DOWN("bat01"),
    BATTY_FLAP_UP("bat02"),
    BATTY_FLAP_DOWN_HIT("bat01Hit"),
    BATTY_FLAP_UP_HIT("bat02Hit"),

    // book
    BOOK_FLAT("book01"),
    BOOK_BEND("book02"),
    BOOK_CLOSED("book03"),
    BOOK_FLAT_HIT("bookHit01"),
    BOOK_BEND_HIT("bookHit02"),
    BOOK_CLOSED_HIT("bookHit03"),

    // bouncer
    BOUNCER_IN("bouncer01"),
    BOUNCER_OUT("bouncer02"),
    BOUNCER_IN_HIT("bouncer01Hit"),
    BOUNCER_OUT_HIT("bouncer02Hit"),

    // droopy
    DROOPY_DOWN("droopyDown"),
    DROOPY_UP("droopyUp"),
    DROOPY_DOWN_HIT("droopyDownHit"),
    DROOPY_UP_HIT("droopyUpHit"),

    // circuit
    CIRCUIT_LEFT("circuit01"),
    CIRCUIT_RIGHT("circuit02"),
    CIRCUIT_LEFT_HIT("circuit01Hit"),
    CIRCUIT_RIGHT_HIT("circuit02Hit"),

    // smokey
    SMOKEY_FLAME_BIG("smokey01"),
    SMOKEY_FLAME_SMALL("smokey02"),
    SMOKEY_FLAME_NONE("smokey03"),
    SMOKEY_FLAME_BIG_HIT("smokeyHit01"),
    SMOKEY_FLAME_SMALL_HIT("smokeyHit02"),
    SMOKEY_FLAME_NONE_HIT("smokeyHit03"),

    // telly
    TELLY_FUZZ_ONE("telly01"),
    TELLY_FUZZ_TWO("telly02"),
    TELLY_FUZZ_ONE_HIT("tellyHit01"),
    TELLY_FUZZ_TWO_HIT("tellyHit02"),

    // cloud
    CLOUD("cloud"),
    CLOUD_HIT("cloudHit"),

    // helmet
    HELMET("helmet"),
    HELMET_HIT("helmetHit"),

    // skull alien
    SKULL("skull"),
    SKULL_HIT("skullHit"),

    // droid alien
    DROID("droid"),
    DROID_HIT("droidHit"),

    // bases
    BASE("base"),
    BASE_RIGHT("baseRight"),
    BASE_LEFT("baseLeft"),
    HELPER("baseHelper"),

    // pulsing shield when base stationary
    BASE_SHIELD_ONE("baseShieldOutline01"),
    BASE_SHIELD_TWO("baseShieldOutline02"),
    BASE_SHIELD_THREE("baseShieldOutline03"),
    BASE_SHIELD_FOUR("baseShieldOutline04"),

    // pulsing shield when base turning left
    BASE_LEFT_SHIELD_ONE("baseShieldLeftOutline01"),
    BASE_LEFT_SHIELD_TWO("baseShieldLeftOutline02"),
    BASE_LEFT_SHIELD_THREE("baseShieldLeftOutline03"),
    BASE_LEFT_SHIELD_FOUR("baseShieldLeftOutline04"),

    // pulsing shield when base turning right
    BASE_RIGHT_SHIELD_ONE("baseShieldRightOutline01"),
    BASE_RIGHT_SHIELD_TWO("baseShieldRightOutline02"),
    BASE_RIGHT_SHIELD_THREE("baseShieldRightOutline03"),
    BASE_RIGHT_SHIELD_FOUR("baseShieldRightOutline04"),

    // pulsing shield for helper base
    HELPER_SHIELD_ONE("baseHelperShieldOutline01"),
    HELPER_SHIELD_TWO("baseHelperShieldOutline02"),
    HELPER_SHIELD_THREE("baseHelperShieldOutline03"),
    HELPER_SHIELD_FOUR("baseHelperShieldOutline04"),

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

    // base missiles
    BASE_MISSILE("baseMissile"),
    BASE_MISSILE_LASER("baseMissileLaser"),
    BASE_MISSILE_ROCKET("baseMissileRocket"),
    BASE_MISSILE_BLAST("baseMissileBlast"),

    // alien laser
    ALIEN_LASER("alienLaser"),

    // fireball
    FIREBALL01("fireBall01"),
    FIREBALL02("fireBall02"),

    // lightning and rain missile
    LIGHTNING("lightningLong"),
    RAIN("rain"),

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
    PAUSE_BUTTON_UP("pauseButtonUp"),
    PAUSE_BUTTON_DOWN("pauseButtonDown"),

    // wave flags
    FLAG_1("Flag_1"),
    FLAG_5("Flag_5"),
    FLAG_10("Flag_10"),
    FLAG_50("Flag_50"),

    // menu buttons
    MENU_BUTTON_UP("blueButtonUp"),
    MENU_BUTTON_DOWN("blueButtonDown"),

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
