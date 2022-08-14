package com.danosoftware.galaxyforce.sprites.game.behaviours.hit;

import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;

public class HitBehaviourFactory {

  private final SoundPlayerService sounds;
  private final VibrationService vibrator;

  public HitBehaviourFactory(
      final SoundPlayerService sounds,
      final VibrationService vibrator) {
    this.sounds = sounds;
    this.vibrator = vibrator;
  }

  public HitBehaviour createHitBehaviour() {
    return new HitAnimation(
        sounds,
        vibrator);
  }
}
