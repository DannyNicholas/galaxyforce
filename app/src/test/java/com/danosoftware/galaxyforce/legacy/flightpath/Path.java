package com.danosoftware.galaxyforce.legacy.flightpath;

import com.danosoftware.galaxyforce.constants.GameConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * enum containing all the possible flight paths for aliens.
 * <p>
 * Useful Bezier Curve sites:
 * <p>
 * http://cubic-bezier.com/#.06,.93,0,1.01
 * https://www.desmos.com/calculator/cahqdxeshd
 */
public enum Path {

    /*
     * Triangular path where aliens cross-over where the top of the triangle.
     */
    TRIANGLULAR_PATH(new StraightLine(5, new Point(60, GameConstants.SCREEN_TOP), new Point(480, 340)), new StraightLine(5, new Point(480,
            340), new Point(60, 340)), new StraightLine(5, new Point(60, 340), new Point(480, GameConstants.SCREEN_TOP))),

    /*
     * Path where aliens move from left to right in an arc. Aliens originally
     * rise as they move to right. At top of arc at mid-way point and then move
     * downwards in arc as they continue to move right.
     */
    SINGLE_ARC(new BezierCurve(100, new Point(GameConstants.SCREEN_LEFT, GameConstants.SCREEN_MID_Y), new Point(
            GameConstants.SCREEN_LEFT + 200, GameConstants.SCREEN_MID_Y + 300), new Point(GameConstants.SCREEN_RIGHT - 200,
            GameConstants.SCREEN_MID_Y + 300), new Point(GameConstants.SCREEN_RIGHT, GameConstants.SCREEN_MID_Y))),

    /**
     * space invader style attack - gradually moving across screen and then down
     */
    SPACE_INVADER(5, new Point(50, GameConstants.SCREEN_TOP),

            new Point(50, 800), new Point(490, 800),

            new Point(490, 650), new Point(50, 650),

            new Point(50, 500), new Point(490, 500),

            new Point(490, 350), new Point(50, 350),

            new Point(50, 200), new Point(490, 200),

            new Point(490, 50), new Point(50, 50),

            new Point(50, GameConstants.SCREEN_BOTTOM)

    ),

    /**
     * Figure of eight attack. Starting at top right looping round in figure
     * figure of eight and finishing at top right.
     */
    FIGURE_OF_EIGHT(
            new BezierCurve(100,
                    new Point(GameConstants.SCREEN_LEFT_EDGE, GameConstants.SCREEN_TOP),
                    new Point(GameConstants.SCREEN_LEFT_EDGE, GameConstants.SCREEN_MID_Y),
                    new Point(GameConstants.SCREEN_RIGHT_EDGE, GameConstants.SCREEN_TOP),
                    new Point(GameConstants.SCREEN_RIGHT_EDGE, GameConstants.SCREEN_MID_Y)),
            new BezierCurve(100,
                    new Point(GameConstants.SCREEN_RIGHT_EDGE, GameConstants.SCREEN_MID_Y),
                    new Point(GameConstants.SCREEN_RIGHT_EDGE, GameConstants.SCREEN_MID_Y / 2),
                    new Point(GameConstants.SCREEN_LEFT_EDGE, GameConstants.SCREEN_MID_Y / 2),
                    new Point(GameConstants.SCREEN_LEFT_EDGE, GameConstants.SCREEN_MID_Y)),
            new BezierCurve(100,
                    new Point(GameConstants.SCREEN_LEFT_EDGE, GameConstants.SCREEN_MID_Y),
                    new Point(GameConstants.SCREEN_LEFT_EDGE, GameConstants.SCREEN_TOP),
                    new Point(GameConstants.SCREEN_RIGHT_EDGE, GameConstants.SCREEN_MID_Y),
                    new Point(GameConstants.SCREEN_RIGHT_EDGE, GameConstants.SCREEN_TOP))
    ),

    /**
     * Bezier curve from bottom left, crosses to right hand-side then leaves at
     * top right.
     */
    BEZIER_STEP_UP(
            new BezierCurve(100,
                    new Point(GameConstants.SCREEN_LEFT_EDGE, GameConstants.SCREEN_BOTTOM),
                    new Point(GameConstants.SCREEN_LEFT_EDGE, GameConstants.SCREEN_TOP),
                    new Point(GameConstants.SCREEN_RIGHT_EDGE, GameConstants.SCREEN_BOTTOM),
                    new Point(GameConstants.SCREEN_RIGHT_EDGE, GameConstants.SCREEN_TOP))
    ),

