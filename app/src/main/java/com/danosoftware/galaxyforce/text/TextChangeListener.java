package com.danosoftware.galaxyforce.text;

/**
 * Listener to be implemented to be notified of any text changes.
 * <p>
 * Useful to re-build on-screen text following a change.
 */
public interface TextChangeListener {

  void onTextChange();
}
