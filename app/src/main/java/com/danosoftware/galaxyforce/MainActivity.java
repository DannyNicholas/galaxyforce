package com.danosoftware.galaxyforce;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.billingclient.api.BillingClient;
import com.danosoftware.galaxyforce.billing.BillingManager;
import com.danosoftware.galaxyforce.billing.BillingService;
import com.danosoftware.galaxyforce.billing.BillingServiceImpl;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.games.Game;
import com.danosoftware.galaxyforce.games.GameImpl;
import com.danosoftware.galaxyforce.view.GLGraphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends Activity {

    private enum ActivityState {
        INITIALISED, RUNNING, PAUSED, FINISHED, IDLE
    }

    /* logger tag */
    private static final String ACTIVITY_TAG = "MainActivity";

    /* reference to game instance */
    private Game game;

    /* used for state synchronisation */
    private final Object stateChanged = new Object();

    /* application state */
    private ActivityState state = ActivityState.INITIALISED;

    /* GL Graphics reference */
    private GLGraphics glGraphics;

    /* GL Surface View reference */
    private GLSurfaceView glView;

    /* Billing Manager for In-App Billing Requests */
    private BillingManager mBillingManager;

    /* runs when application initially starts */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(GameConstants.LOG_TAG, ACTIVITY_TAG + ": Create Application");

        // set application to use full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // stop window dimming when window is visible (recommended over
        // deprecated full wake lock)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // set-up GL view
        glView = new GLSurfaceView(this);
        glView.setRenderer(new GLRenderer());
        setContentView(glView);
        this.glGraphics = new GLGraphics(glView);

        // Create and initialize billing
        BillingService billingService = new BillingServiceImpl();
        BillingManager.BillingUpdatesListener billingListener = (BillingManager.BillingUpdatesListener) billingService;
        this.mBillingManager = new BillingManager(this, billingListener);

        // create instance of game
        game = new GameImpl(this, glGraphics, glView, billingService);
    }

    /* runs after onCreate or resuming after being in background */
    @Override
    protected void onResume() {

        Log.i(GameConstants.LOG_TAG, ACTIVITY_TAG + ": Resume Application");
        super.onResume();
        glView.onResume();

        // Note: We query purchases in onResume() to handle purchases completed while the activity
        // is inactive. For example, this can happen if the activity is destroyed during the
        // purchase flow. This ensures that when the activity is resumed it reflects the user's
        // current purchases.
        if (mBillingManager != null
                && mBillingManager.getBillingClientResponseCode() == BillingClient.BillingResponse.OK) {
            mBillingManager.queryPurchases();
        }
    }

    /* runs when application is paused */
    @Override
    protected void onPause() {
        Log.i(GameConstants.LOG_TAG, ACTIVITY_TAG + ": Pause Application");

        synchronized (stateChanged) {
            if (isFinishing()) {
                Log.i(GameConstants.LOG_TAG, ACTIVITY_TAG + ": Finish Application");
                state = ActivityState.FINISHED;
            } else {
                state = ActivityState.PAUSED;
            }

            while (true) {
                try {
                    stateChanged.wait();
                    break;
                } catch (InterruptedException e) {

                }
            }

        }

        glView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(ACTIVITY_TAG, "Destroying Billing Manager.");
        if (mBillingManager != null) {
            mBillingManager.destroy();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.i(GameConstants.LOG_TAG, ACTIVITY_TAG + ": Back Button Pressed");

            // if back button is handled internally then don't use normal
            // superclass's method
            if (game.handleBackButton()) {
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables "sticky immersive" mode.
        // hides UI system bars until user swipes to reveal them
        // see: https://developer.android.com/training/system-ui/immersive#sticky-immersive
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    /**
     * Inner class to handle graphics
     */
    private class GLRenderer implements Renderer {

        private static final String LOCAL_TAG = "GLRenderer";
        long startTime;

        @Override
        public void onDrawFrame(GL10 gl) {
            ActivityState stateCheck;

            synchronized (stateChanged) {
                stateCheck = state;
            }

            if (stateCheck == ActivityState.RUNNING) {
                float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
                startTime = System.nanoTime();

                game.update(deltaTime);
                game.draw(deltaTime);
            }

            if (stateCheck == ActivityState.PAUSED) {
                game.pause();

                synchronized (stateChanged) {
                    state = ActivityState.IDLE;
                    stateChanged.notifyAll();
                }
            }

            if (stateCheck == ActivityState.FINISHED) {
                game.pause();
                game.dispose();

                synchronized (stateChanged) {
                    state = ActivityState.IDLE;
                    stateChanged.notifyAll();
                }
            }
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": onSurfaceChanged. width: " + width + ". height: " + height + ".");
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": onSurfaceCreated");

            glGraphics.setGl(gl);

            synchronized (stateChanged) {
                if (state == ActivityState.INITIALISED) {
                    game.start();
                }
                state = ActivityState.RUNNING;
                game.resume();
                startTime = System.nanoTime();
            }
        }
    }
}
