package com.danosoftware.galaxyforce.controllers.touch_base;

import com.danosoftware.galaxyforce.controllers.models.base_touch.TouchBaseControllerModel;
import com.danosoftware.galaxyforce.controllers.touch.TouchController;

public interface BaseTouchController extends TouchController {

  void setBaseController(TouchBaseControllerModel baseModel);
}
