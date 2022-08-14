package com.danosoftware.galaxyforce.services.vibration;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.VibrationEffect;

public enum VibrateTime {

  TINY(75),
  SHORT(125),
  MEDIUM(250),
  LONG(500);

  private final long timeInMilliseconds;
  private final VibrationEffect vibrationEffect;

  VibrateTime(long timeInMilliseconds) {
    this.timeInMilliseconds = timeInMilliseconds;
    if (VERSION.SDK_INT >= VERSION_CODES.O) {
      this.vibrationEffect = VibrationEffect.createOneShot(
          timeInMilliseconds,
          VibrationEffect.DEFAULT_AMPLITUDE);
    } else {
      this.vibrationEffect = null;
    }
  }

  public long getTimeInMilliseconds() {
    return timeInMilliseconds;
  }

  public VibrationEffect getVibrationEffect() {
    return vibrationEffect;
  }
}
