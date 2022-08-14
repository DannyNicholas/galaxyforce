package com.danosoftware.galaxyforce.services.vibration;

import static com.danosoftware.galaxyforce.services.vibration.VibrationHelper.getModernVibrator;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Vibrator;

public class VibrationServiceImpl implements VibrationService {

  // reference to device vibrator
  private final Vibrator vibrator;

  // does this device support vibration
  private final boolean supportsVibration;

  // is vibration currently enabled
  private boolean vibrationEnabled;

  public VibrationServiceImpl(Context context, boolean enableVibration) {
    this.vibrator = getVibrator(context);
    this.supportsVibration = vibrator != null && vibrator.hasVibrator();
    this.vibrationEnabled = enableVibration;
  }

  @SuppressWarnings("deprecation")
  private Vibrator getVibrator(Context context) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
      return getModernVibrator(context);
    } else {
      return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void vibrate(VibrateTime vibrateTime) {
    if (supportsVibration && vibrationEnabled) {
      if (VERSION.SDK_INT >= VERSION_CODES.O && vibrateTime.getVibrationEffect() != null) {
        vibrator.vibrate(vibrateTime.getVibrationEffect());
      } else {
        vibrator.vibrate(vibrateTime.getTimeInMilliseconds());
      }
    }
  }

  @Override
  public void setVibrationEnabled(boolean enableVibration) {
    this.vibrationEnabled = enableVibration;
  }

  @Override
  public void stop() {
    if (supportsVibration) {
      vibrator.cancel();
    }
  }
}
