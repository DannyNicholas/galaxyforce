package com.danosoftware.galaxyforce.services.savedgame;

import com.danosoftware.galaxyforce.services.googleplay.GooglePlaySavedGame;

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
   *
   * @param observer - observers interested in highest level unlocks
   */
  void registerHighestLevelChangeObserver(HighestLevelChangeObserver observer);

  /**
   * Unregister an observer for highest level unlocked changes
   *
   * @param observer - observers interested in highest level unlocks
   */
  void unregisterHighestLevelChangeObserver(HighestLevelChangeObserver observer);

  /**
   * Google Play Service will call on saved game loads from the cloud. Use this to compute the
   * highest wave seen and return the result.
   *
   * @param savedGame - supplied game loaded from cloud
   * @return - computed highest game loaded
   */
  GooglePlaySavedGame computeHighestWaveOnSavedGameLoaded(GooglePlaySavedGame savedGame);
}
