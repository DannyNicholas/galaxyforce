package com.danosoftware.galaxyforce.sprites.refactor;

import java.util.List;

public interface IAlienManager {

    List<IAlien> activeAliens();

    List<IAlien> allAliens();

    void animate(float deltaTime);

    void setUpWave(int wave);

    boolean isWaveReady();

    boolean isWaveComplete();
}
