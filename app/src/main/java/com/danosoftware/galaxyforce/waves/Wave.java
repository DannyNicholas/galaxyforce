package com.danosoftware.galaxyforce.waves;

import java.util.List;

/**
 * Alien waves consist of a list of sub-waves.
 * <p>
 * Each sub-wave completes before the next one starts.
 * Once all sub-waves are completed, the wave is completed.
 */
public class Wave {

    // list of sub-waves in this wave
    private final List<SubWave> subWaves;

    public Wave(List<SubWave> subWaves) {
        this.subWaves = subWaves;
    }

    public List<SubWave> getSubWaves() {
        return subWaves;
    }
}
