package com.danosoftware.galaxyforce.controller.menus;

import com.danosoftware.galaxyforce.controller.interfaces.Controller;
import com.danosoftware.galaxyforce.controller.interfaces.TouchController;
import com.danosoftware.galaxyforce.interfaces.Input;
import com.danosoftware.galaxyforce.interfaces.Input.TouchEvent;
import com.danosoftware.galaxyforce.view.Camera2D;
import com.danosoftware.galaxyforce.view.Vector2;

import java.util.ArrayList;
import java.util.List;

public class MenuController implements Controller
{

    /* reference to game input */
    private Input input = null;

    /* reference to the camera */
    private Camera2D camera = null;

    // list of any touch controllers to be processed (e.g. joystick, buttons).
    private List<TouchController> touchControllers = null;

    public MenuController(Input input, Camera2D camera)
    {
        this.input = input;
        this.camera = camera;
        this.touchControllers = new ArrayList<TouchController>();
    }

    @Override
    public void update(float deltaTime)
    {

        List<TouchEvent> touchEvents = input.getTouchEvents();
        int len = touchEvents.size();

        for (int i = 0; i < len; i++)
        {
            // get current touch event
            TouchEvent event = touchEvents.get(i);

            // get pointer to identify touch event
            int pointerID = event.pointer;

            // set the touch point x,y,
            Vector2 touchPoint = new Vector2();
            touchPoint.set(event.x, event.y);

            // convert touch point to current camera x,y
            camera.touchToWorld(touchPoint);

            boolean processed = false;

            // process any wanted touch controllers (e.g. buttons, joysticks).
            for (TouchController aTouchController : touchControllers)
            {
                // only process touch controller if no other controllers have
                // already processed this touch point.
                if (!processed)
                {
                    processed = aTouchController.processTouchEvent(event, touchPoint, pointerID, deltaTime);
                }
            }

        }
    }

    @Override
    public void addTouchController(TouchController touchController)
    {
        touchControllers.add(touchController);
    }

    @Override
    public void clearTouchControllers()
    {
        touchControllers.clear();
    }
}
