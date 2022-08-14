package com.danosoftware.galaxyforce.services.savedgame;

public interface HighestLevelChangeObserver {

  /**
   * Notify an observer that the highest unlocked game level has changed. Normally triggered by
   * loading a new saved game.
   *
   * @param level - game level
   */
  void onHighestLevelUnlockedChange(int level);
}
