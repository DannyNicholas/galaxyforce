package com.danosoftware.galaxyforce.controller.menus;

import com.danosoftware.galaxyforce.controller.interfaces.Controller;
import com.danosoftware.galaxyforce.controller.interfaces.Swipe;
import com.danosoftware.galaxyforce.model.screens.SelectLevelModel;
import com.danosoftware.galaxyforce.view.Vector2;

/**
 * Represents a level selector. Level selector has a border, level number and
 * position.
 */
public class SelectLevelSwipe implements Swipe {
    // reference to parent model
    private SelectLevelModel model;

    // reference to swipe start point
    private Vector2 startTouchPoint = null;

    public SelectLevelSwipe(SelectLevelModel model, Controller controller) {
        this.model = model;

        // add a new menu button to controller's list of touch controllers
        controller.addTouchController(new SwipeTouch(this));

    }

    @Override
    public void fingerUp(Vector2 touchPoint) {
        // finish swipe - send latest delta between current point and start
        // point
        model.swipeFinish(startTouchPoint.x - touchPoint.x);
    }

    @Override
    public void fingerDown(Vector2 touchPoint) {
        // set new start touch point
        startTouchPoint = touchPoint;

        // start new swipe
        model.swipeStart();
    }

    @Override
    public void fingerDragged(Vector2 touchPoint) {
        // update swipe - send latest delta between current point and start
        // point
        model.swipeUpdate(startTouchPoint.x - touchPoint.x);
    }
}
