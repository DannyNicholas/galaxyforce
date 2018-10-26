package com.danosoftware.galaxyforce.controller.game;

import com.danosoftware.galaxyforce.controller.interfaces.TouchBaseControllerModel;
import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;

public class BaseDragModel implements TouchBaseControllerModel
{
    /* drag sprite animation */
//    private static final Animation DRAG_ANIMATION = new Animation(0.75f, GameSpriteIdentifier.DRAG_1, GameSpriteIdentifier.DRAG_2);


    // how far new touch point must be away from current touch point to be
    // updated
    private static final float TOUCH_MOVE_RADIUS = 1;

    private static final double HALF_PI = (float) Math.PI / 2f;

    // how far current touch point must be away from base point to move base
    // private static final float BASE_MOVE_RADIUS_LARGE = 10;
    // private static final float BASE_MOVE_RADIUS_SMALL = 5;

    /*
     * large movement increased as base movement can be very jittery when the
     * game is running slowly as base appears to be overshoot target so next
     * move goes back in other opposite direction as so on. the slower the game
     * is, the more the base will move and so the greater chance of overshooting
     * we have.
     * 
     * game optimised to run at 1/60 fps so increase BASE_MOVE_RADIUS_LARGE by
     * ratio of actual speed to ideal speed.
     */
    private static final float BASE_MOVE_RADIUS_LARGE = 20;
    private static final float BASE_MOVE_RADIUS_SMALL = 5;
    private static final float BASE_MOVE_RADIUS_DELTA = BASE_MOVE_RADIUS_LARGE - BASE_MOVE_RADIUS_SMALL;

    // offset from touch point to base position
    private static final int BASE_Y_OFFSET = -96;

    // stores the calculated weighting (ranges from 0 to 1)
    // used to determine how far to move base
//    private float weightingX;
//    private float weightingY;

    // stores the position of the touch point (target position for base)
    private float targetX;
    private float targetY;

    // store the position of the base
    // used in calculations relative to touch points
//    private float baseX = 0f;
//    private float baseY = 0f;

    /*
     * does weighting need re-calculating - to be set to true follwoing any
     * change to actual or base positions
     */
//    private boolean recalculateWeighting = false;

    /* reference to list of sprites */
//    private final ISprite dragSprite;
//    private final List<ISprite> sprites;

    /* contains reference to game model */
//    private ControllerBase controller = null;

    private final IBasePrimary base;

    /* timer to store time since animation timer reset */
//    private float animationTime = 0f;

    public BaseDragModel(IBasePrimary base)
    {
        this.base = base;
        this.targetX = base.x();
        this.targetY = base.y() + BASE_Y_OFFSET;

//        this.controller = controller;

//        this.weightingX = 0f;
//        this.weightingY = 0f;

//        this.dragSprite = new JoystickPoint(
//                0,
//                0,
//                DRAG_ANIMATION.getKeyFrame(
//                        0,
//                        Animation.ANIMATION_LOOPING));
//        this.sprites = Arrays.asList(dragSprite);dragSprite

        // add this drag controller as a direction listener
//        model.addDirectionListener(this);

        // set controller's base controller to use drag
//        controller.setBaseController(new ControllerDrag(model, this));
    }

//    @Override
//    public void update(float deltaTime)
//    {
//
////            animationTime += deltaTime;
////            dragSprite.changeType(
////                    DRAG_ANIMATION.getKeyFrame(
////                            animationTime,
////                            Animation.ANIMATION_LOOPING));
//
//    }

    // when finger released, reset weightings so base no longer moves
    @Override
    public void releaseTouchPoint()
    {
        // reset weighting so that base stops moving
//        this.weightingX = 0f;
//        this.weightingY = 0f;

        // reset target to current base position when finger lifted
//        this.targetX = this.baseX;
//        this.targetY = this.baseY;

        // show drag sprite when touch released
//        sprites.clear();
//        sprites.add(dragSprite);
//        animationTime = 0f;
    }

