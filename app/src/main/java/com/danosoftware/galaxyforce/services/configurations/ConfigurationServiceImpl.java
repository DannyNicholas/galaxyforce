package com.danosoftware.galaxyforce.services.configurations;

import com.danosoftware.galaxyforce.options.OptionMusic;
import com.danosoftware.galaxyforce.options.OptionSound;
import com.danosoftware.galaxyforce.options.OptionVibration;
import com.danosoftware.galaxyforce.services.preferences.IPreferences;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationServiceImpl implements ConfigurationService {


  // keys for shared preference persistence
  private final static String SOUND_KEY = "sound.option";
  private final static String MUSIC_KEY = "music.option";
  private final static String VIBRATION_KEY = "vibration.option";

  // default values if configuration option not already persisted
  private final static String DEFAULT_SOUND_OPTION = OptionSound.ON.name();
  private final static String DEFAULT_MUSIC_OPTION = OptionMusic.ON.name();
  private final static String DEFAULT_VIBRATION_OPTION = OptionVibration.ON.name();

  // shared preferences service
  private final IPreferences<String> preferences;

  // chosen configurations
  private OptionSound soundOption;
  private OptionMusic musicOption;
  private OptionVibration vibrationOption;

  // private constructor
  public ConfigurationServiceImpl(IPreferences<String> preferences) {
    this.preferences = preferences;

    // retrieve chosen sound option
    String soundString = preferences.getPreference(SOUND_KEY, DEFAULT_SOUND_OPTION);
    this.soundOption = OptionSound.valueOf(soundString);

    // retrieve chosen music option
    String musicString = preferences.getPreference(MUSIC_KEY, DEFAULT_MUSIC_OPTION);
    this.musicOption = OptionMusic.valueOf(musicString);

    // retrieve chosen vibration option
    String vibrationString = preferences.getPreference(VIBRATION_KEY, DEFAULT_VIBRATION_OPTION);
    this.vibrationOption = OptionVibration.valueOf(vibrationString);
  }

  /*
   * SOUND OPTION
   */

  @Override
  public OptionSound getSoundOption() {
    return soundOption;
  }

  @Override
  public void setSoundOption(OptionSound soundOption) {
    this.soundOption = soundOption;
  }

  /*
   * MUSIC OPTION
   */

  @Override
  public OptionMusic getMusicOption() {
    return musicOption;
  }

  @Override
  public void setMusicOption(OptionMusic musicOption) {
    this.musicOption = musicOption;
  }

  /*
   * VIBRATION OPTION
   */

  @Override
  public OptionVibration getVibrationOption() {
    return vibrationOption;
  }

  @Override
  public void setVibrationOption(OptionVibration vibrationOption) {
    this.vibrationOption = vibrationOption;
  }

  /*
   * PERSIST CONFIGURATION
   */

  @Override
  public void persistConfigurations() {
    Map<String, String> keyValueMap = new HashMap<>();
    keyValueMap.put(SOUND_KEY, soundOption.name());
    keyValueMap.put(MUSIC_KEY, musicOption.name());
    keyValueMap.put(VIBRATION_KEY, vibrationOption.name());

    // store configurations to shared preferences to persist in future
    preferences.storePreference(keyValueMap);
  }
}
