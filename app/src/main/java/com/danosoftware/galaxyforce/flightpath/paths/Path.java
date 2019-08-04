package com.danosoftware.galaxyforce.flightpath.paths;

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
    TRIANGULAR("triangular.json"),

    /*
     * Path where aliens move from left to right in an arc. Aliens originally
     * rise as they move to right. At top of arc at mid-way point and then move
     * downwards in arc as they continue to move right.
     */
    SINGLE_ARC("singleArc.json"),

    /**
     * space invader style attack - gradually moving across screen and then down
     */
    SPACE_INVADER("spaceInvader.json"),

    /**
     * Figure of eight attack. Starting at top right looping round in figure
     * figure of eight and finishing at top right.
     */
    FIGURE_OF_EIGHT("figureOfEight.json"),

    /**
     * Bezier curve from bottom left, crosses to right hand-side then leaves at
     * top right.
     */
    BEZIER_STEP_UP("bezierStepUp.json"),

    /**
     * Alien starts at top left edge, moves straight down, pauses and then exit
     * on opposite right-side of screen.
     */
    EXIT_STAGE_RIGHT("exitStageRight.json"),

    /**
     * straight line down screen from top to bottom. designed to be adjusted in
     * x-axis so any straight line path from top to bottom at any x-position can
     * be used.
     */
    STRAIGHT_DOWN("straightDown.json"),

    /**
     * straight line across screen from left to right and then back again.
     * Designed to be adjusted in y-axis so horiontal path at any y-position can
     * be used.
     */
    LEFT_AND_RIGHT("leftAndRight.json"),

    /**
     * straight line down screen from top to near bottom then bounce up again.
     * designed to be adjusted in x-axis so any straight line path from top to
     * bottom at any x-position can be used.
     */
    BOUNCE_DOWN_AND_UP("bounceDownAndUp.json"),

    /**
     * Drops from top-left down to valley and up again to top right.
     */
    VALLEY_DROP("valleyDrop.json"),

    /*
     * Bell Curve path. Start bottom left, rises to peak in middle then drops to
     * bottom right.
     */
    BELL_CURVE("bellCurve.json"),

    /*
     * Starts bottom left, twists around loop and exits top right
     */
    LOOPER("looper.json"),

    /*
     * Tear drop shaped path starting from and ending at top middle.
     */
    TEAR_DROP("tearDrop.json"),

    /*
     * Spirals down the screen from top to bottom
     */
    SPIRAL("spiral.json");


    // file name holding path data
    private final String pathFile;

    Path(String pathFile) {
        this.pathFile = pathFile;
    }

    public String getPathFile() {
        return pathFile;
    }
}
