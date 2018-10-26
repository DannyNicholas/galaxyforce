package com.danosoftware.galaxyforce.sound;


public interface SoundEffectBank
{
    /**
     * Return the sound effect matching the supplied parameter.
     * 
     * @param soundEffect
     */
    Sound get(SoundEffect soundEffect);
}