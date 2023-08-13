package com.danosoftware.galaxyforce.games;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.opengl.GLSurfaceView;
import android.util.Log;
import com.danosoftware.galaxyforce.billing.BillingService;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.input.GameInput;
import com.danosoftware.galaxyforce.input.Input;
import com.danosoftware.galaxyforce.models.screens.background.RgbColour;
import com.danosoftware.galaxyforce.options.OptionMusic;
import com.danosoftware.galaxyforce.options.OptionSound;
import com.danosoftware.galaxyforce.options.OptionVibration;
import com.danosoftware.galaxyforce.screen.IScreen;
import com.danosoftware.galaxyforce.screen.enums.ScreenType;
import com.danosoftware.galaxyforce.screen.factories.ScreenFactory;
import com.danosoftware.galaxyforce.services.configurations.ConfigurationService;
import com.danosoftware.galaxyforce.services.googleplay.GooglePlayServices;
import com.danosoftware.galaxyforce.services.music.Music;
import com.danosoftware.galaxyforce.services.music.MusicPlayerService;
import com.danosoftware.galaxyforce.services.music.MusicPlayerServiceImpl;
import com.danosoftware.galaxyforce.services.preferences.IPreferences;
import com.danosoftware.galaxyforce.services.preferences.PreferencesInteger;
import com.danosoftware.galaxyforce.services.savedgame.SavedGame;
import com.danosoftware.galaxyforce.services.savedgame.SavedGameImpl;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerServiceImpl;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.services.vibration.VibrationServiceImpl;
import com.danosoftware.galaxyforce.sprites.common.ISprite;
import com.danosoftware.galaxyforce.tasks.OnTaskCompleteListener;
import com.danosoftware.galaxyforce.tasks.ResultTask;
import com.danosoftware.galaxyforce.tasks.TaskCallback;
import com.danosoftware.galaxyforce.tasks.TaskService;
import com.danosoftware.galaxyforce.textures.TextureLoader;
import com.danosoftware.galaxyforce.textures.TextureRegionXmlParser;
import com.danosoftware.galaxyforce.textures.TextureService;
import com.danosoftware.galaxyforce.view.GLGraphics;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Initialises model, controller and view for game. Handles the main game loop using the controller,
 * model and view.
 */
public class GameImpl implements Game, OnTaskCompleteListener<IScreen> {

  private enum ScreenChangeType {
    RETURN_SCREEN,
    NO_RETURN_SCREEN
  }

  private static final String LOCAL_TAG = "GameImpl";
  // often a screen will temporarily change to another screen (e.g OPTIONS)
  // and will then return back to where it came from when finished.
  // the screens to return back to are held in a stack.
  private final Deque<IScreen> returningScreens;
  // factory used to create new screens
  private final ScreenFactory screenFactory;
  private final SoundPlayerService sounds;
  private final MusicPlayerService music;
  private final VibrationService vibrator;
  private final TextureService textureService;
  // reference to current screen
  private IScreen screen;

  private final TaskService taskService;

  private ScreenChangeType screenChangeType;
  private boolean newScreenReady;
  private IScreen newScreen;
  private boolean transitioningToScreen;
  private boolean gameStarted;

  public GameImpl(
      Context context,
      GLGraphics glGraphics,
      GLSurfaceView glView,
      BillingService billingService,
      GooglePlayServices playService,
      ConfigurationService configurationService,
      TaskService taskService) {

    this.returningScreens = new ArrayDeque<>();

    Input input = new GameInput(glView, 1, 1);
    String versionName = versionName(context);

    boolean enableSounds = (configurationService.getSoundOption() == OptionSound.ON);
    this.sounds = new SoundPlayerServiceImpl(context, enableSounds);

    boolean enableVibrator = (configurationService.getVibrationOption() == OptionVibration.ON);
    this.vibrator = new VibrationServiceImpl(context, enableVibrator);

    boolean enableMusic = (configurationService.getMusicOption() == OptionMusic.ON);
    this.music = new MusicPlayerServiceImpl(context, enableMusic);
    this.music.load(Music.MAIN_TITLE);

    IPreferences<Integer> savedGamePreferences = new PreferencesInteger(context);
    SavedGame savedGame = new SavedGameImpl(savedGamePreferences, playService);

    AssetManager assetManager = context.getAssets();
    this.textureService = new TextureService(
        new TextureRegionXmlParser(assetManager),
        new TextureLoader(assetManager),
        taskService);

    this.screenFactory = new ScreenFactory(
        glGraphics,
        billingService,
        configurationService,
        textureService,
        sounds,
        music,
        vibrator,
        playService,
        savedGame,
        assetManager,
        this,
        input,
        versionName,
        taskService);

    this.taskService = taskService;

    this.newScreenReady = false;
    this.transitioningToScreen = false;
    this.gameStarted = false;
  }

