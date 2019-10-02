package com.danosoftware.galaxyforce.services.googleplay;

public interface GooglePlayObserver {
    /**
     * Notify a google play service observer that the connection state of the
     * google play service has changed (possibly following a login or logout
     * request).
     *
     * @param state - latest state of google play connection
     */
    void onConnectionStateChange(ConnectionState state);
}
