package com.danosoftware.galaxyforce.controllers.common;

import com.danosoftware.galaxyforce.controllers.touch.TouchController;

public interface Controller {

  /**
   * update model using any set-up controllers.
   */
  void update(float deltaTime);

  /**
   * add a touch controller to be monitored.
   */
  void addTouchController(TouchController touchController);

  /**
   * Remove all existing touch controllers
   */
  void clearTouchControllers();
}
