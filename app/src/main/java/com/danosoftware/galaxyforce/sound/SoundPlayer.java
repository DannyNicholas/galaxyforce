package com.danosoftware.galaxyforce.sound;

import com.danosoftware.galaxyforce.options.OptionSound;

public interface SoundPlayer
{

    /**
     * Play supplied sound at default volume.
     * 
     * Sound will only play if sound player is enabled.
     * 
     * @param sound
     */
    void playSound(Sound sound);

    /**
     * Set whether sound should be played from supplied parameter.
     * 
     * @param optionSound
     */
    void setSoundEnabled(OptionSound optionSound);

}