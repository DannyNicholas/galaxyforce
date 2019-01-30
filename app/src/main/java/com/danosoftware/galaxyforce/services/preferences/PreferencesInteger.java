package com.danosoftware.galaxyforce.services.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class PreferencesInteger implements IPreferences<Integer> {
    private final SharedPreferences preferences;

    public PreferencesInteger(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            this.preferences = activity.getPreferences(Context.MODE_PRIVATE);
        } else {
            throw new IllegalArgumentException("Supplied context is not an instance of an Activity.");
        }
    }

    @Override
    public Integer getPreference(String key, Integer defaultValue) {
        // get preference from shared preferences persistence.
        // if none exists then use default.
        return preferences.getInt(key, defaultValue);
    }

    @Override
    public void storePreference(Map<String, Integer> keyValueMap) {
        // persist keys and values
        SharedPreferences.Editor editor = preferences.edit();

        for (String key : keyValueMap.keySet()) {
            editor.putInt(key, keyValueMap.get(key));
        }

        editor.apply();
    }
}