  @Override
  public void start() {
    Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": Start Game");
    this.screen = screenFactory.newScreen(ScreenType.SPLASH);
    gameStarted = true;
  }

  @Override
  public void changeToScreen(ScreenType screenType) {
    if (!transitioningToScreen) {
      screenChangeType = ScreenChangeType.NO_RETURN_SCREEN;
      createScreen(() -> screenFactory.newScreen(screenType));
    }
  }

  @Override
  public void changeToScreenAfterDelay(ScreenType screenType, int delayInMilliseconds) {
    if (!transitioningToScreen) {
      screenChangeType = ScreenChangeType.NO_RETURN_SCREEN;
      createScreen(() ->
      {
        try {
          TimeUnit.MILLISECONDS.sleep(delayInMilliseconds);
        } catch (InterruptedException e) {
          // no action
        }
        return screenFactory.newScreen(screenType);
      });
    }
  }

  @Override
  public void changeToReturningScreen(ScreenType screenType) {
    if (!transitioningToScreen) {
      screenChangeType = ScreenChangeType.RETURN_SCREEN;
      createScreen(() -> screenFactory.newScreen(screenType));
    }
  }

  @Override
  public void changeToGameScreen(int wave) {
    if (!transitioningToScreen) {
      screenChangeType = ScreenChangeType.NO_RETURN_SCREEN;
      createScreen(() -> screenFactory.newGameScreen(wave));
    }
  }

  @Override
  public void changeToGamePausedScreen(List<ISprite> pausedSprites, RgbColour backgroundColour) {
    if (!transitioningToScreen) {
      screenChangeType = ScreenChangeType.RETURN_SCREEN;
      createScreen(() -> screenFactory.newPausedGameScreen(pausedSprites, backgroundColour));
    }
  }

  @Override
  public void changeToGameOverScreen(int previousWave) {
    if (!transitioningToScreen) {
      screenChangeType = ScreenChangeType.NO_RETURN_SCREEN;
      createScreen(() -> screenFactory.newGameOverScreen(previousWave));
    }
  }

  @Override
  public void screenReturn() {
    if (returningScreens.isEmpty()) {
      throw new GalaxyForceException("Returning Screen stack is empty. No Screen to return to.");
    }

    // pause and dispose current screen
    this.screen.pause();
    this.screen.dispose();

    // switch back to previous screen on top of the stack
    switchScreen(returningScreens.pop());
  }

  @Override
  public void resume() {
    // in rare scenarios, resume() may be called before the game has initialised.
    // this can occur if the game paused immediately on start-up.
    if (!gameStarted) {
      this.start();
    }
    Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": Resume Game");
    textureService.reloadTextures();
    screen.resume();
    sounds.resume();
    music.play();
    transitioningToScreen = false;
  }

  @Override
  public void pause() {
    Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": Pause Game");
    screen.pause();
    textureService.disposeTextures();
    sounds.pause();
    vibrator.stop();
    music.pause();
  }

  @Override
  public void dispose() {
    Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": Dispose Game");
    screen.dispose();
    textureService.disposeTextures();
    sounds.dispose();
    music.dispose();
  }

  @Override
  public void draw() {
    screen.draw();
  }

  @Override
  public void update(float deltaTime) {
    if (newScreenReady) {
      switch (screenChangeType) {
        case NO_RETURN_SCREEN:
          switchScreenWithoutReturn(newScreen);
          break;
        case RETURN_SCREEN:
          switchScreenWithReturn(newScreen);
          break;
        default:
          throw new IllegalStateException("Unexpected value: " + screenChangeType);
      }
      newScreenReady = false;
      newScreen = null;
      transitioningToScreen = false;
    } else {
      screen.update(deltaTime);
    }
  }

  @Override
  public boolean handleBackButton() {
    return screen.handleBackButton();
  }

  /**
   * Discard the current screen and switch to a new screen.
   */
  private void switchScreen(IScreen newScreen) {
    // resume and update new screen
    this.screen = newScreen;
    this.screen.resume();
    this.screen.update(0);
  }

  /**
   * Change to a new screen that will not return to the current screen.
   */
  private void switchScreenWithoutReturn(IScreen newScreen) {

    // pause and dispose current screen
    this.screen.pause();
    this.screen.dispose();

    // we should also discard any previously stacked returning screens
    while (!returningScreens.isEmpty()) {
      IScreen stackedScreen = returningScreens.pop();
      stackedScreen.dispose();
    }

    switchScreen(newScreen);
  }

  /**
   * Change to a new screen that may return back to the current screen.
   */
  private void switchScreenWithReturn(IScreen newScreen) {

    // pause current screen.
    // we do not dispose screen and we expect to return to it later
    this.screen.pause();

    // push current screen onto the returning screens stack
    returningScreens.push(this.screen);

    switchScreen(newScreen);
  }

  /**
   * Retrieve version name of this package. Can return null if version name can not be found.
   */
  @SuppressWarnings("deprecation")
  private String versionName(Context context) {
    PackageManager packageMgr = context.getPackageManager();
    String packageName = context.getPackageName();

    if (packageMgr != null && packageName != null) {
      try {
        PackageInfo info;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
          info = packageMgr.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0));
        } else {
          // deprecated from API 33
          info = packageMgr.getPackageInfo(packageName, 0);
        }
        return info.versionName;
      } catch (PackageManager.NameNotFoundException e) {
        return null;
      }
    }
    return null;
  }

  // create a new screen - this can be a long process so is run in another thread to avoid blocking render thread.
  // will callback with the screen when created.
  private void createScreen(ResultTask<IScreen> screenTask) {
    Log.i(LOCAL_TAG, "Creating screen");
    transitioningToScreen = true;
    TaskCallback<IScreen> callback = new TaskCallback<>(screenTask, this);
    taskService.execute(callback);
  }

  // new screens are created in a different thread.
  // will callback with the new screen once it has been created.
  @Override
  public void onCompletion(IScreen screen) {
    Log.i(LOCAL_TAG, "Screen ready: " + screen);
    newScreen = screen;
    newScreenReady = true;
  }
}
