package com.danosoftware.galaxyforce.controllers.models.base_touch;

import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;

public class BaseDragModel implements TouchBaseControllerModel {

    // minimum distance from current touch point for it to be updated
    private static final float TOUCH_MOVE_RADIUS = 1f;

    // offset from touch point to base position
    // ensures base is positioned above finger touch position
    private static final int BASE_Y_OFFSET = 96;

    // stores the position of the touch point (target position for base)
    private float targetX;
    private float targetY;

    // reference to base being moved
    private final IBasePrimary base;

    public BaseDragModel(IBasePrimary base) {
        this.base = base;
        this.targetX = base.x();
        this.targetY = base.y() + BASE_Y_OFFSET;
    }

    // when finger released, reset target position to current position
    @Override
    public void releaseTouchPoint() {
        base.moveTarget(base.x(), base.y());
    }

    // update finger position when touched down or dragged
    @Override
    public void updateTouchPoint(float touchX, float touchY) {

        float touchDistance = (float) Math.sqrt(
                Math.pow(touchX - targetX, 2) + Math.pow(touchY - targetY, 2));

        /*
        if distance from current target touch point to new touch point
        is small then don't change current target.
         */
        if (touchDistance > TOUCH_MOVE_RADIUS) {
            // update target position based on new touch point.
            // base will use this to calculate it's movements.
            this.targetX = touchX;
            this.targetY = touchY;
            base.moveTarget((int) targetX, (int) targetY + BASE_Y_OFFSET);
        }
    }
}
