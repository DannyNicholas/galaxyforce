package com.danosoftware.galaxyforce.input;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;

import com.danosoftware.galaxyforce.interfaces.Input;

import java.util.List;

public class GameInput implements Input {

    AccelerometerHandler accelHandler;
    TouchHandler touchHandler;

    public GameInput(Context context, View view, float scaleX, float scaleY) {
        accelHandler = new AccelerometerHandler(context);

        if (Integer.parseInt(VERSION.SDK) < 5)
            touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
        else
            touchHandler = new MultiTouchHandler(view, scaleX, scaleY);
    }

    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }

    public float getAccelX() {
        return accelHandler.getAccelX();
    }

    public float getAccelY() {
        return accelHandler.getAccelY();
    }

    public float getAccelZ() {
        return accelHandler.getAccelZ();
    }

    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }

}
