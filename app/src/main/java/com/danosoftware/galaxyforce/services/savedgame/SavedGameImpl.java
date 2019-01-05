package com.danosoftware.galaxyforce.services.savedgame;

import com.danosoftware.galaxyforce.services.preferences.IPreferences;
import com.danosoftware.galaxyforce.utilities.WaveUtilities;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class SavedGameImpl implements SavedGame {

    // key for shared preference persistence
    private final static String GAME_LEVEL_KEY = "game.level";

    // default level if saved game option not already persisted
    private final static int DEFAULT_GAME_LEVEL = 1;

    // reference to shared preferences
    private final IPreferences<Integer> preferences;

    // current highest game level reached
    private int gameLevel;

    public SavedGameImpl(IPreferences<Integer> preferences) {
        this.preferences = preferences;

        // retrieve game level from preferences
        int retrievedLevel = preferences.getPreference(GAME_LEVEL_KEY, DEFAULT_GAME_LEVEL);
        if (WaveUtilities.isValidWave(retrievedLevel)) {
            this.gameLevel = retrievedLevel;
        } else {
            this.gameLevel = DEFAULT_GAME_LEVEL;
        }
    }

    @Override
    public int getGameLevel() {
        return gameLevel;
    }

    @Override
    public void saveGameLevel(final int gameLevel) {
        this.gameLevel = gameLevel;

        // persist game level
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Integer> keyValueMap = new HashMap<>();
                keyValueMap.put(GAME_LEVEL_KEY, gameLevel);
                preferences.storePreference(keyValueMap);
            }
        });
    }
}
