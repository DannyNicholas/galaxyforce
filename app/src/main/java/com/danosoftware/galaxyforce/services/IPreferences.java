package com.danosoftware.galaxyforce.services;

import java.util.Map;

public interface IPreferences<T> {

    /**
     * Return preference of wanted type. If no preference exists then return the
     * default value;
     *
     * @param key
     * @param defaultValue
     * @return
     */
    T getPreference(String key, T defaultValue);

    /**
     * Persist a map of string keys with values of wanted type.
     *
     * @param keyValueMap
     */
    void storePreference(Map<String, T> keyValueMap);

    /**
     * Return true if key is contained in preferences.
     *
     * @param key
     * @return
     */
    boolean preferenceExists(String key);
}