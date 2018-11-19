package com.danosoftware.galaxyforce.controllers.common;

import com.danosoftware.galaxyforce.controllers.touch.TouchController;
import com.danosoftware.galaxyforce.interfaces.Input;
import com.danosoftware.galaxyforce.interfaces.Input.TouchEvent;
import com.danosoftware.galaxyforce.view.Camera2D;
import com.danosoftware.galaxyforce.view.Vector2;

import java.util.ArrayList;
import java.util.List;

public class ControllerImpl implements Controller {

    /* reference to game input */
    private final Input input;

    /* reference to the camera */
    private final Camera2D camera;

    // list of any touch controllers to be processed (e.g. buttons).
    private final List<TouchController> touchControllers;

    public ControllerImpl(Input input, Camera2D camera) {
        this.input = input;
        this.camera = camera;
        this.touchControllers = new ArrayList<>();
    }

    @Override
    public void update(float deltaTime) {

        for (TouchEvent event : input.getTouchEvents()) {

            // get pointer to identify touch event
            int pointerID = event.pointer;

            // set the touch point x,y,
            Vector2 touchPoint = new Vector2(event.x, event.y);

            // convert touch point to current camera x,y
            camera.touchToWorld(touchPoint);

            boolean processed = false;

            // process any wanted touch controllers (e.g. buttons, joysticks).
            for (TouchController aTouchController : touchControllers) {
                // only process touch controller if no other controllers have
                // already processed this touch point.
                if (!processed) {
                    processed = aTouchController.processTouchEvent(event, touchPoint, pointerID);
                }
            }

        }
    }

    @Override
    public void addTouchController(TouchController touchController) {
        touchControllers.add(touchController);
    }

    @Override
    public void clearTouchControllers() {
        touchControllers.clear();
    }
}
