package com.danosoftware.galaxyforce.enumerations;

import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;

/**
 * Describes the animation and sound effects for different alien missile characters.
 */
public enum AlienMissileCharacter {

    LASER(
            new Animation(
                    0f,
                    GameSpriteIdentifier.ALIEN_LASER),
            SoundEffect.ALIEN_FIRE
    ),
    FIREBALL(
            new Animation(
                    0.1f,
                    GameSpriteIdentifier.FIREBALL01,
                    GameSpriteIdentifier.FIREBALL02),
            SoundEffect.ALIEN_FIRE
    ),
    LIGHTNING(
            new Animation(
                    0.4f,
                    GameSpriteIdentifier.LIGHTNING_01,
                    GameSpriteIdentifier.LIGHTNING_02),
            SoundEffect.ALIEN_FIRE
    ),
    RAIN(
            new Animation(
                    0.2f,
                    GameSpriteIdentifier.RAIN_01,
                    GameSpriteIdentifier.RAIN_02),
            SoundEffect.ALIEN_FIRE
    );

    private final Animation animation;
    private final SoundEffect sound;

    AlienMissileCharacter(Animation animation, SoundEffect sound) {
        this.animation = animation;
        this.sound = sound;
    }

    public Animation getAnimation() {
        return animation;
    }

    public SoundEffect getSound() {
        return sound;
    }}


