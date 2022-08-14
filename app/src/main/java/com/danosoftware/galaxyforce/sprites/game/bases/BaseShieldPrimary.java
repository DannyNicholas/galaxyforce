package com.danosoftware.galaxyforce.sprites.game.bases;

import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrateTime;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.common.AbstractMovingSprite;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.view.Animation;

public class BaseShieldPrimary extends AbstractMovingSprite implements IBaseShield {

  // shield animation that pulses every second
  private static final Animation SHIELD_PULSE = new Animation(
      0.25f,
      SpriteDetails.BASE_SHIELD_ONE,
      SpriteDetails.BASE_SHIELD_TWO,
      SpriteDetails.BASE_SHIELD_THREE,
      SpriteDetails.BASE_SHIELD_FOUR);
  private static final Animation LEFT_LEAN_SHIELD_PULSE = new Animation(
      0.25f,
      SpriteDetails.BASE_LEFT_SHIELD_ONE,
      SpriteDetails.BASE_LEFT_SHIELD_TWO,
      SpriteDetails.BASE_LEFT_SHIELD_THREE,
      SpriteDetails.BASE_LEFT_SHIELD_FOUR);
  private static final Animation RIGHT_LEAN_SHIELD_PULSE = new Animation(
      0.25f,
      SpriteDetails.BASE_RIGHT_SHIELD_ONE,
      SpriteDetails.BASE_RIGHT_SHIELD_TWO,
      SpriteDetails.BASE_RIGHT_SHIELD_THREE,
      SpriteDetails.BASE_RIGHT_SHIELD_FOUR);

  // how often shield pulse effects should repeat
  private static final float SHIELD_PULSE_FREQUENCY_IN_SECONDS = 2f;
  private float timeSinceLastPulse = 0f;
  private boolean effectsPlaying = false;

  private final IBasePrimary base;

  // state time used to help select the current animation frame
  private float stateTime;

  // reference to sound player
  private final SoundPlayerService sounds;

  // reference to vibrator
  private final VibrationService vibrator;

  public BaseShieldPrimary(
      IBasePrimary base,
      final SoundPlayerService sounds,
      final VibrationService vibrator,
      float xStart,
      float yStart,
      float syncTime) {

    super(
        getShieldAnimation(base)
            .getKeyFrame(syncTime, Animation.ANIMATION_LOOPING),
        xStart,
        yStart);
    this.base = base;
    this.stateTime = syncTime;
    this.sounds = sounds;
    this.vibrator = vibrator;
  }

  @Override
  public void animate(float deltaTime) {
    // increase state time by delta
    stateTime += deltaTime;

    // set base sprite using animation loop and time through animation
    changeType(
        getShieldAnimation(base)
            .getKeyFrame(stateTime, Animation.ANIMATION_LOOPING));

    // play shield pulse effects if not started playing or
    // repeat after set number of seconds.
    timeSinceLastPulse += deltaTime;
    if (!effectsPlaying || timeSinceLastPulse > SHIELD_PULSE_FREQUENCY_IN_SECONDS) {
      sounds.play(SoundEffect.SHIELD_PULSE);
      vibrator.vibrate(VibrateTime.LONG);
      effectsPlaying = true;
      timeSinceLastPulse %= SHIELD_PULSE_FREQUENCY_IN_SECONDS;
    }
  }

  /**
   * Shields can be added at different times. A base with a shield may gain some shielded helpers.
   * In order to keep all shields animating in sync, it should be possible to share the state time
   * used for synchronisation.
   * <p>
   * Normally the helper bases will be synchronised to the primary base.
   *
   * @return synchronisation timing
   */
  @Override
  public float getSynchronisation() {
    return stateTime;
  }

  /**
   * Return shield animation for current base lean
   */
  private static Animation getShieldAnimation(IBasePrimary base) {
    switch (base.getLean()) {
      case LEFT:
        return LEFT_LEAN_SHIELD_PULSE;
      case RIGHT:
        return RIGHT_LEAN_SHIELD_PULSE;
      case NONE:
        return SHIELD_PULSE;
    }
    throw new GalaxyForceException("Unknown Base Lean: " + base.getLean());
  }
}
