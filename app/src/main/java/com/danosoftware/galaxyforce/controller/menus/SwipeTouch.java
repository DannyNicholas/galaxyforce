package com.danosoftware.galaxyforce.controller.menus;

import com.danosoftware.galaxyforce.controller.interfaces.Swipe;
import com.danosoftware.galaxyforce.controller.interfaces.TouchController;
import com.danosoftware.galaxyforce.controller.utilities.TouchButton;
import com.danosoftware.galaxyforce.interfaces.Input.TouchEvent;
import com.danosoftware.galaxyforce.view.Vector2;

public class SwipeTouch implements TouchController {

    /* reference to the finger's touch pointer */
    private int FINGER_POINTER = -1;

    // reference to swipe start point
    private Vector2 startTouchPoint = null;

    /* contains reference to parent swipe */
    private Swipe swipe = null;

    public SwipeTouch(Swipe swipe) {
        this.swipe = swipe;
    }

    /*
     * check if finger is starting, continuing or ending a swipe. Pass on
     * location of finger in each case.
     */
    @Override
    public boolean processTouchEvent(TouchEvent event, Vector2 touchPoint, int pointerID, float deltaTime) {

        // check finger pressed - no need to check bounds as anywhere on screen
        // is valid
        if (event.type == TouchEvent.TOUCH_DOWN) {
            FINGER_POINTER = pointerID;
            startTouchPoint = touchPoint;
            swipe.fingerDown(touchPoint);
        }

        // check finger released
        if (pointerID == FINGER_POINTER && event.type == TouchEvent.TOUCH_UP) {
            FINGER_POINTER = -1;
            swipe.fingerUp(touchPoint);
        }

        // check if finger dragged significantly
        if (pointerID == FINGER_POINTER && TouchButton.isDragged(event.type, startTouchPoint, touchPoint)) {
            swipe.fingerDragged(touchPoint);
        }

        // return false to allow other touch controllers to be processed
        return false;
    }
}
