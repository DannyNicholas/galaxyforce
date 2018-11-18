package com.danosoftware.galaxyforce.controller.interfaces;

import com.danosoftware.galaxyforce.view.Vector2;

/**
 * Processes user's finger swiped across screen.
 */
public interface Swipe {

    /**
     * Finger has been released from screen. Process as end of swipe.
     *
     * @param touchPoint - location of finger
     */
    void fingerUp(Vector2 touchPoint);

    /**
     * Finger has been pressed to screen. Process as start of swipe.
     *
     * @param touchPoint - location of finger
     */
    void fingerDown(Vector2 touchPoint);

    /**
     * Finger has been dragged around screen. Process as on-going swipe.
     *
     * @param touchPoint - location of finger
     */
    void fingerDragged(Vector2 touchPoint);

}
