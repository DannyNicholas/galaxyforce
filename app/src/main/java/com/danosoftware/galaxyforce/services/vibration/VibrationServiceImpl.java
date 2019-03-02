package com.danosoftware.galaxyforce.services.vibration;

import android.content.Context;
import android.os.Vibrator;

public class VibrationServiceImpl implements VibrationService {

    // reference to device vibrator
    private final Vibrator vibrator;

    // does this device support vibration
    private final boolean supportsVibration;

    // is vibration currently enabled
    private boolean vibrationEnabled;

    public VibrationServiceImpl(Context context, boolean enableVibration) {
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        this.supportsVibration = vibrator != null && vibrator.hasVibrator();
        this.vibrationEnabled = enableVibration;
    }

    @Override
    public void vibrate(VibrateTime vibrateTime) {
        if (supportsVibration && vibrationEnabled) {
            vibrator.vibrate(vibrateTime.getTimeInMilliseconds());
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
