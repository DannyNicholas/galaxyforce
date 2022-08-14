package com.danosoftware.galaxyforce.services.vibration;

public interface VibrationService {

  /**
   * Vibrate for specified time.
   * <p>
   * Will only vibrate if enabled and supported by device.
   */
  void vibrate(VibrateTime vibrateTime);

  /**
   * Set whether device should vibrate.
   * <p>
   * Will only have an impact if vibration is supported by device.
   */
  void setVibrationEnabled(boolean enableVibration);

  /**
   * Stop vibration. Normally called prior to pausing game.
   */
  void stop();
}
