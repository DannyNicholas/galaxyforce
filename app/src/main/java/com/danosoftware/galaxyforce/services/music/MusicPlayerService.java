package com.danosoftware.galaxyforce.services.music;

public interface MusicPlayerService {

    /**
     * Load new music.
     * play() will need to be called to start playing music.
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
     * Stop current music.
     */
    void stop();

    /**
     * Enable or disable music using supplied parameter.
     */
    void setMusicEnabled(boolean enableMusic);

    /**
     * Dispose of sounds when finished
     */
    void dispose();
}
