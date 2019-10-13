package com.danosoftware.galaxyforce.services.googleplay;

public interface GooglePlaySavedGameObserver {
    /**
     * Notify a google play service observer that a saved game
     * has been loaded. Normally following a connection to
     * the Google Play Service.
     *
     * @param savedGame - loaded saved game
     */
    void onSavedGameLoaded(
            GooglePlaySavedGame savedGame);
}
