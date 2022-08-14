package com.danosoftware.galaxyforce.exceptions;

import android.util.Log;
import com.danosoftware.galaxyforce.constants.GameConstants;

/**
 * Galaxy Force runtime exception for use in cases when the exception is unrecoverable.
 */
public class GalaxyForceException extends RuntimeException {

  private static final String ACTIVITY_TAG = "Galaxy Force Unrecoverable Exception";

  public GalaxyForceException(final String message, final Throwable e) {
    super(message, e);
    Log.e(GameConstants.LOG_TAG, ACTIVITY_TAG + ": " + message);
  }

  public GalaxyForceException(final String message) {
    super(message);
    Log.e(GameConstants.LOG_TAG, ACTIVITY_TAG + ": " + message);
  }

  public GalaxyForceException(final Throwable e) {
    super(e);
    Log.e(GameConstants.LOG_TAG, ACTIVITY_TAG);
  }
}
