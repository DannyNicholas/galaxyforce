package com.danosoftware.galaxyforce.games;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.danosoftware.galaxyforce.billing.BillingService;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.input.GameInput;
import com.danosoftware.galaxyforce.input.Input;
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
import com.danosoftware.galaxyforce.view.GLGraphics;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Initialises model, controller and view for game. Handles the main
 * game loop using the controller, model and view.
 */
public class GameImpl implements Game {

    private static final String LOCAL_TAG = "GameImpl";

    // reference to current screen
    private IScreen screen;

    // often a screen will temporarily change to another screen (e.g OPTIONS)
    // and will then return back to where it came from when finished.
    // the screens to return back to are held in a stack.
    private final Deque<IScreen> returningScreens;

    // factory used to create new screens
    private final ScreenFactory screenFactory;

    private final SoundPlayerService sounds;
    private final MusicPlayerService music;
    private final VibrationService vibrator;

    public GameImpl(
            Context context,
            GLGraphics glGraphics,
            GLSurfaceView glView,
            BillingService billingService,
            GooglePlayServices playService,
            ConfigurationService configurationService) {

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
        this.music.play();

        IPreferences<Integer> savedGamePreferences = new PreferencesInteger(context);
        SavedGame savedGame = new SavedGameImpl(savedGamePreferences);

        this.screenFactory = new ScreenFactory(
                glGraphics,
                billingService,
                configurationService,
                sounds,
                music,
                vibrator,
                playService,
                savedGame,
                context.getAssets(),
                this,
                input,
                versionName);
    }

    @Override
    public void start() {
        Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": Start Game");
        this.screen = screenFactory.newScreen(ScreenType.SPLASH);
    }

    @Override
    public void changeToScreen(ScreenType screenType) {
        switchScreenWithoutReturn(
                screenFactory.newScreen(screenType));
    }

    @Override
    public void changeToReturningScreen(ScreenType screenType) {
        switchScreenWithReturn(
                screenFactory.newScreen(screenType));
    }

    @Override
    public void changeToGameScreen(int wave) {
        switchScreenWithoutReturn(
                screenFactory.newGameScreen(wave));
    }

    @Override
    public void changeToGamePausedScreen(List<ISprite> pausedSprites) {
        switchScreenWithReturn(
                screenFactory.newPausedGameScreen(pausedSprites));
    }

    @Override
    public void changeToGameOverScreen(int previousWave) {
        switchScreenWithoutReturn(
                screenFactory.newGameOverScreen(previousWave));
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
        Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": Resume Game");
        screen.resume();
        sounds.resume();
        music.play();
    }

    @Override
    public void pause() {
        Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": Pause Game");
        screen.pause();
        sounds.pause();
        vibrator.stop();
        music.pause();
    }

    @Override
    public void dispose() {
        Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": Dispose Game");
        screen.dispose();
        sounds.dispose();
        music.dispose();
    }

    @Override
    public void draw(float deltaTime) {
        screen.draw(deltaTime);
    }

    @Override
    public void update(float deltaTime) {
        screen.update(deltaTime);
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
     * Retrieve version name of this package.
     * Can return null if version name can not be found.
     */
    private String versionName(Context context) {
        PackageManager packageMgr = context.getPackageManager();
        String packageName = context.getPackageName();

        if (packageMgr != null && packageName != null) {
            try {
                PackageInfo info = packageMgr.getPackageInfo(packageName, 0);
                if (info != null) {
                    return info.versionName;
                }
            } catch (PackageManager.NameNotFoundException e) {
                return null;
            }
        }
        return null;
    }
}