    // no action for drag implementation
//    @Override
//    public void setCentre(float centreX, float centreY)
//    {
//        this.baseX = centreX;
//        this.baseY = centreY + BASE_Y_OFFSET;
//
//        // ensures weighting is recalculated to avoid jitter
//        updateWeighting();
//
////        dragSprite.move(
////                (int) baseX,
////                (int) baseY);
//    }

    // update finger position when touched down or dragged
    @Override
    public void updateTouchPoint(float touchX, float touchY, float deltaTime)
    {
        // to avoid base jitter - check how far new touch point is away from
        // current touch point.
        // if distance is small then don't change current point.
        float touchDistance = (float) Math.sqrt(
                Math.pow(touchX - targetX, 2) + Math.pow(touchY - targetY, 2));
        if (touchDistance > TOUCH_MOVE_RADIUS)
        {
            // update target position based on new touch point
            this.targetX = touchX;
            this.targetY = touchY;

            // adjust base position by the offset
            // weighting is calculated from base to touch point
//            this.baseX = base.x();
//            this.baseY = base.y() + BASE_Y_OFFSET;

            // ensures weighting is recalculated to avoid jitter
            updateWeighting(
                    targetX - base.x(),
                    targetY - (base.y() + BASE_Y_OFFSET),
                    deltaTime);

//            float deltaX = targetX - baseX;
//        float deltaY = targetY - baseY;

            // move base by current weighting
//            setCentre(base.x(), base.y());



//            base.moveBase(weightingX, weightingY, deltaTime);

            // remove drag sprite when touching screen
//            sprites.clear();
        }

//        else
//        {
//            // if very small movement then exit
//            return;
//        }


    }

//    @Override
//    public List<ISprite> getSprites() {
//        return sprites;
//    }

//    @Override
//    public void reset()
//    {
//        // resets centre to current base position
//        setCentre(base.x(), base.y());
//
//        // reset weighting so that base stops moving
//        releaseTouchPoint();
//    }

    private void updateWeighting(float deltaX, float deltaY, float deltaTime)
    {
//        float deltaX = targetX - baseX;
//        float deltaY = targetY - baseY;

        // to avoid jitter when moving - check how far base is away from current
        // touch point.
        // if distance is small then don't move base
        float baseDistance = (float) Math.sqrt(
                (deltaX * deltaX) + (deltaY * deltaY));

        // if distance large then calculate weighting purely on angle between
        // base and current touch point
        if (baseDistance > BASE_MOVE_RADIUS_LARGE)
        {
            // calculate angle from circle centre to touch point
            // atan2 doesn't have limitations of atan but is PI/2 greater than
            // expected so manually correct this.
            float theta = (float) (HALF_PI - Math.atan2(deltaY, deltaX));

            // calculate weighting (used to calculate how far to move base's x
            // and y). Ranges from 0 to 1.
//            this.weightingX = (float) Math.sin(theta);
//            this.weightingY = (float) Math.cos(theta);

            base.moveBase(
                    (float) Math.sin(theta),
                    (float) Math.cos(theta),
                    deltaTime);
        }

        // if distance small then calculate weighting based on angle but
        // multiply by a calculated weight. Much smoother movement for small
        // moves than previous calculation.
        else if (baseDistance > BASE_MOVE_RADIUS_SMALL)
        {
            // calculate angle from circle centre to touch point
            // atan2 doesn't have limitations of atan but is PI/2 greater than
            // expected so manually correct this.
            float theta = (float) (HALF_PI - Math.atan2(deltaY, deltaX));

            // calculate weighting using distance and threshold.
            // result ranges from 0-1.
            float distanceWeight = (baseDistance - BASE_MOVE_RADIUS_SMALL)
                    / (BASE_MOVE_RADIUS_DELTA);

//            this.weightingX = (float) (distanceWeight * Math.sin(theta));
//            this.weightingY = (float) (distanceWeight * Math.cos(theta));

            base.moveBase(
                    (float) (distanceWeight * Math.sin(theta)),
                    (float) (distanceWeight * Math.cos(theta)),
                    deltaTime);
        }
        // if tiny movements (maybe involuntary) then do not move
//        else
//        {
//            this.weightingX = 0;
//            this.weightingY = 0;
//        }

//        recalculateWeighting = false;
    }
}
