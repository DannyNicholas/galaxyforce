package com.danosoftware.galaxyforce.controllers.models.base_touch;

/**
 * Base Controller Model interface for touch point implementations (e.g. drag
 * and joystick).
 */
public interface TouchBaseControllerModel {

    void releaseTouchPoint();

    /**
     * Updates the current touch point and calculates the base movement's
     * weighting using the current touch point referenced to the centre point.
     *
     * @param touchX x co-ordinate of touch point
     * @param touchY y co-ordinate of touch point
     */
    void updateTouchPoint(float touchX, float touchY);
}
