package com.danosoftware.galaxyforce.vibration;

public enum VibrateTime
{

    TINY(75), SHORT(125), MEDIUM(250), LONG(500);

    private long timeInMilliseconds;

    VibrateTime(long timeInMilliseconds)
    {
        this.timeInMilliseconds = timeInMilliseconds;
    }

    public long getTimeInMilliseconds()
    {
        return timeInMilliseconds;
    }
}
