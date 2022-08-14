package com.danosoftware.galaxyforce.services.savedgame;

import com.danosoftware.galaxyforce.services.googleplay.GooglePlaySavedGame;
import com.danosoftware.galaxyforce.services.googleplay.GooglePlayServices;
import com.danosoftware.galaxyforce.services.preferences.IPreferences;
import com.danosoftware.galaxyforce.utilities.WaveUtilities;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

public class SavedGameImpl implements SavedGame {

  // key for shared preference persistence
  private final static String GAME_LEVEL_KEY = "game.level";

  // default level if saved game option not already persisted
  private final static int DEFAULT_GAME_LEVEL = 1;

  // reference to shared preferences
  private final IPreferences<Integer> preferences;

  // reference to Google Play Services that can persist to the cloud
  private final GooglePlayServices playService;

  // current highest game level reached
  private volatile int gameLevel;

  /*
   * set of observers to be notified following any highest level unlocked changes.
   */
  private final Set<HighestLevelChangeObserver> highestLevelChangeObservers;

  public SavedGameImpl(
      final IPreferences<Integer> preferences,
      final GooglePlayServices playService) {
    this.preferences = preferences;
    this.playService = playService;
    this.highestLevelChangeObservers = new HashSet<>();

    // retrieve game level from preferences
    int retrievedLevel = preferences.getPreference(GAME_LEVEL_KEY, DEFAULT_GAME_LEVEL);
    if (WaveUtilities.isValidWave(retrievedLevel)) {
      this.gameLevel = retrievedLevel;
    } else {
      this.gameLevel = DEFAULT_GAME_LEVEL;
    }

    // register for saved game loads from the google play service
    playService.registerSavedGameService(this);
  }

  @Override
  public synchronized void registerHighestLevelChangeObserver(HighestLevelChangeObserver observer) {
    highestLevelChangeObservers.add(observer);
  }

  @Override
  public void unregisterHighestLevelChangeObserver(HighestLevelChangeObserver observer) {
    highestLevelChangeObservers.remove(observer);
  }

  private void notifyHighestLevelChangeObservers(int level) {
    for (HighestLevelChangeObserver observer : highestLevelChangeObservers) {
      observer.onHighestLevelUnlockedChange(level);
    }
  }

  @Override
  public int getGameLevel() {
    return gameLevel;
  }

  @Override
  public synchronized void saveGameLevel(final int gameLevel) {
    this.gameLevel = gameLevel;

    // persist game level
    saveLocalDevice(gameLevel);

    // persist game level to Google Play Services
    saveGooglePlay(gameLevel);

    notifyHighestLevelChangeObservers(gameLevel);
  }

  // called when Google Play Services have loaded a saved game from the cloud.
  // compute and return the highest level seen based on local device and cloud saved games.
  // if the cloud's saved wave is higher than our current wave, then update our device
  @Override
  public synchronized GooglePlaySavedGame computeHighestWaveOnSavedGameLoaded(
      GooglePlaySavedGame googleSavedGame) {

    final int googlePlayGameLevel = googleSavedGame.getHighestWaveReached();

    // reset local device level if Google Play shows player has previously reached a higher level
    if (googlePlayGameLevel > gameLevel) {
      gameLevel = googlePlayGameLevel;
      saveLocalDevice(gameLevel);
      notifyHighestLevelChangeObservers(gameLevel);
      return googleSavedGame;
    }

    // otherwise player has already reached the cloud's saved wave or exceeded it on this device.
    // so return this value back to Google Play service so other devices may also benefit
    // from playing this higher level
    return new GooglePlaySavedGame(gameLevel);
  }

  private void saveLocalDevice(final int gameLevel) {
    Executors.newSingleThreadExecutor().execute(() -> {
      Map<String, Integer> keyValueMap = new HashMap<>();
      keyValueMap.put(GAME_LEVEL_KEY, gameLevel);
      preferences.storePreference(keyValueMap);
    });
  }

  private void saveGooglePlay(final int gameLevel) {
    playService.saveGame(new GooglePlaySavedGame(gameLevel));
  }
}
