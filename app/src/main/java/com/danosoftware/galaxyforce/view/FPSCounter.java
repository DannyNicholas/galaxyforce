package com.danosoftware.galaxyforce.view;

public class FPSCounter {
    private long startTime = System.nanoTime();
    private int frames = 0;
    private int previousFrames = 0;

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
