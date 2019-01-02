package com.danosoftware.galaxyforce.sound;

public interface Sound {

    /**
     * Play sound at specified volume
     */
    void play(float volume);

    /**
     * Dispose of sound when finished
     */
    void dispose();
}
