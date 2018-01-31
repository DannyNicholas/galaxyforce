package com.danosoftware.galaxyforce.controller.utilities;

import com.danosoftware.galaxyforce.interfaces.Input.TouchEvent;
import com.danosoftware.galaxyforce.view.Vector2;

/**
 * This provides classes implementing touch buttons with common methods to help
 * check touch button state.
 */
public class TouchButton
{

    // constant used to determine if finger has been dragged from original
    // position
    final static int DRAG_DISTANCE = 20;

    // don't allow this class to be constructed
    private TouchButton()
    {

    }

    /**
     * check if button has been pressed. returns true if within a button's
     * bounds and finger has touched down
     * 
     * @param inButtonBounds
     *            - is finger within button bounds
     * @param touchEvent
     *            - touch event type
     * @return true if button has been pressed
     */
    public static boolean isButtonPressed(boolean inButtonBounds, int touchEvent)
    {
        if (inButtonBounds && touchEvent == TouchEvent.TOUCH_DOWN)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * check if button should be released returns true if finger has been lifted
     * within a button's bounds. this is normally processed as pressed button.
     * 
     * @param inButtonBounds
     *            - is finger within button bounds
     * @param touchEvent
     *            - touch event type
     * @return true if button has been released
     */
    public static boolean isButtonReleased(boolean inButtonBounds, int touchEvent)
    {
        if (inButtonBounds && touchEvent == TouchEvent.TOUCH_UP)
        {
            return true;
        }

        return false;
    }

    /**
     * check if button should be released as finger has been dragged outside
     * bounds. returns true if finger dragged outside button's bounds. this is
     * normally processed as a released button but NOT a pressed button.
     * 
     * @param inButtonBounds
     *            - is finger within button bounds
     * @param touchEvent
     *            - touch event type
     * @return true if finger has been dragged outside button bounds
     */
    public static boolean isDraggedOutsideButton(boolean inButtonBounds, int touchEvent)
    {
        if (touchEvent == TouchEvent.TOUCH_DRAGGED && !inButtonBounds)
        {
            return true;
        }

        return false;
    }

    /**
     * check if finger has been dragged significantly. returns true if finger
     * dragged significantly. this is normally processed as a swipe drag and if
     * button had previously been pressed, it will no longer be processed as a
     * pressed button.
     * 
     * @param inButtonBounds
     *            - is finger within button bounds
     * @param touchEvent
     *            - touch event type
     * @return true if finger has been dragged outside button bounds
     */
    public static boolean isDragged(int touchEvent, Vector2 startTouchPoint, Vector2 currentTouchPoint)
    {
        if (touchEvent == TouchEvent.TOUCH_DRAGGED && startTouchPoint.dist(currentTouchPoint) >= DRAG_DISTANCE)
        {
            return true;
        }

        return false;
    }

}
