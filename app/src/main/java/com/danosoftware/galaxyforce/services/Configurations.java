package com.danosoftware.galaxyforce.services;

import com.danosoftware.galaxyforce.options.OptionController;
import com.danosoftware.galaxyforce.options.OptionMusic;
import com.danosoftware.galaxyforce.options.OptionSound;
import com.danosoftware.galaxyforce.options.OptionVibration;
import com.danosoftware.galaxyforce.sound.Sound;
import com.danosoftware.galaxyforce.sound.SoundEffect;
import com.danosoftware.galaxyforce.sound.SoundEffectBank;
import com.danosoftware.galaxyforce.sound.SoundEffectBankSingleton;
import com.danosoftware.galaxyforce.sound.SoundPlayer;
import com.danosoftware.galaxyforce.sound.SoundPlayerSingleton;
import com.danosoftware.galaxyforce.vibration.VibrateTime;
import com.danosoftware.galaxyforce.vibration.Vibration;
import com.danosoftware.galaxyforce.vibration.VibrationSingleton;

import java.util.HashMap;
import java.util.Map;

public class Configurations {

    // keys for shared preference persistence
    private final static String CONTROLLER_KEY = "controller.option";
    private final static String SOUND_KEY = "sound.option";
    private final static String MUSIC_KEY = "music.option";
    private final static String VIBRATION_KEY = "vibration.option";

    // default values if configuration option not already persisted
    private final static String DEFAULT_CONTROLLER_OPTION = OptionController.DRAG.name();
    private final static String DEFAULT_SOUND_OPTION = OptionSound.ON.name();
    private final static String DEFAULT_MUSIC_OPTION = OptionMusic.ON.name();
    private final static String DEFAULT_VIBRATION_OPTION = OptionVibration.ON.name();

    // reference to shared preferences
    private final IPreferences<String> preferences;

    // reference to configuration
    private OptionController controllerType = null;
    private OptionSound soundOption = null;
    private OptionMusic musicOption = null;
    private OptionVibration vibrationOption = null;

    // singleton instance;
    private static Configurations instance = null;

    // private constructor
    private Configurations(IPreferences<String> preferences) {
        this.preferences = preferences;
    }

    // must initialise singleton with preferences before it can be used
    public static void initialise(IPreferences<String> preferences) {
        if (instance == null) {
            instance = new Configurations(preferences);
        } else {
            throw new IllegalStateException("Configurations singleton has already been initialised.");
        }
    }

    // have configurations been initialised with preferences.
    public static boolean isInitialised() {
        // return true if initialised
        return (instance != null);
    }

    // get configurations singleton
    public static Configurations getInstance() {
        if (instance != null) {
            return instance;
        } else {
            throw new IllegalStateException("Configurations singleton has not been initialised.");
        }
    }

    /*
     * CONTROLLER OPTION
     */

    // return the configurations
    public OptionController getControllerType() {
        if (controllerType == null) {
            // get controller type from shared preferences
            String controllerString = preferences.getPreference(CONTROLLER_KEY, DEFAULT_CONTROLLER_OPTION);

            // create an option controller enum from the string preference
            this.controllerType = OptionController.valueOf(controllerString);
        }

        return controllerType;
    }

    // set new controller type
    public void newControllerType(OptionController controllerType) {
        if (controllerType == null) {
            throw new IllegalArgumentException("Supplied ControllerType object can not be null.");
        }

        this.controllerType = controllerType;
    }

    /*
     * SOUND OPTION
     */

    // return the configurations
    public OptionSound getSoundOption() {
        if (soundOption == null) {
            // get string from shared preferences
            String soundString = preferences.getPreference(SOUND_KEY, DEFAULT_SOUND_OPTION);

            // create an enum from the string preference
            this.soundOption = OptionSound.valueOf(soundString);
        }

        return soundOption;
    }

    // set sound option
    public void setSoundOption(OptionSound soundOption) {
        if (soundOption == null) {
            throw new IllegalArgumentException("Supplied OptionSound object can not be null.");
        }

        this.soundOption = soundOption;

        /*
         * modify sound
         */
        SoundPlayer soundPlayer = SoundPlayerSingleton.getInstance();
        soundPlayer.setSoundEnabled(soundOption);

        // play sound (if enabled) to prove sound is on
        SoundEffectBank soundBank = SoundEffectBankSingleton.getInstance();
        Sound sound = soundBank.get(SoundEffect.ALIEN_FIRE);
        soundPlayer.playSound(sound);
    }

    /*
     * MUSIC OPTION
     */

    // return the configurations
    public OptionMusic getMusicOption() {
        if (musicOption == null) {
            // get string from shared preferences
            String musicString = preferences.getPreference(MUSIC_KEY, DEFAULT_MUSIC_OPTION);

            // create an enum from the string preference
            this.musicOption = OptionMusic.valueOf(musicString);
        }

        return musicOption;
    }

    // set music option
    public void setMusicOption(OptionMusic musicOption) {
        if (musicOption == null) {
            throw new IllegalArgumentException("Supplied OptionMusic object can not be null.");
        }

        this.musicOption = musicOption;
    }

    /*
     * VIBRATION OPTION
     */

    // return the configurations
    public OptionVibration getVibrationOption() {
        if (vibrationOption == null) {
            // get string from shared preferences
            String vibrationString = preferences.getPreference(VIBRATION_KEY, DEFAULT_VIBRATION_OPTION);

            // create an enum from the string preference
            this.vibrationOption = OptionVibration.valueOf(vibrationString);
        }

        return vibrationOption;
    }

    // set vibration option
    public void setVibrationOption(OptionVibration vibrationOption) {
        if (vibrationOption == null) {
            throw new IllegalArgumentException("Supplied OptionVibration object can not be null.");
        }

        this.vibrationOption = vibrationOption;

        /*
         * modify vibrator
         */
        Vibration vibrator = VibrationSingleton.getInstance();
        vibrator.setVibrationEnabled(vibrationOption);

        // vibrate (if enabled) to prove vibrator is on
        vibrator.vibrate(VibrateTime.MEDIUM);
    }

    public void persistConfigurations() {
        Map<String, String> keyValueMap = new HashMap<String, String>();
        keyValueMap.put(CONTROLLER_KEY, controllerType.name());
        keyValueMap.put(SOUND_KEY, soundOption.name());
        keyValueMap.put(MUSIC_KEY, musicOption.name());
        keyValueMap.put(VIBRATION_KEY, vibrationOption.name());

        // store configurations to shared preferences to persist in future
        preferences.storePreference(keyValueMap);
    }
}
