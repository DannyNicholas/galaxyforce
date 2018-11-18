package com.danosoftware.galaxyforce.vibration;

import com.danosoftware.galaxyforce.options.OptionVibration;

public interface Vibration {

    /**
     * Vibrate for specified time.
     * <p>
     * Will only vibrate if enabled and supported by device.
     *
     * @param vibrateTime
     */
    void vibrate(VibrateTime vibrateTime);

    /**
     * Set whether device should vibrate.
     * <p>
     * Will only have an impact if vibration is supported by device.
     *
     * @param vibrateEnabled
     */
    void setVibrationEnabled(OptionVibration optionVibration);

}