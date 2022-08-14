package com.danosoftware.galaxyforce.enumerations;

import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.view.Animation;

/**
 * Describes the animation and sound effects for different alien missile characters.
 */
public enum AlienMissileCharacter {

  LASER(
      new Animation(
          0f,
          SpriteDetails.ALIEN_LASER),
      SoundEffect.ALIEN_FIRE
  ),
  FIREBALL(
      new Animation(
          0.1f,
          SpriteDetails.FIREBALL01,
          SpriteDetails.FIREBALL02),
      SoundEffect.ALIEN_FIRE
  ),
  LIGHTNING(
      new Animation(
          0.4f,
          SpriteDetails.LIGHTNING_01,
          SpriteDetails.LIGHTNING_02),
      SoundEffect.ALIEN_FIRE
  ),
  RAIN(
      new Animation(
          0.2f,
          SpriteDetails.RAIN_01,
          SpriteDetails.RAIN_02),
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
  }
}


