package com.danosoftware.galaxyforce.enumerations;

import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;

/**
 * Describes the animation and sound effects for different base missile characters.
 */
public enum BaseMissileCharacter {
    MISSILE(
            new Animation(
                    0f,
                    GameSpriteIdentifier.BASE_MISSILE),
            SoundEffect.BASE_FIRE
    ),
    BLAST(
            new Animation(
                    0f,
                    GameSpriteIdentifier.BASE_MISSILE_BLAST),
            SoundEffect.BASE_FIRE
    ),
    ROCKET(
            new Animation(
                    0f,
                    GameSpriteIdentifier.BASE_MISSILE_ROCKET),
            SoundEffect.BASE_FIRE
    ),
    LASER(
            new Animation(
                    0f,
                    GameSpriteIdentifier.BASE_MISSILE_LASER),
            SoundEffect.BASE_FIRE
    );

    private final Animation animation;
    private final SoundEffect sound;

    BaseMissileCharacter(Animation animation, SoundEffect sound) {
        this.animation = animation;
        this.sound = sound;
    }

    public Animation getAnimation() {
        return animation;
    }

    public SoundEffect getSound() {
        return sound;
    }
}
