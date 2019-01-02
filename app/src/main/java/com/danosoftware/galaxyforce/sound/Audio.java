package com.danosoftware.galaxyforce.sound;

public interface Audio {
    Music newMusic(String filename);

    Sound newSound(SoundEffect soundEffect);
}
