package com.danosoftware.galaxyforce.buttons.interfaces;

import com.danosoftware.galaxyforce.utilities.Rectangle;

/**
 * Button that can be selected by a user.
 */
public interface Button
{

    /**
     * Return the bounds of the button's area.
     * 
     * @return area of button's bounds.
     */
    Rectangle getBounds();

    /**
     * Button has been pressed and then released. Process the button request.
     */
    void buttonUp();

    /**
     * Button has been pressed. The button request should not be processed until
     * released.
     */
    void buttonDown();

    /**
     * Finger pressing button has been dragged outside the button's bounds.
     * Release button but do not process the button's request.
     */
    void buttonReleased();
}
