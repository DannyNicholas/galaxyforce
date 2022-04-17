package com.danosoftware.galaxyforce.models.screens;

import com.danosoftware.galaxyforce.models.screens.background.RgbColour;
import com.danosoftware.galaxyforce.sprites.providers.SpriteProvider;
import com.danosoftware.galaxyforce.text.TextProvider;

/**
 * Generic interface for any models containing the logic behind any screens.
 */
public interface Model {

  TextProvider getTextProvider();

  SpriteProvider getSpriteProvider();

  void update(float deltaTime);

  /**
   * Called when model associated with screen can be disposed with. Since references to the models
   * can be held, any objects that do not need references to be kept should be disposed. This allows
   * these objects to be garbage collected. Setting references of unneeded objects to null will
   * allow garbage collection. If the model is needed again, objects should be re-initialised using
   * the initialise() method.
   */
  void dispose();

  /**
   * Handle "back button" behaviour
   */
  void goBack();

  /**
   * Pause the current game model. Has no effect if model is already paused.
   */
  void pause();

  /**
   * Resume the current game model.
   */
  void resume();

  /**
   * Get background colour.
   */
  RgbColour background();

  /**
   * Should stars animate for this model.
   */
  boolean animateStars();
}