package com.danosoftware.galaxyforce.controllers.touch_base;

import com.danosoftware.galaxyforce.controllers.models.base_touch.TouchBaseControllerModel;
import com.danosoftware.galaxyforce.interfaces.Input.TouchEvent;
import com.danosoftware.galaxyforce.view.Vector2;

public class ControllerDrag implements BaseTouchController {

    // pointer to finger currently controlling this drag
    private int dragPointer = -1;

    // reference to drag model
    private TouchBaseControllerModel model;

    @Override
    public void setBaseController(TouchBaseControllerModel model) {
        this.model = model;
    }

    @Override
    public boolean processTouchEvent(TouchEvent event, Vector2 touchPoint, int pointerID) {
        boolean processed = false;

        // on touch down: set drag pointer, update touch point
        if (event.type == TouchEvent.TOUCH_DOWN && dragPointer == -1) {
            dragPointer = pointerID;
            model.updateTouchPoint(touchPoint.x, touchPoint.y);
            processed = true;
        }

        // on drag: update touch point
        if (event.type == TouchEvent.TOUCH_DRAGGED && pointerID == dragPointer) {
            model.updateTouchPoint(touchPoint.x, touchPoint.y);
            processed = true;
        }

        // on release: release touch point and reset drag pointer
        if (event.type == TouchEvent.TOUCH_UP && pointerID == dragPointer) {
            model.releaseTouchPoint();
            dragPointer = -1;
            processed = true;
        }

        return processed;
    }
}
