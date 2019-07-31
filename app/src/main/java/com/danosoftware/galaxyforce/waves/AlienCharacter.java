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

    // octopus alien with flapping legs
    OCTOPUS(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.OCTOPUS_LEFT,
                    GameSpriteIdentifier.OCTOPUS_RIGHT},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.OCTOPUS_LEFT_HIT,
                    GameSpriteIdentifier.OCTOPUS_RIGHT_HIT},
            0.5f),

    // yellow minion alien
    MINION(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.ALIEN_MINION_NORMAL,
                    GameSpriteIdentifier.ALIEN_MINION_FUZZ1,
                    GameSpriteIdentifier.ALIEN_MINION_FUZZ2},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.ALIEN_MINION_NORMAL,
                    GameSpriteIdentifier.ALIEN_MINION_FUZZ1,
                    GameSpriteIdentifier.ALIEN_MINION_FUZZ2},
            0.5f),

    // rocky asteroid
    ASTEROID(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.ASTEROID},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.ASTEROID_HIT},
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

    // stork
    STORK(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.STORK_1,
                    GameSpriteIdentifier.STORK_2},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.STORK_1,
                    GameSpriteIdentifier.STORK_2},
            0.4f),

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
                    GameSpriteIdentifier.ALIEN_GOBBY_LEFT,
                    GameSpriteIdentifier.ALIEN_GOBBY_RIGHT},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.ALIEN_GOBBY_LEFT,
                    GameSpriteIdentifier.ALIEN_GOBBY_RIGHT},
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

    // circuit board
    CLOUD(
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.CLOUD},
            new ISpriteIdentifier[]{
                    GameSpriteIdentifier.CLOUD_HIT},
            0f);


    // alien animation frames
    private final ISpriteIdentifier[] animationFrames;

    // alien animation frames when hit - to be in sync with normal animation frames
    private final ISpriteIdentifier[] hitAnimationFrames;

    // time between frame transitions
    private final float frameDuration;

    AlienCharacter(
            final ISpriteIdentifier[] animationFrames,
            final ISpriteIdentifier[] hitAnimationFrames,
            final float frameDuration) {
        this.animationFrames = animationFrames;
        this.hitAnimationFrames = hitAnimationFrames;
        this.frameDuration = frameDuration;
    }

    public Animation getAnimation() {
        return new Animation(frameDuration, animationFrames);
    }

    public Animation getHitAnimation() {
        return new Animation(frameDuration, hitAnimationFrames);
    }
}
