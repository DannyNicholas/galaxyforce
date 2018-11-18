package com.danosoftware.galaxyforce.services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class PreferencesString implements IPreferences<String> {
    private final SharedPreferences preferences;

    public PreferencesString(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            this.preferences = activity.getPreferences(Context.MODE_PRIVATE);
        } else {
            throw new IllegalArgumentException("Supplied context is not an instance of an Activity.");
        }
    }

    @Override
    public boolean preferenceExists(String key) {
        return preferences.contains(key);
    }

    @Override
    public String getPreference(String key, String defaultValue) {
        // get preference from shared preferences persistence.
        // if none exists then use default.
        return preferences.getString(key, defaultValue);
    }

    @Override
    public void storePreference(Map<String, String> keyValueMap) {
        // persist keys and values
        SharedPreferences.Editor editor = preferences.edit();

        for (String key : keyValueMap.keySet()) {
            editor.putString(key, keyValueMap.get(key));
        }

        editor.commit();
    }
}
