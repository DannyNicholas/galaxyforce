package com.danosoftware.galaxyforce;

import static com.danosoftware.galaxyforce.constants.GameConstants.BACKGROUND_ALPHA;
import static com.danosoftware.galaxyforce.constants.GameConstants.BACKGROUND_BLUE;
import static com.danosoftware.galaxyforce.constants.GameConstants.BACKGROUND_GREEN;
import static com.danosoftware.galaxyforce.constants.GameConstants.BACKGROUND_RED;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.VmPolicy;
import android.os.Trace;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import com.danosoftware.galaxyforce.billing.BillingManager;
import com.danosoftware.galaxyforce.billing.BillingManagerImpl;
import com.danosoftware.galaxyforce.billing.BillingService;
import com.danosoftware.galaxyforce.billing.BillingServiceImpl;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.games.Game;
import com.danosoftware.galaxyforce.games.GameImpl;
import com.danosoftware.galaxyforce.games.GameLoopTest;
import com.danosoftware.galaxyforce.services.configurations.ConfigurationService;
import com.danosoftware.galaxyforce.services.configurations.ConfigurationServiceImpl;
import com.danosoftware.galaxyforce.services.googleplay.GooglePlayServices;
import com.danosoftware.galaxyforce.services.preferences.IPreferences;
import com.danosoftware.galaxyforce.services.preferences.PreferencesString;
import com.danosoftware.galaxyforce.tasks.TaskService;
import com.danosoftware.galaxyforce.view.GLGraphics;
import com.danosoftware.galaxyforce.view.GLShaderHelper;
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

  /* GL Surface View reference */
  private GLSurfaceView glView;

  /* Billing Service for In-App Billing Requests */
  private BillingService billingService;

  private final TaskService taskService = new TaskService();

  /* runs when application initially starts */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.i(GameConstants.LOG_TAG, ACTIVITY_TAG + ": Create Application");
    super.onCreate(savedInstanceState);

    setupScreen();

    // set-up GL view
    glView = new GameGLSurfaceView(this);
    setContentView(glView);
    GLGraphics glGraphics = new GLGraphics(glView);

    // Create billing service
    BillingManager billingMgr = new BillingManagerImpl(this);
    billingService = new BillingServiceImpl(billingMgr);

    // set-up configuration service that uses shared preferences
    // for persisting configuration
    IPreferences<String> configPreferences = new PreferencesString(this);
    ConfigurationService configurationService = new ConfigurationServiceImpl(configPreferences);

    // initialise play games services
    GooglePlayServices mPlayServices = new GooglePlayServices(this);

    // create instance of game
    Intent launchIntent = getIntent();
    String launchIntentAction = launchIntent.getAction();
    if (launchIntentAction != null &&
        launchIntentAction.equals("com.google.intent.action.TEST_LOOP")) {
      game = new GameLoopTest(this, glGraphics, glView, billingService, mPlayServices,
          configurationService, taskService);
    } else {
      game = new GameImpl(this, glGraphics, glView, billingService, mPlayServices,
          configurationService, taskService);
    }

    Log.i(GameConstants.LOG_TAG, ACTIVITY_TAG + ": Application Created");

    // enable strict mode to detect memory leaks
    if (BuildConfig.DEBUG) {
      StrictMode.setVmPolicy(new VmPolicy.Builder()
          .detectLeakedClosableObjects()
          .penaltyLog()
          .build());
    }
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
    if (billingService != null) {
      billingService.queryPurchasesAsync();
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
          // this waits for renderer thread to complete pause.
          // GL work (e.g. unloading textures) must happen on renderer thread.
          // also applies a short wait time-out in scenarios where renderer isn't called (e.g. view has gone).
          // otherwise this would block indefinitely.
          stateChanged.wait(250);
          break;
        } catch (InterruptedException e) {
          Log.w(GameConstants.LOG_TAG, ACTIVITY_TAG + ": Unexpected InterruptedException",
              e);
        }
      }
    }
    glView.onPause();
    super.onPause();
    Log.i(GameConstants.LOG_TAG, ACTIVITY_TAG + ": Application Paused");
  }

  @Override
  protected void onDestroy() {
    Log.i(GameConstants.LOG_TAG, ACTIVITY_TAG + ": Destroying Application");
    super.onDestroy();

    taskService.dispose();

    Log.i(ACTIVITY_TAG, "Destroying Billing Service.");
    if (billingService != null) {
      billingService.destroy();
    }

    Log.i(GameConstants.LOG_TAG, ACTIVITY_TAG + ": Application Destroyed");
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      Log.i(GameConstants.LOG_TAG, ACTIVITY_TAG + ": Back Button Pressed");

      // if back button is handled internally then don't use normal
      // super-class's method
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

  private void setupScreen() {
    // turn off screen title
    requestWindowFeature(Window.FEATURE_NO_TITLE);

    // stop window dimming when window is visible
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
  }

  private void hideSystemUI() {
    // See https://developer.android.com/training/system-ui/immersive
    final Window window = getWindow();
    final View decorView = window.getDecorView();

    WindowInsetsControllerCompat windowInsetsController =
        WindowCompat.getInsetsController(window, decorView);

    // Configure the behavior of the hidden system bars
    windowInsetsController.setSystemBarsBehavior(
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    );

    // Hide both the status bar and the navigation bar
    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
  }

  /**
   * Inner class to handle graphics
   */
  private class GLRenderer implements Renderer {

    private static final String LOCAL_TAG = "GLRenderer";
    long startTime;

    @Override
    public void onDrawFrame(GL10 unused) {
      synchronized (stateChanged) {
        ActivityState stateCheck = state;

        if (stateCheck == ActivityState.RUNNING) {
          float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
          startTime = System.nanoTime();

          Trace.beginSection("onDrawFrame.update");
          game.update(deltaTime);
          Trace.endSection();
          Trace.beginSection("onDrawFrame.draw");
          game.draw();
          Trace.endSection();
        }

        if (stateCheck == ActivityState.PAUSED) {
          Log.i(GameConstants.LOG_TAG, "Render Pause");
          game.pause();
          state = ActivityState.IDLE;

          // notify waiting main thread
          stateChanged.notifyAll();
        }

        if (stateCheck == ActivityState.FINISHED) {
          Log.i(GameConstants.LOG_TAG, "Render Finish");
          game.pause();
          game.dispose();
          state = ActivityState.IDLE;

          // delete our GL shaders program
          GLShaderHelper.deleteProgram();

          // notify waiting main thread
          stateChanged.notifyAll();
        }
      }
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
      Log.i(GameConstants.LOG_TAG,
          LOCAL_TAG + ": onSurfaceChanged. width: " + width + ". height: " + height + ".");
      // set viewport to match view
      GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
      Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": onSurfaceCreated");

      // create and initialise our GL shaders program
      GLShaderHelper.createProgram();

      // set game background colour.
      // i.e. colour used when screen is cleared before each frame
      GLES20.glClearColor(
          BACKGROUND_RED,
          BACKGROUND_GREEN,
          BACKGROUND_BLUE,
          BACKGROUND_ALPHA);

      // Disable depth testing -- we're 2D only.
      GLES20.glDisable(GLES20.GL_DEPTH_TEST);

      // Don't need back-face culling.
      GLES20.glDisable(GLES20.GL_CULL_FACE);

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

  /**
   * Inner class for GL Surface View
   */
  private class GameGLSurfaceView extends GLSurfaceView {

    public GameGLSurfaceView(Context context) {
      super(context);

      // Create an OpenGL ES 2.0 context
      setEGLContextClientVersion(2);

      // Set the Renderer for drawing on the GLSurfaceView
      GLRenderer renderer = new GLRenderer();
      setRenderer(renderer);
    }
  }
}
