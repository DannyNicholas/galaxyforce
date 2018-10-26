package com.danosoftware.galaxyforce.sprites.game.aliens;

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
     * alien has reached the end of the path
     */
    void endOfPass();

    /**
     * Get the original time delay. Can be used to calculate a corrected time
     * delay offset.
     */
    float getTimeDelay();
}
