package com.danosoftware.galaxyforce.constants;

public class GameConstants {
    // global logging tag
    public final static String LOG_TAG = "GalaxyForce";

    // maximum waves
    public final static int MAX_ZONES = 5;
    public final static int WAVES_PER_ZONE = 12;
    public final static int MAX_WAVES = MAX_ZONES * WAVES_PER_ZONE;

    /*
     * maximum free wave. users must upgrade to play beyond this wave.
     */
    public final static int MAX_FREE_WAVE = 5;

    /* maximum number of lives possible */
    public final static int MAX_LIVES = 5;

    // should we show the FPS counter
    public static final boolean SHOW_FPS = true;

    public final static Integer BASE_MAX_ENERGY_LEVEL = 8;


    // request code sent with billing requests
    public final static int BILLING_REQUEST = 10001;

    /* billing product IDs */
    public static final String FULL_GAME_PRODUCT_ID = "galaxy_force_full_game_unlock";
    public static final String ALL_LEVELS_PRODUCT_ID = "galaxy_force_all_levels_unlock";

    // font glyphs per row - i.e. characters in a row within texture map
    public final static int FONT_GLYPHS_PER_ROW = 8;

    // font glyphs width - i.e. width of individual character
    public final static int FONT_GLYPHS_WIDTH = 30;

    // font glyphs height - i.e. height of individual character
    public final static int FONT_GLYPHS_HEIGHT = 38;

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
    public static final int SCREEN_TOP_EDGE = GameConstants.GAME_HEIGHT - (MAX_SPRITE_WIDTH / 2);
    public static final int SCREEN_BOTTOM_EDGE = (MAX_SPRITE_HEIGHT / 2);
    public static final int SCREEN_LEFT_EDGE = (MAX_SPRITE_WIDTH / 2);
    public static final int SCREEN_RIGHT_EDGE = GameConstants.GAME_WIDTH - (MAX_SPRITE_HEIGHT / 2);

}
