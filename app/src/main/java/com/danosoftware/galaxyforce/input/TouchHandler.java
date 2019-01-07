package com.danosoftware.galaxyforce.input;

import android.view.View.OnTouchListener;

import com.danosoftware.galaxyforce.input.Input.TouchEvent;

import java.util.List;

public interface TouchHandler extends OnTouchListener {
    boolean isTouchDown(int pointer);

    int getTouchX(int pointer);

    int getTouchY(int pointer);

    List<TouchEvent> getTouchEvents();
}
