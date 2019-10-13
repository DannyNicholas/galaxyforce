package com.danosoftware.galaxyforce.services.googleplay;

/**
 * Represents the current state of connections to Google Play Service
 */
public enum ConnectionState {

    // no request made yet
    NO_ATTEMPT,

    // successfully connected to Google Play Service
    CONNECTED,

    // not connected (either failed or logged out)
    DISCONNECTED
}
