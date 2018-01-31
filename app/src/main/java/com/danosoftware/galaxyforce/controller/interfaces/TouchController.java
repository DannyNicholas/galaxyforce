package com.danosoftware.galaxyforce.controller.interfaces;

import com.danosoftware.galaxyforce.interfaces.Input.TouchEvent;
import com.danosoftware.galaxyforce.view.Vector2;

public interface TouchController
{

    /**
     * process touch points. for example a touch button may implement this to
     * detect and process button presses. returns true if touch point was
     * processed by this controller - stops other controllers processing the
     * same touch point.
     * 
     * @param event
     *            - the touch event (e.g.TOUCH DOWN)
     * @param touchPoint
     *            - vector representing the touch point
     * @param pointerID
     *            - pointer ID of the current touch event
     * @param deltaTime
     *            - time since last update
     * @return returns true if touch point was processed by this controller
     */
    public boolean processTouchEvent(TouchEvent event, Vector2 touchPoint, int pointerID, float deltaTime);
}
