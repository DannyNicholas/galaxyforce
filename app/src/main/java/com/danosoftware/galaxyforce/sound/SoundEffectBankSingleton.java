package com.danosoftware.galaxyforce.sound;

import com.danosoftware.galaxyforce.interfaces.Audio;

import java.util.HashMap;
import java.util.Map;

public class SoundEffectBankSingleton implements SoundEffectBank {

    // map of effect enums to actual sound effects
    Map<SoundEffect, Sound> effectsBank;

    // reference to game audio
    private Audio audio;

    // singleton instance
    private static SoundEffectBankSingleton instance = null;

    // private constructor - can only instatiated by static initialise methods
    private SoundEffectBankSingleton(Audio audio) {
        this.effectsBank = new HashMap<SoundEffect, Sound>();
        this.audio = audio;

        /* load sound effects - loading can take time */
        for (SoundEffect aSoundEffect : SoundEffect.values()) {
            loadEffect(aSoundEffect);
        }
    }

    // must initialise singleton with audio before it can be used
    public static void initialise(Audio audio) {
        if (instance == null) {
            instance = new SoundEffectBankSingleton(audio);
        } else {
            throw new IllegalStateException("SoundEffectBank singleton has already been initialised.");
        }
    }

    // has singleton been initialised.
    public static boolean isInitialised() {
        // return true if initialised
        return (instance != null);
    }

    // get singleton
    public static SoundEffectBank getInstance() {
        if (instance != null) {
            return instance;
        } else {
            throw new IllegalStateException("SoundEffectBank singleton has not been initialised.");
        }
    }

    /*
     * PUBLIC METHODS
     */

    @Override
    public Sound get(SoundEffect soundEffect) {
        // return sound
        return effectsBank.get(soundEffect);
    }

    /*
     * PRIVATE METHODS
     */

    private void loadEffect(SoundEffect soundEffect) {
        this.effectsBank.put(soundEffect, audio.newSound(soundEffect));
    }
}