    /**
     * Alien starts at top left edge, moves straight down, pauses and then exit
     * on opposite right-side of screen.
     */
    EXIT_STAGE_RIGHT(
            new StraightLine(5,
                    new Point(GameConstants.SCREEN_LEFT_EDGE, GameConstants.SCREEN_TOP),
                    new Point(GameConstants.SCREEN_LEFT_EDGE, GameConstants.SCREEN_MID_Y)),
            new FlightPause(0.5f, new Point(GameConstants.SCREEN_LEFT_EDGE, GameConstants.SCREEN_MID_Y)),
            new BezierCurve(100,
                    new Point(GameConstants.SCREEN_LEFT_EDGE, GameConstants.SCREEN_MID_Y),
                    new Point(GameConstants.SCREEN_LEFT + 150, GameConstants.SCREEN_BOTTOM + 150),
                    new Point(GameConstants.SCREEN_LEFT_EDGE, GameConstants.SCREEN_BOTTOM + 150),
                    new Point(GameConstants.SCREEN_RIGHT, GameConstants.SCREEN_BOTTOM_EDGE))
    ),

    /**
     * straight line down screen from top to bottom. designed to be adjusted in
     * x-axis so any straight line path from top to bottom at any x-position can
     * be used.
     */
    STRAIGHT_DOWN(5, new Point(0, GameConstants.SCREEN_TOP),

            new Point(0, GameConstants.SCREEN_BOTTOM)

    ),

    /**
     * straight line down screen from top to near bottom then bounce up again.
     * designed to be adjusted in x-axis so any straight line path from top to
     * bottom at any x-position can be used.
     */
    BOUNCE_DOWN_AND_UP(5, new Point(0, GameConstants.SCREEN_TOP),

            new Point(0, GameConstants.SCREEN_BOTTOM + 350),

            new Point(0, GameConstants.SCREEN_TOP)

    ),

    WAVEY_HORIZONTAL(new BezierCurve(100, new Point(GameConstants.SCREEN_LEFT, 500), new Point(200, 700), new Point(300, 700), new Point(
            500, 500)), new BezierCurve(100, new Point(500, 500), new Point(700, 300), new Point(800, 300), new Point(1000, 500))),

    BEZIER_PATH_01(new BezierCurve(400, new Point(-50, -50), new Point(250, 250), new Point(250, 250), new Point(750, 750)),
            new BezierCurve(400, new Point(750, 750), new Point(250, 250), new Point(250, 250), new Point(1250, 850))),

    /**
     * Drops from top-left down to valley and up again to top right.
     */
    VALLEY_DROP(new BezierCurve(400, new Point(GameConstants.SCREEN_LEFT, GameConstants.SCREEN_TOP), new Point(GameConstants.SCREEN_MID_X,
            100), new Point(GameConstants.SCREEN_MID_X, 100), new Point(GameConstants.SCREEN_RIGHT, GameConstants.SCREEN_TOP))),

    /*
     * Bell Curve path. Start bottom left, rises to peak in middle then drops to
     * bottom right.
     */
    BELL_CURVE(new BezierCurve(100, new Point(GameConstants.SCREEN_LEFT, GameConstants.SCREEN_BOTTOM_EDGE), new Point(
            GameConstants.SCREEN_MID_X, GameConstants.SCREEN_BOTTOM_EDGE), new Point(GameConstants.SCREEN_LEFT,
            GameConstants.SCREEN_TOP_EDGE), new Point(GameConstants.SCREEN_MID_X, GameConstants.SCREEN_TOP_EDGE)),

            new BezierCurve(100, new Point(GameConstants.SCREEN_MID_X, GameConstants.SCREEN_TOP_EDGE), new Point(GameConstants.SCREEN_RIGHT,
                    GameConstants.SCREEN_TOP_EDGE), new Point(GameConstants.SCREEN_MID_X, GameConstants.SCREEN_BOTTOM_EDGE), new Point(
                    GameConstants.SCREEN_RIGHT, GameConstants.SCREEN_BOTTOM_EDGE))),
    /*
     * Starts bottom left, twists around loop and exits top right
     */
    LOOPER(new BezierCurve(150, new Point(GameConstants.SCREEN_RIGHT, GameConstants.SCREEN_BOTTOM), new Point(GameConstants.SCREEN_LEFT,
            GameConstants.SCREEN_TOP * 2), new Point(GameConstants.SCREEN_LEFT, -GameConstants.SCREEN_TOP), new Point(
            GameConstants.SCREEN_RIGHT, GameConstants.SCREEN_TOP))),

