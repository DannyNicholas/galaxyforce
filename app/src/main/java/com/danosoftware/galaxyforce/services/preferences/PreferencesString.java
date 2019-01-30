package com.danosoftware.galaxyforce.services.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;

import java.util.Map;

/**
 * Handles string preferences.
 * Methods are synchronized. May be called by multiple other threads
 * to prevent I/O operations delaying main thread.
 */
public class PreferencesString implements IPreferences<String> {
    private final SharedPreferences preferences;

    public PreferencesString(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            this.preferences = activity.getPreferences(Context.MODE_PRIVATE);
        } else {
            throw new GalaxyForceException("Supplied context is not an instance of an Activity.");
        }
    }

    @Override
    public synchronized String getPreference(String key, String defaultValue) {
        // get preference from shared preferences persistence.
        // if none exists then use default.
        return preferences.getString(key, defaultValue);
    }

    @Override
    public synchronized void storePreference(Map<String, String> keyValueMap) {
        // persist keys and values
        SharedPreferences.Editor editor = preferences.edit();

        for (String key : keyValueMap.keySet()) {
            editor.putString(key, keyValueMap.get(key));
        }

        editor.apply();
    }
}
