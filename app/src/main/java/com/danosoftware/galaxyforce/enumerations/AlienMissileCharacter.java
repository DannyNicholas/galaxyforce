package com.danosoftware.galaxyforce.enumerations;

import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;

/**
 * Describes the animation and sound effects for different alien missile characters.
 */
public enum AlienMissileCharacter {

    LASER(
            new Animation(0f, GameSpriteIdentifier.LASER_ALIEN),
            SoundEffect.ALIEN_FIRE
    ),
    FIREBALL(
            new Animation(0f, GameSpriteIdentifier.LASER_ALIEN),
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


