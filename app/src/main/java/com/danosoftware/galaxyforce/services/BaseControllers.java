package com.danosoftware.galaxyforce.services;

import com.danosoftware.galaxyforce.controller.interfaces.BaseController;
import com.danosoftware.galaxyforce.options.OptionController;

import java.util.HashMap;
import java.util.Map;

public class BaseControllers {
    // holds a map of controller type to base controller
    private static final Map<OptionController, BaseController> controllerMap = new HashMap<OptionController, BaseController>();

    // private constructor
    private BaseControllers() {

    }

    // register a controller type with controller implementation
    public static void registerController(OptionController type, BaseController controller) {
        if (type == null) {
            throw new IllegalArgumentException("Supplied ControllerType object can not be null.");
        }

        if (controller == null) {
            throw new IllegalArgumentException("Supplied BaseController object can not be null.");
        }

        controllerMap.put(type, controller);
    }

    // return the controller
    public static BaseController getBaseController(OptionController type) {
        return controllerMap.get(type);
    }
}
