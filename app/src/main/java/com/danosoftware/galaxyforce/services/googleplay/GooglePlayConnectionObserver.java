package com.danosoftware.galaxyforce.services.googleplay;

public interface GooglePlayConnectionObserver {

  /**
   * Notify a google play service observer that the player sign-in state has changed.
   * <p>
   * Used to tell observers that the player has sign-in was successful or has failed.
   */
  void onPlayerSignInStateChange();
}
