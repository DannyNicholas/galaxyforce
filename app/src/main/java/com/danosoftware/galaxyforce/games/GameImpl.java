package com.danosoftware.galaxyforce.games;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.danosoftware.galaxyforce.billing.service.IBillingService;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.input.GameInput;
import com.danosoftware.galaxyforce.interfaces.FileIO;
import com.danosoftware.galaxyforce.interfaces.Input;
import com.danosoftware.galaxyforce.options.OptionSound;
import com.danosoftware.galaxyforce.screen.IScreen;
import com.danosoftware.galaxyforce.screen.enums.ScreenType;
import com.danosoftware.galaxyforce.screen.factories.ScreenFactory;
import com.danosoftware.galaxyforce.services.IPreferences;
import com.danosoftware.galaxyforce.services.PreferencesInteger;
import com.danosoftware.galaxyforce.services.PreferencesString;
import com.danosoftware.galaxyforce.services.SavedGame;
import com.danosoftware.galaxyforce.services.configurations.ConfigurationService;
import com.danosoftware.galaxyforce.services.configurations.ConfigurationServiceImpl;
import com.danosoftware.galaxyforce.sound.AndroidAudio;
import com.danosoftware.galaxyforce.sound.Audio;
import com.danosoftware.galaxyforce.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.sound.SoundPlayerServiceImpl;
import com.danosoftware.galaxyforce.vibration.Vibration;
import com.danosoftware.galaxyforce.vibration.VibrationSingleton;
import com.danosoftware.galaxyforce.view.GLGraphics;
import com.danosoftware.galaxyforce.view.GameFileIO;

/**
 * Initialises model, controller and view for game. Handles the main
 * game loop using the controller, model and view.
 */
public class GameImpl implements Game {

    private static final String LOCAL_TAG = "GameImpl";

    // reference to current screen
    private IScreen screen;

    // reference to screen to return to.
    // used in cases where we temporarily go to one
    // screen but expect to return to where we came from.
    // e.g. returning from an options screen back to the main game
    private IScreen returningScreen;

    // factory used to create new screens
    private final ScreenFactory screenFactory;

    private final SoundPlayerService sounds;

    public GameImpl(
            Context context,
            GLGraphics glGraphics,
            GLSurfaceView glView,
            IBillingService billingService) {

        FileIO fileIO = new GameFileIO(context);
        Input input = new GameInput(glView, 1, 1);
        Audio audio = new AndroidAudio(context);
        String versionName = versionName(context);

        // set-up configuration service that uses shared preferences
        // for persisting configuration
        IPreferences<String> configPreferences = new PreferencesString(context);
        ConfigurationService configurationService = new ConfigurationServiceImpl(configPreferences);

        boolean enableSounds = (configurationService.getSoundOption() == OptionSound.ON);
        this.sounds = new SoundPlayerServiceImpl(context, enableSounds);

        this.screenFactory = new ScreenFactory(
                glGraphics,
                fileIO,
                billingService,
                configurationService,
                sounds,
                this,
                input,
                versionName);

        /* initialise vibrator singleton */
        if (!VibrationSingleton.isInitialised()) {
            // initialise vibration
            VibrationSingleton.initialise(context);
        }

        // enable or disable vibrator depending current configuration
        Vibration vibrator = VibrationSingleton.getInstance();
        vibrator.setVibrationEnabled(configurationService.getVibrationOption());

        /* initialise saved game singleton */
        if (!SavedGame.isInitialised()) {
            // set-up reference to shared preference.
            // used for persisting saved games
            IPreferences<Integer> savedGamePreferences = new PreferencesInteger(context);

            // initialise configuration
            SavedGame.initialise(savedGamePreferences);
        }
    }

    @Override
    public void start() {
        Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": Start Game");
        this.screen = screenFactory.newScreen(ScreenType.SPLASH);
    }

    @Override
    public void changeToScreen(ScreenType screenType) {

        changeScreen(
                screenFactory.newScreen(screenType));
    }

    @Override
    public void changeToGameScreen(int wave) {

        changeScreen(
                screenFactory.newGameScreen(wave));
    }

    private void changeScreen(IScreen newScreen) {

        // pause and dispose current screen
        this.screen.pause();
        this.screen.dispose();

        // resume and update new screen
        this.screen = newScreen;
        this.screen.resume();
        this.screen.update(0);
    }

    @Override
    public void changeToReturningScreen(ScreenType gameScreen) {

        // set returning screen to current screen
        this.returningScreen = this.screen;

        // call normal set screen method to change screens
        changeToScreen(gameScreen);
    }

    @Override
    public void screenReturn() {
        if (returningScreen == null)
            throw new GalaxyForceException("Returning Screen must not be null");

        // return back to previous screens
        changeScreen(returningScreen);

        // clear returning screen
        this.returningScreen = null;
    }

    @Override
    public void resume() {
        Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": Resume Game");
        screen.resume();
    }

    @Override
    public void pause() {
        Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": Pause Game");
        screen.pause();
    }

    @Override
    public void dispose() {
        Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": Dispose Game");
        screen.dispose();
        sounds.dispose();
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
