package com.danosoftware.galaxyforce.controller.utilities;

import android.util.Log;

import com.danosoftware.galaxyforce.buttons.interfaces.Button;
import com.danosoftware.galaxyforce.controller.interfaces.TouchController;
import com.danosoftware.galaxyforce.interfaces.Input.TouchEvent;
import com.danosoftware.galaxyforce.utilities.OverlapTester;
import com.danosoftware.galaxyforce.view.Vector2;

public class DetectButtonTouch implements TouchController
{

    /* reference to the button's touch pointer */
    private int BUTTON_POINTER = -1;

    // reference to swipe start point
    private Vector2 startTouchPoint = null;

    /* contains reference to parent button */
    private Button button = null;

    public DetectButtonTouch(Button button)
    {
        this.button = button;
    }

    /*
     * check if button pressed. button stays pressed until released or finger
     * dragged away from button.
     */
    @Override
    public boolean processTouchEvent(TouchEvent event, Vector2 touchPoint, int pointerID, float deltaTime)
    {
        boolean processed = false;

        boolean buttonBounds = OverlapTester.pointInRectangle(button.getBounds(), touchPoint);

        // check button pressed
        if (TouchButton.isButtonPressed(buttonBounds, event.type))
        {
            BUTTON_POINTER = pointerID;
            startTouchPoint = touchPoint;
            button.buttonDown();
            processed = true;
        }

        // check button released
        if (pointerID == BUTTON_POINTER && TouchButton.isButtonReleased(buttonBounds, event.type))
        {
            BUTTON_POINTER = -1;
            button.buttonUp();
            processed = true;
        }

        // check if finger dragged away from button (released but not pressed)
        if (pointerID == BUTTON_POINTER && TouchButton.isDraggedOutsideButton(buttonBounds, event.type))
        {
            BUTTON_POINTER = -1;
            button.buttonReleased();
            processed = true;
        }

        // check if finger dragged significantly from original position (should
        // now be considered as a swipe and so not a button press)
        if (pointerID == BUTTON_POINTER && TouchButton.isDragged(event.type, startTouchPoint, touchPoint))
        {
            BUTTON_POINTER = -1;
            button.buttonReleased();
            processed = true;
        }

        if (processed) {
            Log.i(button.toString(), "Event " + event + " touch " + touchPoint);
        }

        return processed;
    }
}
