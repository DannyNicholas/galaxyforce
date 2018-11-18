package com.danosoftware.galaxyforce.input;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.danosoftware.galaxyforce.services.WindowManagers;

public class AccelerometerHandler implements SensorEventListener {
    float accelX;
    float accelY;
    float accelZ;
    int screenRotation;

    static final int ACCELEROMETER_AXIS_SWAP[][] =
            {
                    {1, -1, 0, 1}, // ROTATION_0
                    {-1, -1, 1, 0}, // ROTATION_90
                    {-1, 1, 0, 1}, // ROTATION_180
                    {1, 1, 1, 0} // ROTATION_270
            };

    public AccelerometerHandler(Context context) {
        SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
            Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }

        // use Window Manager service to get window manager and get default
        // device orientation
        screenRotation = WindowManagers.getWindowMgr().getDefaultDisplay().getRotation();

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing to do here
    }

    public void onSensorChanged(SensorEvent event) {
        // accelX = event.values[0];
        // accelY = event.values[1];
        // accelZ = event.values[2];

        // x,y values are corrected using matrix depending on screen orientation
        final int[] as = ACCELEROMETER_AXIS_SWAP[screenRotation];
        accelX = (float) as[0] * event.values[as[2]];
        accelY = (float) as[1] * event.values[as[3]];
        accelZ = event.values[2];
    }

    public float getAccelX() {
        return accelX;
    }

    public float getAccelY() {
        return accelY;
    }

    public float getAccelZ() {
        return accelZ;
    }

}
