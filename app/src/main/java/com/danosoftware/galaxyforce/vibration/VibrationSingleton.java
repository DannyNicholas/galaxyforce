package com.danosoftware.galaxyforce.vibration;

import android.content.Context;
import android.os.Vibrator;

import com.danosoftware.galaxyforce.options.OptionVibration;

public class VibrationSingleton implements Vibration
{
    // reference to device vibrator
    private final Vibrator vibrator;

    // does this device support vibration
    private boolean supportsVibration = false;

    // is vibration currently enabled
    private boolean vibrationEnabled = false;

    // singleton instance
    private static VibrationSingleton instance = null;

    // private constructor - can only instatiated by static initialise methods
    private VibrationSingleton(Context context)
    {
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        supportsVibration = vibrator.hasVibrator();
    }

    // must initialise singleton with context before it can be used
    public static void initialise(Context context)
    {
        if (instance == null)
        {
            instance = new VibrationSingleton(context);
        }
        else
        {
            throw new IllegalStateException("Vibration singleton has already been initialised.");
        }
    }

    // has vibrator singleton been initialised.
    public static boolean isInitialised()
    {
        // return true if initialised
        return (instance != null);
    }

    // get vibration singleton
    public static Vibration getInstance()
    {
        if (instance != null)
        {
            return instance;
        }
        else
        {
            throw new IllegalStateException("Vibration singleton has not been initialised.");
        }
    }

    /*
     * PUBLIC METHODS
     */

    @Override
    public void vibrate(VibrateTime vibrateTime)
    {
        if (vibrationEnabled)
        {
            // Vibrate for x milliseconds
            vibrator.vibrate(vibrateTime.getTimeInMilliseconds());
        }
    }

    @Override
    public void setVibrationEnabled(OptionVibration optionVibration)
    {
        if (supportsVibration)
        {
            switch (optionVibration)
            {
            case ON:
                vibrationEnabled = true;
                break;

            case OFF:
                vibrationEnabled = false;
                break;

            default:
                throw new IllegalArgumentException("Unrecognised OptionVibration found: '" + optionVibration + "'.");
            }
        }
        else
        {
            this.vibrationEnabled = false;
        }
    }
}
