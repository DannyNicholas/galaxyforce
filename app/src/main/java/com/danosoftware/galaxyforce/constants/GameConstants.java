package com.danosoftware.galaxyforce.constants;

import com.danosoftware.galaxyforce.BuildConfig;
import com.danosoftware.galaxyforce.models.screens.background.RgbColour;

public class GameConstants {
    // global logging tag
    public final static String LOG_TAG = "GalaxyForce";

    // Request code used to invoke Google Play sign-in user interactions.
    public static final int RC_SIGN_IN = 9001;

    // Saved Game name
    public static final String SAVED_GAME_FILENAME = "SavedGame";

    // maximum waves
    public final static int MAX_ZONES = 5;
    public final static int WAVES_PER_ZONE = 12;
    public final static int MAX_WAVES = MAX_ZONES * WAVES_PER_ZONE;

    // background screen colours
    public final static float BACKGROUND_RED = 12f / 255f;
    public final static float BACKGROUND_GREEN = 12f / 255f;
    public final static float BACKGROUND_BLUE = 30f / 255f;
    public final static float BACKGROUND_ALPHA = 1.0f;
    public final static RgbColour DEFAULT_BACKGROUND_COLOUR = new RgbColour(
            BACKGROUND_RED,
            BACKGROUND_GREEN,
            BACKGROUND_BLUE);

    // sprite positions
    public final static int LOGO_Y_POS = 817;
    public final static int PLANET_Y_POS = 100;

    /*
     * maximum free wave. users must upgrade to play beyond this wave.
     */
    public final static int MAX_FREE_WAVE = 12;

    /* maximum number of lives possible */
    public final static int MAX_LIVES = 5;

    // show the FPS counter if we have a DEBUG build
    public static final boolean SHOW_FPS = BuildConfig.DEBUG;

    /*
     * font characters in map - displayed text will only support these
     * characters
     */
    public final static String FONT_CHARACTER_MAP = "ABCDEFGHIJKLMNOPQRSTUVWXYZ 0123456789.,?!-#\"'&()@°+=*$£<>%¤¢¥Œœ";

    /* constants to set screen width and height */

    // original screen size - aspect ratio 1:1.50
    // WIDTH = 1200;
    // HEIGHT = 800;

    // nexus 10 screen size - aspect ratio 1:1.70
    // WIDTH = 2560;
    // HEIGHT = 1504;

    // samsung galaxy s4 mini screen size - aspect ratio 1:1.77
    // WIDTH = 960;
    // HEIGHT = 540;

    // current screen size - aspect ratio 1:1.70
    // public static final int GAME_WIDTH = 1360;
    // public static final int GAME_HEIGHT = 800;

    // portrait screen size - aspect ratio 1:1.70
    // 0, 0 at bottom-left
    // 540, 960 at top-right
    public static final int GAME_WIDTH = 540;
    public static final int GAME_HEIGHT = 960;

    // used to calculate starting points of off-screen aliens
    private static final int MAX_SPRITE_WIDTH = 64;
    private static final int MAX_SPRITE_HEIGHT = 64;

    // fixed points on screen to help define alien-flight paths
    public static final int SCREEN_TOP = GameConstants.GAME_HEIGHT + (MAX_SPRITE_WIDTH / 2);
    public static final int SCREEN_BOTTOM = 0 - (MAX_SPRITE_HEIGHT / 2);
    public static final int SCREEN_LEFT = 0 - (MAX_SPRITE_WIDTH / 2);
    public static final int SCREEN_RIGHT = GameConstants.GAME_WIDTH + (MAX_SPRITE_HEIGHT / 2);
    public static final int SCREEN_MID_X = GameConstants.GAME_WIDTH / 2;
    public static final int SCREEN_MID_Y = GameConstants.GAME_HEIGHT / 2;

    // fixed points that keep alien-flight paths on screens
    public static final int SCREEN_TOP_EDGE = GameConstants.GAME_HEIGHT - (MAX_SPRITE_HEIGHT / 2);
    public static final int SCREEN_BOTTOM_EDGE = (MAX_SPRITE_HEIGHT / 2);
    public static final int SCREEN_LEFT_EDGE = (MAX_SPRITE_WIDTH / 2);
    public static final int SCREEN_RIGHT_EDGE = GameConstants.GAME_WIDTH - (MAX_SPRITE_WIDTH / 2);

}
