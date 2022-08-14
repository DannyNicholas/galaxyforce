package com.danosoftware.galaxyforce.services.sound;

public interface SoundPlayerService {

  /**
   * Play supplied sound effect.
   * <p>
   * Sound will only play if sound player is enabled.
   */
  void play(SoundEffect effect);

  /**
   * Pause all currently playing sounds.
   */
  void pause();

  /**
   * Resume any sounds that were previously paused.
   */
  void resume();

  /**
   * Enable or disable sound using supplied parameter.
   */
  void setSoundEnabled(boolean enableSound);

  /**
   * Dispose of sounds when finished
   */
  void dispose();
}