package com.danosoftware.galaxyforce.sound;

public interface Sound
{

    /**
     * Play sound at specified volume
     * 
     * @param volume
     */
    void play(float volume);

    /**
     * Dipose of sound when finished
     */
    void dispose();
}
