package com.danosoftware.galaxyforce.services.vibration;

public enum VibrateTime {

    TINY(75), SHORT(125), MEDIUM(250), LONG(500);

    private final long timeInMilliseconds;

    VibrateTime(long timeInMilliseconds) {
        this.timeInMilliseconds = timeInMilliseconds;
    }

    public long getTimeInMilliseconds() {
        return timeInMilliseconds;
    }
}
