package com.danosoftware.galaxyforce.services.savedgame;

/**
 * Allows retrieval and persistence of highest level reached.
 */
public interface SavedGame {

    /**
     * Get game level reached
     */
    int getGameLevel();

    /**
     * Persist game level reached.
     */
    void saveGameLevel(int gameLevel);

    /**
     * Register an observer for highest level unlocked changes
     * @param observer
     */
    void registerHighestLevelChangeObserver(HighestLevelChangeObserver observer);

    /**
     * Unregister an observer for highest level unlocked changes
     * @param observer
     */
    void unregisterHighestLevelChangeObserver(HighestLevelChangeObserver observer);
}
