package com.danosoftware.galaxyforce.services;

import com.danosoftware.galaxyforce.utilities.WaveUtilities;

import java.util.HashMap;
import java.util.Map;

public class SavedGame {
    // static values to be used for game level encryption
    private static int KEY = 13 * 13;
    private static int OFFSET = 13;

    // keys for shared preference persistence
    private final static String GAME_LEVEL_KEY = "game.level";

    // default values if saved game option not already persisted
    private final static Integer DEFAULT_GAME_LEVEL = 1;

    // reference to shared preferences
    private final IPreferences<Integer> preferences;

    // reference to current values
    private Integer gameLevel = null;

    // singleton instance;
    private static SavedGame instance = null;

    // private constructor
    private SavedGame(IPreferences<Integer> preferences) {
        this.preferences = preferences;
    }

    // must initialise singleton with preferences before it can be used
    public static void initialise(IPreferences<Integer> preferences) {
        if (instance == null) {
            instance = new SavedGame(preferences);
        } else {
            throw new IllegalStateException("SavedGame singleton has already been initialised.");
        }
    }

    // have SavedGame been initialised with preferences.
    public static boolean isInitialised() {
        // return true if initialised
        return (instance != null);
    }

    // get configurations singleton
    public static SavedGame getInstance() {
        if (instance != null) {
            return instance;
        } else {
            throw new IllegalStateException("SavedGame singleton has not been initialised.");
        }
    }

    /*
     * GAME LEVEL
     */

    // return the game level
    public Integer getGameLevel() {
        if (gameLevel == null) {

            /*
             * since we store an encrypted version of the game level, first
             * check to see if game level has been stored before. if not,
             * immediately return default level. we don't want to decrypt the
             * default level as this will corrupt it.
             */
            if (!preferences.preferenceExists(GAME_LEVEL_KEY)) {
                this.gameLevel = DEFAULT_GAME_LEVEL;
            }
            /*
             * otherwise get the encrypted game level, then decrypt and check it
             * is valid. if the level is not valid, return the default level.
             */
            else {
                // get encrypted game level from preferences
                int encryptedGameLevel = preferences.getPreference(GAME_LEVEL_KEY, DEFAULT_GAME_LEVEL);

                // create decrypted version of level number from persistence
                int decrypted = decrypt(encryptedGameLevel);

                if (WaveUtilities.isValidWave(decrypted)) {
                    this.gameLevel = decrypted;
                } else {
                    this.gameLevel = DEFAULT_GAME_LEVEL;
                }
            }
        }

        return gameLevel;
    }

    // set new controller type
    public void setGameLevel(Integer gameLevel) {
        if (gameLevel == null) {
            throw new IllegalArgumentException("Supplied gameLevel can not be null.");
        }

        this.gameLevel = gameLevel;
    }

    public void persistSavedGame() {
        Map<String, Integer> keyValueMap = new HashMap<String, Integer>();

        // create encrypted version of level number for persistence
        int encrypted = encrypt(gameLevel);
        keyValueMap.put(GAME_LEVEL_KEY, encrypted);

        // store configurations to shared preferences to persist in future
        preferences.storePreference(keyValueMap);
    }

    /**
     * Simple encryption/decrypted methods to avoid actual level numbers being
     * stored in preference file. This should deter users from trying to edit
     * these files to access later levels.
     */
    private int encrypt(int original) {
        int encrypted = (original + OFFSET) * KEY;

        return encrypted;
    }

    private int decrypt(int encrypted) {
        int decrypted = (encrypted / KEY) - OFFSET;

        return decrypted;
    }
}
