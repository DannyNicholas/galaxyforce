package com.danosoftware.galaxyforce.input;

import android.view.View;

import java.util.List;

public class GameInput implements Input {

    private final TouchHandler touchHandler;

    public GameInput(View view, float scaleX, float scaleY) {
        this.touchHandler = new MultiTouchHandler(view, scaleX, scaleY);
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

    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }

}
