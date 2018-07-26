package com.danosoftware.galaxyforce.sprites.refactor;

public interface IAlienWithPath extends IAlien {

    /**
     * sets alien back to the beginning of the path with an amended
     * time-delay offset.
     */
    void reset(float offset);

    /**
     * has alien reached the end of it's path?
     */
    boolean isEndOfPass();

    /**
     * Get the original time delay. Can be used to calculate a corrected time
     * delay offset.
     */
    float getTimeDelay();
}
