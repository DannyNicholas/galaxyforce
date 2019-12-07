package com.danosoftware.galaxyforce.sprites.properties;

import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.textures.Texture;

public enum GameSpriteIdentifier implements ISpriteIdentifier {

    // octopus
    OCTOPUS_LEFT("octopusLeft",4),
    OCTOPUS_RIGHT("octopusRight", 4),
    OCTOPUS_LEFT_HIT("octopusLeftHit", 4),
    OCTOPUS_RIGHT_HIT("octopusRightHit", 4),

    // yellow minion
    MINION("minion", 2),
    MINION_HIT("minionHit", 2),

    // green ball spinner
    SPINNER_GREEN_01("spinnerGreen01", 4),
    SPINNER_GREEN_02("spinnerGreen02", 4),
    SPINNER_GREEN_03("spinnerGreen03", 4),
    SPINNER_GREEN_01_HIT("spinnerGreenHit01", 4),
    SPINNER_GREEN_02_HIT("spinnerGreenHit02", 4),
    SPINNER_GREEN_03_HIT("spinnerGreenHit03", 4),

    // pulsating green ball spinner
    SPINNER_PULSE_GREEN_01("spinnerPulseGreen01", 4),
    SPINNER_PULSE_GREEN_02("spinnerPulseGreen02", 4),
    SPINNER_PULSE_GREEN_03("spinnerPulseGreen03", 4),
    SPINNER_PULSE_GREEN_01_HIT("spinnerPulseGreenHit01", 4),
    SPINNER_PULSE_GREEN_02_HIT("spinnerPulseGreenHit02", 4),
    SPINNER_PULSE_GREEN_03_HIT("spinnerPulseGreenHit03", 4),

    // molecule
    MOLECULE("molecule", 2),
    MOLECULE_HIT("moleculeHit", 2),

    // big boss
    BIG_BOSS_01("bigBoss01", 6),
    BIG_BOSS_02("bigBoss02", 6),
    BIG_BOSS_01_HIT("bigBossHit01", 6),
    BIG_BOSS_02_HIT("bigBossHit02", 6),

    // flapping lady bird
    LADYBIRD_01("ladyBird01", 6),
    LADYBIRD_02("ladyBird02", 6),
    LADYBIRD_01_HIT("ladyBirdHit01", 6),
    LADYBIRD_02_HIT("ladyBirdHit02", 6),

    // zogg
    ZOGG_UP("zogg01", 4),
    ZOGG_DOWN("zogg02", 4),
    ZOGG_UP_HIT("zoggHit01",4),
    ZOGG_DOWN_HIT("zoggHit02", 4),

    // asteroid
    ASTEROID("asteroid", 4),
    ASTEROID_HIT("asteroidHit", 4),

    // mini-asteroid
    ASTEROID_MINI("miniAsteroid", 3),
    ASTEROID_MINI_HIT("miniAsteroidHit", 3),

    // egg
    EGG_CRACK_01("eggCrack01", 3),
    EGG_CRACK_02("eggCrack02", 3),
    EGG_CRACK_03("eggCrack03", 3),
    EGG_CRACK_01_HIT("eggCrackHit01", 3),
    EGG_CRACK_02_HIT("eggCrackHit02", 3),
    EGG_CRACK_03_HIT("eggCrackHit03", 3),

    // egg explosion
    EGG_CRACK_04("eggCrack04"),
    EGG_CRACK_05("eggCrack05"),
    EGG_CRACK_06("eggCrack06"),
    EGG_CRACK_07("eggCrack07"),

    // dragon - head and body
    DRAGON_HEAD_LEFT("dragonLeft", 5),
    DRAGON_HEAD_RIGHT("dragonRight", 5),
    DRAGON_HEAD_LEFT_HIT("dragonHitLeft", 5),
    DRAGON_HEAD_RIGHT_HIT("dragonHitRight", 5),
    DRAGON_BODY("dragonBody", 5),
    DRAGON_BODY_HIT("dragonBodyHit",5),

    // buzzer - small flying insect
    INSECT_WINGS_UP("buzzerWingsUp", 3),
    INSECT_WINGS_DOWN("buzzerWingsDown", 3),
    INSECT_WINGS_UP_HIT("buzzerWingsUpHit", 3),
    INSECT_WINGS_DOWN_HIT("buzzerWingsDownHit", 3),

    // mother buzzer - big flying insect
    MOTHER_BUZZER_WINGS_DOWN("motherBuzzerWingsDown", 4),
    MOTHER_BUZZER_WINGS_UP("motherBuzzerWingsUp", 4),
    MOTHER_BUZZER_WINGS_DOWN_HIT("motherBuzzerWingsDownHit", 4),
    MOTHER_BUZZER_WINGS_UP_HIT("motherBuzzerWingsUpHit", 4),

