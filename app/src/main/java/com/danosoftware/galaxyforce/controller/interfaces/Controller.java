package com.danosoftware.galaxyforce.controller.interfaces;

public interface Controller {
    /**
     * update model using any set-up controllers.
     */
    void update(float deltaTime);

    /**
     * add a touch controller to be monitored.
     *
     * @param touchController
     */
    void addTouchController(TouchController touchController);

    /**
     * Remove all existing touch controllers
     */
    void clearTouchControllers();
}
