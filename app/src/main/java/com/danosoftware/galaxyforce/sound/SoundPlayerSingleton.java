package com.danosoftware.galaxyforce.sound;

import com.danosoftware.galaxyforce.options.OptionSound;

public class SoundPlayerSingleton implements SoundPlayer {
    private boolean soundEnabled = false;

    // singleton instance
    private static SoundPlayerSingleton instance = null;

    // private constructor - can only instantiated by static methods
    private SoundPlayerSingleton() {
    }

    // get sound player singleton
    public static SoundPlayer getInstance() {
        if (instance == null) {
            instance = new SoundPlayerSingleton();
        }

        return instance;
    }

    /*
     * PUBLIC METHODS
     */

    @Override
    public void playSound(Sound sound) {
        if (soundEnabled) {
            sound.play(1);
        }
    }

    @Override
    public void setSoundEnabled(OptionSound optionSound) {
        switch (optionSound) {
            case ON:
                soundEnabled = true;
                break;

            case OFF:
                soundEnabled = false;
                break;

            default:
                throw new IllegalArgumentException("Unrecognised OptionSound found: '" + optionSound + "'.");
        }
    }
}
