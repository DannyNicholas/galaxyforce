package com.danosoftware.galaxyforce.waves.managers;

import com.danosoftware.galaxyforce.waves.SubWave;
import com.danosoftware.galaxyforce.waves.Wave;
import com.danosoftware.galaxyforce.waves.utilities.WaveFactory;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WaveManagerImpl implements WaveManager {

    private enum WaveManagerState {
        NONE, CREATING_WAVE, WAVE_READY, ITERATING_WAVE
    }

    // executor service for threads to create waves
    private final ExecutorService pool;

    // iterator for sub-waves
    private Iterator<SubWave> iterator;

    // factory used to create waves
    private final WaveFactory waveFactory;

    // wave manager state
    private WaveManagerState state;

    public WaveManagerImpl(WaveFactory waveFactory) {
        this.waveFactory = waveFactory;

        // created single thread pool for creating waves
        this.pool = Executors.newFixedThreadPool(1);

        // set initial state
        this.state = WaveManagerState.NONE;
    }

    @Override
    public synchronized void setUpWave(int wave) {
        this.state = WaveManagerState.CREATING_WAVE;
        WaveCreator waveCreator = new WaveCreator(this, waveFactory, wave);
        pool.execute(waveCreator);
    }

    /*
     * Method is synchronised as it is called by the wave creation thread and
     * alters state. Could happen concurrently with isWaveReady() method.
     */
    @Override
    public synchronized void setWaveReady(Wave wave) {
        List<SubWave> subWaves = wave.getSubWaves();
        this.iterator = subWaves.iterator();
        this.state = WaveManagerState.WAVE_READY;
    }

    /*
     * Method is synchronised as it is called by the main UI thread and alters
     * state. Could happen concurrently with setWaveReady() method.
     */
    @Override
    public synchronized boolean isWaveReady() {
        // check if wave is ready
        boolean isReady = (state == WaveManagerState.WAVE_READY);

        // if ready switch to iterating state
        if (isReady) {
            this.state = WaveManagerState.ITERATING_WAVE;
        }

        return isReady;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public SubWave next() {
        return iterator.next();
    }
}
