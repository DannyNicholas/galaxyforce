package com.danosoftware.galaxyforce.services.music;

public interface MusicPlayerService {

  /**
   * Load new music.
   * <p>
   * Will automatically play when ready.
   */
  void load(Music music);

  /**
   * Play current music.
   * <p>
   * Music will only play if music player is enabled.
   */
  void play();

  /**
   * Pause current music.
   */
  void pause();

  /**
   * Enable or disable music using supplied parameter.
   */
  void setMusicEnabled(boolean enableMusic);

  /**
   * Dispose of sounds when finished
   */
  void dispose();
}
