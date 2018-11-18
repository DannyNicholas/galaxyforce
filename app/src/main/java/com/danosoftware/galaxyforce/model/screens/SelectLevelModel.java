package com.danosoftware.galaxyforce.model.screens;

public interface SelectLevelModel {

    void setLevel(int level);

    void changeZone(int newZone);

    /**
     * Screen currently being swiped and should be moved by provided offset.
     *
     * @param xDelta
     */
    void swipeUpdate(float xDelta);

    /**
     * Screen currently being swiped and should be moved by provided offset.
     *
     * @param xOffset
     */
    void swipeStart();

    /**
     * Screen swipe has finished at provided offset. Screen should be updated to
     * nearest appropriate x position.
     *
     * @param xDelta
     */
    void swipeFinish(float xDelta);
}
