package com.danosoftware.galaxyforce.sprites.game.behaviours.explode;

import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrateTime;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlienFollower;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.view.Animation;
import com.danosoftware.galaxyforce.waves.AlienCharacter;

/**
 * Implementation of explosion behaviour that triggers explosion after a set delay time. Alien will
 * appear frozen until that point.
 * <p>
 * Used for followers. When directly hit, they explode normally. When exploded as a follower, they
 * explode after a set delay so they are in-sync with the head followable alien.
 */
public class ExplodeFollowerDelayed implements ExplodeBehaviour {

  // explosion animation
  private final Animation animation;

  // reference to sound player
  private final SoundPlayerService sounds;

  // reference to vibrator
  private final VibrationService vibrator;

  private SpriteDetails spriteToExplode;
  private boolean startedExplosion;

  // delayed time when explosion should start
  private final float explosionStartTime;

  private float totalTimeElapsed;

  // time since explosion actually started
  private float timeSinceExplosionStarted;

  public ExplodeFollowerDelayed(
      final SoundPlayerService sounds,
      final VibrationService vibrator,
      final AlienCharacter character,
      final float explosionStartTime) {
    this.animation = character.getExplosionAnimation();
    this.sounds = sounds;
    this.vibrator = vibrator;
    this.explosionStartTime = explosionStartTime;
  }

  // this method is called if the follower is directly hit.
  // we want the explosion to start immediately.
  @Override
  public void startExplosion(IAlien alien) {
    startedExplosion = true;
    sounds.play(SoundEffect.EXPLOSION);
    vibrator.vibrate(VibrateTime.TINY);
  }

  // this method is called is the head is hit.
  // we want the explosion to be delayed to sync with the head.
  @Override
  public void startExplosionFollower(IAlienFollower alien) {
    totalTimeElapsed = 0f;
    timeSinceExplosionStarted = 0f;
    startedExplosion = false;
    spriteToExplode = alien.spriteDetails();
  }

  @Override
  public SpriteDetails getExplosion(float deltaTime) {
    totalTimeElapsed += deltaTime;

    // the explosion will only start once time has been exceeded
    if (!startedExplosion && totalTimeElapsed >= explosionStartTime) {
      startedExplosion = true;
    }

    if (startedExplosion) {
      timeSinceExplosionStarted += deltaTime;
      return animation.getKeyFrame(timeSinceExplosionStarted, Animation.ANIMATION_NONLOOPING);
    }

    // if we haven't started the explosion, show the original frozen alien sprite
    return spriteToExplode;
  }

  @Override
  public boolean finishedExploding() {
    return animation.isAnimationComplete();
  }
}
