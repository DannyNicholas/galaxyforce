package com.danosoftware.galaxyforce.controller.game;

import com.danosoftware.galaxyforce.controller.interfaces.Controller;
import com.danosoftware.galaxyforce.controller.interfaces.TouchController;
import com.danosoftware.galaxyforce.interfaces.Input;
import com.danosoftware.galaxyforce.interfaces.Input.TouchEvent;
import com.danosoftware.galaxyforce.view.Camera2D;
import com.danosoftware.galaxyforce.view.Vector2;

import java.util.ArrayList;
import java.util.List;


// TODO is this the same as MenuController - if so remove one!
public class ControllerGame implements Controller
{
    /* reference to game input */
    private Input input = null;

    /* reference to the camera */
    private Camera2D camera = null;

    // list of any touch controllers to be processed (e.g. joystick, buttons).
    private List<TouchController> touchControllers = null;

    // current base controller being used
//    private BaseController baseController = null;

    public ControllerGame(Input input, Camera2D camera)
    {
        this.input = input;
        this.camera = camera;
        this.touchControllers = new ArrayList<TouchController>();
    }

    @Override
    public void update(float deltaTime)
    {
        // if accelerometer - process tilt movement
//        if (baseController instanceof BaseControllerTilt)
//        {
//            ((BaseControllerTilt) baseController).updateController(input);
//        }

        List<TouchEvent> touchEvents = input.getTouchEvents();
        int len = touchEvents.size();

        for (int i = 0; i < len; i++)
        {
            // get current touch event
            TouchEvent event = touchEvents.get(i);

            // get pointer to identify touch event
            int pointerID = event.pointer;

            // set the touch point x,y,
            Vector2 touchPoint = new Vector2(event.x, event.y);
//            touchPoint.set(event.x, event.y);

            // convert touch point to current camera x,y
            camera.touchToWorld(touchPoint);

            boolean processed = false;

            // process any wanted touch controllers (e.g. buttons,
            // joysticks).
            for (TouchController aTouchController : touchControllers)
            {
                // only process touch controller if no other controllers
                // have
                // already processed this touch point.
                if (!processed)
                {
                    processed = aTouchController.processTouchEvent(event, touchPoint, pointerID, deltaTime);
                }
            }

            // only process base controller if no other touch controllers have
            // already processed this touch point.
//            if (!processed && (baseController instanceof TouchController))
//            {
//                // process base controller as touch controller (i.e. drag or
//                // joystick)
//                ((TouchController) baseController).processTouchEvent(event, touchPoint, pointerID, deltaTime);
//            }
        }
    }

    @Override
    public void addTouchController(TouchController touchController)
    {
        touchControllers.add(touchController);
    }

//    @Override
//    public void reset()
//    {
//        baseController.reset();
//    }

//    @Override
//    public void setBaseController(BaseController baseController)
//    {
//        this.baseController = baseController;
//    }

    @Override
    public void clearTouchControllers()
    {
        touchControllers.clear();
//        baseController = null;
    }
}
