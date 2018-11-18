package com.danosoftware.galaxyforce.interfaces;

import com.danosoftware.galaxyforce.sound.Sound;
import com.danosoftware.galaxyforce.sound.SoundEffect;

public interface Audio {
    Music newMusic(String filename);

    Sound newSound(SoundEffect soundEffect);
}
