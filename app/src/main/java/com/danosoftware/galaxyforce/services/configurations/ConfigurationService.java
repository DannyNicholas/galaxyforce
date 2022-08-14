package com.danosoftware.galaxyforce.services.configurations;

import com.danosoftware.galaxyforce.options.OptionMusic;
import com.danosoftware.galaxyforce.options.OptionSound;
import com.danosoftware.galaxyforce.options.OptionVibration;

public interface ConfigurationService {

  OptionSound getSoundOption();

  void setSoundOption(OptionSound soundOption);

  OptionMusic getMusicOption();

  void setMusicOption(OptionMusic musicOption);

  OptionVibration getVibrationOption();

  void setVibrationOption(OptionVibration vibrationOption);

  void persistConfigurations();
}
