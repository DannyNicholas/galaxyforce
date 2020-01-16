package com.danosoftware.galaxyforce.waves;

import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;

/**
 * Enum representing the different types of alien that can be created
 * (with their associated animations).
 * <p>
 * Used by AlienFactory to create instances of the wanted alien.
 */
public enum AlienCharacter {

    // special null character reserved for multi-explosions that spawn immediately as exploding aliens
    NULL(
            new ISpriteIdentifier[]{GameSpriteIdentifier.NULL},
            new ISpriteIdentifier[]{GameSpriteIdentifier.NULL},
            0f),

    // octopus alien with flapping legs
    OCTOPUS(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.OCTOPUS_LEFT,
                    GameSpriteIdentifier.OCTOPUS_RIGHT},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.OCTOPUS_LEFT_HIT,
                    GameSpriteIdentifier.OCTOPUS_RIGHT_HIT},
            0.5f),

    MINION(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.MINION},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.MINION_HIT},
            0f),

    // green spinning balls
    SPINNER_GREEN(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.SPINNER_GREEN_01,
                    GameSpriteIdentifier.SPINNER_GREEN_02,
                    GameSpriteIdentifier.SPINNER_GREEN_03},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.SPINNER_GREEN_01_HIT,
                    GameSpriteIdentifier.SPINNER_GREEN_02_HIT,
                    GameSpriteIdentifier.SPINNER_GREEN_03_HIT},
            0.2f),

    // green pulsating spinning balls
    SPINNER_PULSE_GREEN(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.SPINNER_PULSE_GREEN_01,
                    GameSpriteIdentifier.SPINNER_PULSE_GREEN_02,
                    GameSpriteIdentifier.SPINNER_PULSE_GREEN_03},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.SPINNER_PULSE_GREEN_01_HIT,
                    GameSpriteIdentifier.SPINNER_PULSE_GREEN_02_HIT,
                    GameSpriteIdentifier.SPINNER_PULSE_GREEN_03_HIT},
            0.2f),

    // molecule - made from joined spheres
    MOLECULE(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.MOLECULE},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.MOLECULE_HIT},
            0f),

    // big boss alien with claws
    BIG_BOSS(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.BIG_BOSS_01,
                    GameSpriteIdentifier.BIG_BOSS_02},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.BIG_BOSS_01_HIT,
                    GameSpriteIdentifier.BIG_BOSS_02_HIT},
            0.25f),

    // flapping lady bird
    LADY_BIRD(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.LADYBIRD_01,
                    GameSpriteIdentifier.LADYBIRD_02},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.LADYBIRD_01_HIT,
                    GameSpriteIdentifier.LADYBIRD_02_HIT},
            0.25f),


    // rocky asteroid
    ASTEROID(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.ASTEROID},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.ASTEROID_HIT},
            0f),

    // small rocky asteroid
    ASTEROID_MINI(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.ASTEROID_MINI},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.ASTEROID_MINI_HIT},
            0f),

    // dragon
    DRAGON_HEAD(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.DRAGON_HEAD_LEFT,
                    GameSpriteIdentifier.DRAGON_HEAD_RIGHT},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.DRAGON_HEAD_LEFT_HIT,
                    GameSpriteIdentifier.DRAGON_HEAD_RIGHT_HIT},
            0.5f),

    DRAGON_BODY(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.DRAGON_BODY},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.DRAGON_BODY_HIT},
            0.5f),

    // skull
    SKULL(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.SKULL},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.SKULL_HIT},
            0f),

    // droid
    DROID(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.DROID},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.DROID_HIT},
            0f),

    // big flapping mothership alien
    INSECT_MOTHERSHIP(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.MOTHER_BUZZER_WINGS_DOWN,
                    GameSpriteIdentifier.MOTHER_BUZZER_WINGS_UP},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.MOTHER_BUZZER_WINGS_DOWN_HIT,
                    GameSpriteIdentifier.MOTHER_BUZZER_WINGS_UP_HIT},
            0.5f),

    // flying insects
    INSECT(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.INSECT_WINGS_UP,
                    GameSpriteIdentifier.INSECT_WINGS_DOWN},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.INSECT_WINGS_UP,
                    GameSpriteIdentifier.INSECT_WINGS_DOWN},
            0.5f),

    // gobby alien
    GOBBY(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.GOBBY_LEFT,
                    GameSpriteIdentifier.GOBBY_RIGHT},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.GOBBY_LEFT_HIT,
                    GameSpriteIdentifier.GOBBY_RIGHT_HIT},
            0.5f),

    // flapping book
    BOOK(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.BOOK_FLAT,
                    GameSpriteIdentifier.BOOK_BEND,
                    GameSpriteIdentifier.BOOK_CLOSED,
                    GameSpriteIdentifier.BOOK_BEND},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.BOOK_FLAT_HIT,
                    GameSpriteIdentifier.BOOK_BEND_HIT,
                    GameSpriteIdentifier.BOOK_CLOSED_HIT,
                    GameSpriteIdentifier.BOOK_BEND_HIT},
            0.2f),

    // big blobby alien
    ZOGG(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.ZOGG_UP,
                    GameSpriteIdentifier.ZOGG_DOWN},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.ZOGG_UP_HIT,
                    GameSpriteIdentifier.ZOGG_DOWN_HIT},
            0.5f),

    // bomb
    BOMB(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.BOMB_01,
                    GameSpriteIdentifier.BOMB_02,
                    GameSpriteIdentifier.BOMB_03},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.BOMB_01_HIT,
                    GameSpriteIdentifier.BOMB_02_HIT,
                    GameSpriteIdentifier.BOMB_03_HIT},
            0.5f),

    // circuit board
    CIRCUIT(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.CIRCUIT_LEFT,
                    GameSpriteIdentifier.CIRCUIT_RIGHT},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.CIRCUIT_LEFT_HIT,
                    GameSpriteIdentifier.CIRCUIT_RIGHT_HIT},
            0.5f),

    // flying fire bat
    BATTY(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.BATTY_FLAP_DOWN,
                    GameSpriteIdentifier.BATTY_FLAP_UP},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.BATTY_FLAP_DOWN_HIT,
                    GameSpriteIdentifier.BATTY_FLAP_UP_HIT},
            0.2f),

    // circuit board
    CLOUD(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.CLOUD},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.CLOUD_HIT},
            0f,
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.CLOUD_EXPLODE_01,
                    GameSpriteIdentifier.CLOUD_EXPLODE_02,
                    GameSpriteIdentifier.CLOUD_EXPLODE_03,
                    GameSpriteIdentifier.CLOUD_EXPLODE_04,
                    GameSpriteIdentifier.CLOUD_EXPLODE_05},
            0.075f),

    // green bouncy alien
    BOUNCER(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.BOUNCER_IN,
                    GameSpriteIdentifier.BOUNCER_OUT},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.BOUNCER_IN_HIT,
                    GameSpriteIdentifier.BOUNCER_OUT_HIT},
            0.2f),

    // alien with droopy tendrils
    DROOPY(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.DROOPY_UP,
                    GameSpriteIdentifier.DROOPY_DOWN},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.DROOPY_UP_HIT,
                    GameSpriteIdentifier.DROOPY_DOWN_HIT},
            0.2f),

    // evil yellow cat
    BAD_CAT(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.BAD_CAT},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.BAD_CAT_HIT},
            0f),

    SMOKEY(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.SMOKEY_FLAME_NONE,
                    GameSpriteIdentifier.SMOKEY_FLAME_SMALL,
                    GameSpriteIdentifier.SMOKEY_FLAME_BIG,
                    GameSpriteIdentifier.SMOKEY_FLAME_SMALL},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.SMOKEY_FLAME_NONE_HIT,
                    GameSpriteIdentifier.SMOKEY_FLAME_SMALL_HIT,
                    GameSpriteIdentifier.SMOKEY_FLAME_BIG_HIT,
                    GameSpriteIdentifier.SMOKEY_FLAME_SMALL_HIT},
            0.2f),

    TELLY(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.TELLY_FUZZ_ONE,
                    GameSpriteIdentifier.TELLY_FUZZ_TWO},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.TELLY_FUZZ_ONE_HIT,
                    GameSpriteIdentifier.TELLY_FUZZ_TWO_HIT},
            0.3f),

    HELMET(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.HELMET},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.HELMET_HIT},
            0f),

    EGG(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.EGG_CRACK_01,
                    GameSpriteIdentifier.EGG_CRACK_02,
                    GameSpriteIdentifier.EGG_CRACK_03,
                    GameSpriteIdentifier.EGG_CRACK_02,
                    GameSpriteIdentifier.EGG_CRACK_03,
                    GameSpriteIdentifier.EGG_CRACK_02,
                    GameSpriteIdentifier.EGG_CRACK_03,
                    GameSpriteIdentifier.EGG_CRACK_02,
                    GameSpriteIdentifier.EGG_CRACK_03},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.EGG_CRACK_01_HIT,
                    GameSpriteIdentifier.EGG_CRACK_02_HIT,
                    GameSpriteIdentifier.EGG_CRACK_03_HIT,
                    GameSpriteIdentifier.EGG_CRACK_02_HIT,
                    GameSpriteIdentifier.EGG_CRACK_03_HIT,
                    GameSpriteIdentifier.EGG_CRACK_02_HIT,
                    GameSpriteIdentifier.EGG_CRACK_03_HIT,
                    GameSpriteIdentifier.EGG_CRACK_02_HIT,
                    GameSpriteIdentifier.EGG_CRACK_03_HIT},
            0.25f,
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.EGG_CRACK_04,
                    GameSpriteIdentifier.EGG_CRACK_05,
                    GameSpriteIdentifier.EGG_CRACK_06,
                    GameSpriteIdentifier.EGG_CRACK_07},
            0.075f);

    private static final float DEFAULT_EXPLOSION_FRAME_DURATION = 0.075f;

    // alien animation frames
    private final ISpriteIdentifier[] animationFrames;

    // alien animation frames when hit - to be in sync with normal animation frames
    private final ISpriteIdentifier[] hitAnimationFrames;

    // time between frame transitions
    private final float frameDuration;

    // alien animation frames when exploding
    private final ISpriteIdentifier[] explosionAnimationFrames;
    private final float explosionFrameDuration;

    AlienCharacter(
            final ISpriteIdentifier[] animationFrames,
            final ISpriteIdentifier[] hitAnimationFrames,
            final float frameDuration) {
        this(
                animationFrames,
                hitAnimationFrames,
                frameDuration,
                new ISpriteIdentifier[]{
                        GameSpriteIdentifier.EXPLODE_BIG_01,
                        GameSpriteIdentifier.EXPLODE_BIG_02,
                        GameSpriteIdentifier.EXPLODE_BIG_03,
                        GameSpriteIdentifier.EXPLODE_BIG_04,
                        GameSpriteIdentifier.EXPLODE_BIG_05},
                DEFAULT_EXPLOSION_FRAME_DURATION);
    }

    AlienCharacter(
            final ISpriteIdentifier[] animationFrames,
            final ISpriteIdentifier[] hitAnimationFrames,
            final float frameDuration,
            final ISpriteIdentifier[] explosionAnimationFrames,
            final float explosionFrameDuration) {
        this.animationFrames = animationFrames;
        this.hitAnimationFrames = hitAnimationFrames;
        this.frameDuration = frameDuration;
        this.explosionAnimationFrames = explosionAnimationFrames;
        this.explosionFrameDuration = explosionFrameDuration;
    }

    public Animation getAnimation() {
        return new Animation(frameDuration, animationFrames);
    }

    public Animation getHitAnimation() {
        return new Animation(frameDuration, hitAnimationFrames);
    }

    public Animation getExplosionAnimation() {
        return new Animation(explosionFrameDuration, explosionAnimationFrames);
    }
}
