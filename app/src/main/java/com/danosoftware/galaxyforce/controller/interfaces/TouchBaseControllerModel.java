package com.danosoftware.galaxyforce.controller.interfaces;

/**
 * Base Controller Model interface for touch point implementations (e.g. drag
 * and joystick).
 */
public interface TouchBaseControllerModel extends BaseControllerModel
{

    void releaseTouchPoint();

    /**
     * Sets the centre point of the model. This may be the centre point for a
     * joystick implementation or the current base position for a drag
     * implementation. Weighting is calculated from centre point to touch point.
     * 
     * @param centreX
     *            x co-ordinate of centre point
     * @param centreY
     *            y co-ordinate of centre point
     */
//   void setCentre(float centreX, float centreY);

    /**
     * Updates the current touch point and calculates the base movement's
     * weighting using the current touch point referenced to the centre point.
     * 
     * @param touchX
     *            x co-ordinate of touch point
     * @param touchY
     *            y co-ordinate of touch point
     */
    void updateTouchPoint(float touchX, float touchY, float deltaTime);
}
