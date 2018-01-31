package com.danosoftware.galaxyforce.waves;

/**
 * Alien waves consist of a collection of sub-waves.
 * 
 * A wave manager is responsible for co-ordinating the creation of waves. It
 * also provides each sub-wave to a client when requested until all sub-waves
 * have been retrieved.
 */
public interface WaveManager
{

    /**
     * Setup the creation of the supplied wave of aliens.
     * 
     * Setting up waves is an asynchronous task allowing the client to continue
     * while the wave is created. The client should call the isWaveReady()
     * method at some point later to check if the wave is ready to use.
     * 
     * @param wave
     */
    public void setUpWave(int wave);

    /**
     * Sets wave ready. Normally via a callback from a wave creation task. Once
     * set, calls to isWaveReady() should return true.
     * 
     * @param wave
     *            - created wave of aliens
     */
    public void setWaveReady(Wave wave);

    /**
     * Returns true if requested wave is now ready to use.
     * 
     * Specific sub-waves can then be retrieved using hasNext() and next()
     * iterator methods
     * 
     * @return true is wave ready to use
     */
    public boolean isWaveReady();

    /**
     * Is there another sub-wave to retrieve. If true, a call to next() will
     * return the next sub-wave. If false, the end of the wave has been reached.
     * 
     * @return true if there are more sub-waves to retrieve
     */
    public boolean hasNext();

    /**
     * Return the next sub-wave in the current wave.
     * 
     * @return sub-wave
     */
    public SubWave next();
}