    /*
     * Tear drop shaped path starting from and ending at top middle.
     */
    TEAR_DROP(new BezierCurve(200, new Point(GameConstants.SCREEN_MID_X, GameConstants.SCREEN_TOP), new Point(
            GameConstants.SCREEN_MID_X - 200, GameConstants.SCREEN_BOTTOM), new Point(GameConstants.SCREEN_MID_X + 200,
            GameConstants.SCREEN_BOTTOM), new Point(GameConstants.SCREEN_MID_X, GameConstants.SCREEN_TOP))),

    BEZIER_DEMO(new BezierCurve(200, new Point(100, 100), new Point(700, 200), new Point(700, 500), new Point(100, 700)), new BezierCurve(
            200, new Point(100, 700), new Point(100, 500), new Point(500, 500), new Point(700, 400)), new BezierCurve(200, new Point(700,
            400), new Point(0, 400), new Point(700, 100), new Point(100, 100)), new BezierCurve(200, new Point(0, 800),
            new Point(600, 100), new Point(600, 100), new Point(1200, 800)), new BezierCurve(200, new Point(0, 0), new Point(1200, 800),
            new Point(0, 0), new Point(1200, 800))),

    STARIGHT_LINE_01(new StraightLine(5, new Point(-50, 850), new Point(600, 100))),

    STARIGHT_LINE_DEMO(new StraightLine(5, new Point(100, 100), new Point(700, 700)), new StraightLine(5, new Point(700, 700), new Point(
            700, 100)), new StraightLine(5, new Point(700, 100), new Point(100, 700)), new StraightLine(5, new Point(100, 700), new Point(
            300, 700)), new StraightLine(5, new Point(300, 700), new Point(0, 0)),
            new StraightLine(5, new Point(0, 0), new Point(800, 400))),

    ORIGINAL_ALIEN_PATH_01(new StraightLine(5, new Point(-50, 37), new Point(1055, 37)), new StraightLine(5, new Point(1055, 37),
            new Point(54, 608)), new StraightLine(5, new Point(54, 608), new Point(1250, 608))),

    ORIGINAL_ALIEN_PATH_02(new StraightLine(5, new Point(-50, 37), new Point(1055, 37)), new StraightLine(5, new Point(1055, 37),
            new Point(1055, 608)), new StraightLine(5, new Point(1055, 608), new Point(-50, 608)), new StraightLine(5, new Point(-50, 608),
            new Point(54, -50))),

    ORIGINAL_ALIEN_PATH_03(new StraightLine(5, new Point(-50, 46), new Point(522, 46)), new StraightLine(5, new Point(522, 46), new Point(
            28, 309)), new StraightLine(5, new Point(28, 309), new Point(522, 309)), new StraightLine(5, new Point(522, 309), new Point(28,
            573)), new StraightLine(5, new Point(28, 573), new Point(522, 573)), new StraightLine(5, new Point(522, 573), new Point(-50,
            -50))),

    ORIGINAL_ALIEN_PATH_04(new StraightLine(5, new Point(-37, -41), new Point(1237, -41)), new StraightLine(5, new Point(1237, -41),
            new Point(1237, 819)), new StraightLine(5, new Point(1237, 819), new Point(-37, 819)), new StraightLine(5, new Point(-37, 819),
            new Point(-37, -41))),

    ORIGINAL_ALIEN_PATH_05(new StraightLine(5, new Point(-50, 46), new Point(1172, 46)), new StraightLine(5, new Point(1172, 46),
            new Point(28, 309)), new StraightLine(5, new Point(28, 309), new Point(1172, 309)), new StraightLine(5, new Point(1172, 309),
            new Point(28, 573)), new StraightLine(5, new Point(28, 573), new Point(1250, 573))),

    CIRCULAR_DEMO(new CircularPath(new Point(500, 400), 10D));

    /* references list of FlightPaths for path */
    private List<FlightPath> pathList;

    /**
     * construct list of FlightPath objects from variable size array
     */
    Path(FlightPath... pathArray) {
        this.pathList = Arrays.asList(pathArray);
    }

    /**
     * Easier constructor for an array of lines where the starting point begins
     * where the last line ended.
     *
     * @param time
     * @param pointArray
     */
    Path(int speed, Point... pointArray) {
        Point startPoint = null;
        List<FlightPath> lines = new ArrayList<FlightPath>();

        for (Point endPoint : pointArray) {
            if (startPoint != null) {
                StraightLine nextLine = new StraightLine(speed, startPoint, endPoint);
                lines.add(nextLine);
            }
            startPoint = endPoint;
        }

        this.pathList = lines;
    }

    public List<FlightPath> getPathList() {
        return pathList;
    }

}