    // gobby
    GOBBY_LEFT("gobby01", 2),
    GOBBY_RIGHT("gobby02", 2),
    GOBBY_LEFT_HIT("gobbyHit01", 2),
    GOBBY_RIGHT_HIT("gobbyHit02", 2),

    // bad cat
    BAD_CAT("badCat", 2),
    BAD_CAT_HIT("badCatHit", 2),

    // bomb
    BOMB_01("bomb01", 3),
    BOMB_02("bomb02", 3),
    BOMB_03("bomb03", 3),
    BOMB_01_HIT("bombHit01", 3),
    BOMB_02_HIT("bombHit02", 3),
    BOMB_03_HIT("bombHit03", 3),

    // batty
    BATTY_FLAP_DOWN("bat01", 3),
    BATTY_FLAP_UP("bat02", 3),
    BATTY_FLAP_DOWN_HIT("bat01Hit", 3),
    BATTY_FLAP_UP_HIT("bat02Hit", 3),

    // book
    BOOK_FLAT("book01", 3),
    BOOK_BEND("book02", 3),
    BOOK_CLOSED("book03", 3),
    BOOK_FLAT_HIT("bookHit01", 3),
    BOOK_BEND_HIT("bookHit02", 3),
    BOOK_CLOSED_HIT("bookHit03", 3),

    // bouncer
    BOUNCER_IN("bouncer01", 4),
    BOUNCER_OUT("bouncer02", 4),
    BOUNCER_IN_HIT("bouncer01Hit", 4),
    BOUNCER_OUT_HIT("bouncer02Hit", 4),

    // droopy
    DROOPY_DOWN("droopyDown", 4),
    DROOPY_UP("droopyUp", 4),
    DROOPY_DOWN_HIT("droopyDownHit",4),
    DROOPY_UP_HIT("droopyUpHit", 4),

    // circuit
    CIRCUIT_LEFT("circuit01", 2),
    CIRCUIT_RIGHT("circuit02", 2),
    CIRCUIT_LEFT_HIT("circuit01Hit", 2),
    CIRCUIT_RIGHT_HIT("circuit02Hit", 2),

    // smokey
    SMOKEY_FLAME_BIG("smokey01", 4),
    SMOKEY_FLAME_SMALL("smokey02", 4),
    SMOKEY_FLAME_NONE("smokey03", 4),
    SMOKEY_FLAME_BIG_HIT("smokeyHit01", 4),
    SMOKEY_FLAME_SMALL_HIT("smokeyHit02", 4),
    SMOKEY_FLAME_NONE_HIT("smokeyHit03",4),

    // telly
    TELLY_FUZZ_ONE("telly01", 4),
    TELLY_FUZZ_TWO("telly02", 4),
    TELLY_FUZZ_ONE_HIT("tellyHit01", 4),
    TELLY_FUZZ_TWO_HIT("tellyHit02", 4),

    // cloud
    CLOUD("cloud", 5),
    CLOUD_HIT("cloudHit", 5),

    // helmet
    HELMET("helmet", 5),
    HELMET_HIT("helmetHit", 5),

    // skull alien
    SKULL("skull", 3),
    SKULL_HIT("skullHit", 3),

    // droid alien
    DROID("droid", 3),
    DROID_HIT("droidHit", 3),

    // bases
    BASE("base", 4),
    BASE_RIGHT("baseRight", 3),
    BASE_LEFT("baseLeft", 3),
    HELPER("baseHelper", 3),

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
    FONT_MAP("GalaxyForceFont_30x38-crop"),

    // null - transparent 1 x 1 sprite (essentially an invisible sprite)
    NULL("null");


    private final String name;
    private ISpriteProperties properties;

    // for less sensitive collision detection we can optionally
    // reduce the sprite bounds.
    // this value will reduce bounds by this value all-around
    // e.g. a boundsReduction value of 2 will give a 64x64 pixel
    // a bounding box of 60x60 (2 pixels less top, bottom, left, right)
    private final int boundsReduction;

    GameSpriteIdentifier(String name) {
        this(name, 0);
    }

    GameSpriteIdentifier(String name, int boundsReduction) {
        this.name = name;
        this.boundsReduction = boundsReduction;
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
        if (properties.getWidth() <= boundsReduction * 2) {
            throw new GalaxyForceException(
                    String.format(
                            "Sprite: %s has width of %d but bounds reduction of %d.",
                            name,
                            properties.getWidth(),
                            boundsReduction));
        }
        if (properties.getHeight() <= boundsReduction * 2) {
            throw new GalaxyForceException(
                    String.format(
                            "Sprite: %s has height of %d but bounds reduction of %d.",
                            name,
                            properties.getHeight(),
                            boundsReduction));
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int boundsReduction() {
        return boundsReduction;
    }
}
