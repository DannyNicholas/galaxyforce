package com.danosoftware.galaxyforce.input;

import java.util.List;

public interface Input {

  boolean isTouchDown(int pointer);

  int getTouchX(int pointer);

  int getTouchY(int pointer);

  List<TouchEvent> getTouchEvents();

  class TouchEvent {

    public static final int TOUCH_DOWN = 0;
    public static final int TOUCH_UP = 1;
    public static final int TOUCH_DRAGGED = 2;

    public int type;
    public int x, y;
    public int pointer;

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      if (type == TOUCH_DOWN) {
        builder.append("touch down, ");
      } else if (type == TOUCH_DRAGGED) {
        builder.append("touch dragged, ");
      } else {
        builder.append("touch up, ");
      }
      builder.append(pointer);
      builder.append(",");
      builder.append(x);
      builder.append(",");
      builder.append(y);
      return builder.toString();
    }
  }
}
