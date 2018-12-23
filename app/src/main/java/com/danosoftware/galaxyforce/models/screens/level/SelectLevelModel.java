package com.danosoftware.galaxyforce.models.screens.level;

public interface SelectLevelModel {

    void setLevel(int level);

    void changeZone(int newZone);

    /**
     * Screen currently being swiped and should be moved by provided offset.
     */
    void swipeUpdate(float xDelta);

    /**
     * Screen currently being swiped and should be moved by provided offset.
     */
    void swipeStart();

    /**
     * Screen swipe has finished at provided offset. Screen should be updated to
     * nearest appropriate x position.
     */
    void swipeFinish(float xDelta);
}
