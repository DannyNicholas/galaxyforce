package com.danosoftware.galaxyforce.view;

import android.util.Log;

public class FPSCounter {
    long startTime = System.nanoTime();
    int frames = 0;
    int previousFrames = 0;

    public void logFrame() {
        Log.d("FPSCounter", "fps: " + getValue());
    }

    public String getCounter() {
        return "" + getValue();
    }

    public int getValue() {
        int fpsValue = previousFrames;

        frames++;
        if (System.nanoTime() - startTime >= 1000000000) {
            fpsValue = frames;
            previousFrames = frames;
            frames = 0;
            startTime = System.nanoTime();
        }

        return fpsValue;
    }
}
