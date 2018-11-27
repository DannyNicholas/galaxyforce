package com.danosoftware.galaxyforce;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.danosoftware.galaxyforce.billing.service.BillingServiceImpl;
import com.danosoftware.galaxyforce.billing.service.IBillingService;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.interfaces.Game;
import com.danosoftware.galaxyforce.services.Games;
import com.danosoftware.galaxyforce.services.PackageManagers;
import com.danosoftware.galaxyforce.services.WindowManagers;
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
    private Game game = null;

    /* used for state synchronisation */
    private final Object stateChanged = new Object();

    /* application state */
    private ActivityState state = ActivityState.INITIALISED;

    /* GL Graphics reference */
    private GLGraphics glGraphics = null;

    /* GL Surface View reference */
    private GLSurfaceView glView = null;

    /* Billing Service for In-App Billing Requests */
    private IBillingService billingService;

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

        this.billingService = new BillingServiceImpl(this);

        // set-up window manager service
        WindowManager windowMgr = (WindowManager) this.getSystemService(Activity.WINDOW_SERVICE);
        WindowManagers.newWindowMgr(windowMgr);

        // set-up package manager service
        PackageManager packageMgr = this.getPackageManager();
        String packageName = this.getPackageName();
        PackageManagers.newPackageMgr(packageMgr, packageName);

        // create instance of game
        Games.newGame(this, glGraphics, glView, billingService);
        game = Games.getGame();
    }

    /* runs after onCreate or resuming after being paused */
    @Override
    protected void onResume() {

        Log.i(GameConstants.LOG_TAG, ACTIVITY_TAG + ": Resume Application");
        super.onResume();
        glView.onResume();

        /*
         * refresh billing service product states. will initialise product
         * states on start-up and refresh states on application resume. onresume
         * also called after product purchases and so will refresh any product
         * state changes.
         */
        if (billingService != null) {
            billingService.refreshProductStates();
        }

        // game.resume();
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

        // destroy billing service on activity destroy to avoid degrading device
        billingService.destroy();
    }

    /**
     * Handles the results of any results sent back to the activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(GameConstants.LOG_TAG, ACTIVITY_TAG + ": onActivityResult(" + requestCode + "," + resultCode + "," + data + ").");

        // Pass on the activity result to the billing service for handling
        boolean processed = false;
        if (billingService != null && requestCode == GameConstants.BILLING_REQUEST) {
            processed = billingService.processActivityResult(requestCode, resultCode, data);
        }

        // uses superclass methods if not handled
        if (processed == false) {
            Log.d(GameConstants.LOG_TAG, ACTIVITY_TAG + ": Pass activity result to superclass.");
            super.onActivityResult(requestCode, resultCode, data);
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
