package com.danosoftware.galaxyforce.sprites.refactor;

import java.util.List;

public interface IAlienManager {

    List<IAlien> activeAliens();

    List<IAlien> allAliens();

    void animate(float deltaTime);

    void setUpWave(int wave);

    boolean isWaveReady();

    boolean isWaveComplete();

    /**
     * Return a randomly selected active alien.
     * Can return null if no aliens are active.
     */
    IAlien chooseActiveAlien();

    /**
     * Add new spawned aliens to the current wave.
     */
    void spawnAliens(List<IAlien> spawnedAliens);
}
