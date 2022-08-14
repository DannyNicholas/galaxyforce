package com.danosoftware.galaxyforce.services.googleplay;

/**
 * Represents the current state of connections to Google Play Service
 */
public enum ConnectionState {

  // no authentication request made yet
  NO_ATTEMPT,

  // user has been authenticated
  AUTHENTICATED,

  // user could not be authenticated
  FAILED
}
