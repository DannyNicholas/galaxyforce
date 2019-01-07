package com.danosoftware.galaxyforce.controllers.touch;

import com.danosoftware.galaxyforce.buttons.button.Button;
import com.danosoftware.galaxyforce.controllers.utilities.TouchButton;
import com.danosoftware.galaxyforce.input.Input.TouchEvent;
import com.danosoftware.galaxyforce.utilities.OverlapTester;
import com.danosoftware.galaxyforce.view.Vector2;

public class DetectButtonTouch implements TouchController {

    /* reference to the button's touch pointer */
    private int BUTTON_POINTER = -1;

    // reference to swipe start point
    private Vector2 startTouchPoint = null;

    /* contains reference to parent button */
    private final Button button;

    public DetectButtonTouch(Button button) {
        this.button = button;
    }

    /*
     * check if button pressed. button stays pressed until released or finger
     * dragged away from button.
     */
    @Override
    public boolean processTouchEvent(TouchEvent event, Vector2 touchPoint, int pointerID) {
        boolean processed = false;

        boolean buttonBounds = OverlapTester.pointInRectangle(button.getBounds(), touchPoint);

        // check button pressed
        if (TouchButton.isButtonPressed(buttonBounds, event.type)) {
            BUTTON_POINTER = pointerID;
            startTouchPoint = touchPoint;
            button.buttonDown();
            processed = true;
        }

        // check button released
        if (pointerID == BUTTON_POINTER && TouchButton.isButtonReleased(buttonBounds, event.type)) {
            BUTTON_POINTER = -1;
            button.buttonUp();
            processed = true;
        }

        // check if finger dragged away from button (released but not pressed)
        if (pointerID == BUTTON_POINTER && TouchButton.isDraggedOutsideButton(buttonBounds, event.type)) {
            BUTTON_POINTER = -1;
            button.buttonReleased();
            processed = true;
        }

        // check if finger dragged significantly from original position (should
        // now be considered as a swipe and so not a button press)
        if (pointerID == BUTTON_POINTER && TouchButton.isDragged(event.type, startTouchPoint, touchPoint)) {
            BUTTON_POINTER = -1;
            button.buttonReleased();
            processed = true;
        }

        return processed;
    }
}
