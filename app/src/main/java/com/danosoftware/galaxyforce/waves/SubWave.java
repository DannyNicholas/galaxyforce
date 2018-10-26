package com.danosoftware.galaxyforce.waves;

import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.waves.config.SubWaveRepeatMode;

import java.util.List;

/**
 * Represents a sub-wave. A wave represents a collection of sub-waves.
 * 
 * Each sub-wave contains a collection of aliens and parameters about the
 * sub-wave
 */
public class SubWave
{
    // list of aliens in this sub-wave
    private final List<IAlien> aliens;

    // should the sub-wave be repeated until all aliens are destroyed
    private final SubWaveRepeatMode waveRepeatedMode;

    public SubWave(
            final List<IAlien> aliens,
            final SubWaveRepeatMode waveRepeatedMode)
    {
        this.aliens = aliens;
        this.waveRepeatedMode = waveRepeatedMode;
    }

    public List<IAlien> getAliens()
    {
        return aliens;
    }

    public boolean isWaveRepeated()
    {
        return (waveRepeatedMode == SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED);
    }
}
