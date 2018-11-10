package com.danosoftware.galaxyforce.controller.game;

import com.danosoftware.galaxyforce.controller.interfaces.BaseTouchController;
import com.danosoftware.galaxyforce.controller.interfaces.TouchBaseControllerModel;
import com.danosoftware.galaxyforce.interfaces.Input.TouchEvent;
import com.danosoftware.galaxyforce.view.Vector2;

public class ControllerDrag implements BaseTouchController //BaseController
{
    // pointer to finger currently controlling this drag
    private int dragPointer = -1;

    /* contains reference to game model */
//    private final GameHandler model;

    /* reference to drag model */
    private TouchBaseControllerModel dragModel;

    @Override
    public void setBaseController(TouchBaseControllerModel dragModel) {
        this.dragModel = dragModel;

    }

    @Override
    public boolean processTouchEvent(TouchEvent event, Vector2 touchPoint, int pointerID, float deltaTime)
    {
        boolean processed = false;

        // on touch down: set drag pointer, set centre based on base's location,
        // update touch point
        if (event.type == TouchEvent.TOUCH_DOWN && dragPointer == -1)
        {
            dragPointer = pointerID;
//            dragModel.setCentre(model.getBaseX(), model.getBaseY());
            dragModel.updateTouchPoint(touchPoint.x, touchPoint.y, deltaTime);
            processed = true;
        }

        // on drag: set centre on base's location and update touch point
        if (event.type == TouchEvent.TOUCH_DRAGGED && pointerID == dragPointer)
        {
//            dragModel.setCentre(model.getBaseX(), model.getBaseY());
            dragModel.updateTouchPoint(touchPoint.x, touchPoint.y, deltaTime);
            processed = true;
        }

        // on release: release touch point and reset drag pointer
        if (event.type == TouchEvent.TOUCH_UP && pointerID == dragPointer)
        {
            // reset joystick to centre
//            dragModel.setCentre(model.getBaseX(), model.getBaseY());
            dragModel.releaseTouchPoint();
            dragPointer = -1;
            processed = true;
        }

        return processed;
    }


//    @Override
//    public void reset()
//    {
//        // resets centre to current base position
//        dragModel.setCentre(model.getBaseX(), model.getBaseY());
//
//        // resets weighting and sets target to base position
//        dragModel.reset();
//    }
}
