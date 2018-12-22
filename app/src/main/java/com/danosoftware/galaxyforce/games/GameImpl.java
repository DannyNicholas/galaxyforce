package com.danosoftware.galaxyforce.games;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.danosoftware.galaxyforce.billing.service.IBillingService;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.input.GameInput;
import com.danosoftware.galaxyforce.interfaces.Audio;
import com.danosoftware.galaxyforce.interfaces.FileIO;
import com.danosoftware.galaxyforce.interfaces.Input;
import com.danosoftware.galaxyforce.screen.IScreen;
import com.danosoftware.galaxyforce.screen.enums.ScreenType;
import com.danosoftware.galaxyforce.screen.factories.ScreenFactory;
import com.danosoftware.galaxyforce.services.Configurations;
import com.danosoftware.galaxyforce.services.IPreferences;
import com.danosoftware.galaxyforce.services.PreferencesInteger;
import com.danosoftware.galaxyforce.services.PreferencesString;
import com.danosoftware.galaxyforce.services.SavedGame;
import com.danosoftware.galaxyforce.sound.SoundEffectBankSingleton;
import com.danosoftware.galaxyforce.sound.SoundPlayer;
import com.danosoftware.galaxyforce.sound.SoundPlayerSingleton;
import com.danosoftware.galaxyforce.vibration.Vibration;
import com.danosoftware.galaxyforce.vibration.VibrationSingleton;
import com.danosoftware.galaxyforce.view.AndroidAudio;
import com.danosoftware.galaxyforce.view.GLGraphics;
import com.danosoftware.galaxyforce.view.GameFileIO;

/**
 * @author Danny
 * <p>
 * Initialises model, controller and view for game. Handles the main
 * game loop using the controller, model and view.
 */
public class GameImpl implements Game {

    private static final String LOCAL_TAG = "GameImpl";

    /* reference to GL graphics */
    private GLGraphics glGraphics;

    /* reference to file input/output */
    private FileIO fileIO;

    /* reference to game input */
    private final Input input;

    /* reference to current screen */
    private IScreen screen;

    /* reference to current screen */
    private IScreen returningScreen;

    /* reference to billing service */
    private final IBillingService billingService;

    /* reference to game audio */
    private Audio audio;

    public GameImpl(Context context, GLGraphics glGraphics, GLSurfaceView glView, IBillingService billingService) {
        this.fileIO = new GameFileIO(context);
        this.audio = new AndroidAudio(context);
        this.glGraphics = glGraphics;
        this.billingService = billingService;
        this.input = new GameInput(glView, 1, 1);

        /*
         * initialise sound effect bank singleton. initialise as early as
         * possible to ensure sound effects are available when needed.
         */
        if (!SoundEffectBankSingleton.isInitialised()) {
            // initialise configuration
            SoundEffectBankSingleton.initialise(audio);
        }

        /* initialise configuration singleton */
        if (!Configurations.isInitialised()) {
            // set-up reference to shared preference.
            // used for persisting configuration
            IPreferences<String> configPreferences = new PreferencesString(context);

            // initialise configuration
            Configurations.initialise(configPreferences);
        }

        /* initialise vibrator singleton */
        if (!VibrationSingleton.isInitialised()) {
            // initialise vibration
            VibrationSingleton.initialise(context);
        }

        Configurations configurations = Configurations.getInstance();

        // enable or disable vibrator depending current configuration
        Vibration vibrator = VibrationSingleton.getInstance();
        vibrator.setVibrationEnabled(configurations.getVibrationOption());

        // enable or disable sound depending current configuration
        SoundPlayer soundPlayer = SoundPlayerSingleton.getInstance();
        soundPlayer.setSoundEnabled(configurations.getSoundOption());

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
        this.screen = ScreenFactory.newScreen(
                glGraphics,
                fileIO,
                billingService,
                this,
                input,
                ScreenType.SPLASH);
    }

    @Override
    public void setScreen(ScreenType screenType) {

        changeScreen(
                ScreenFactory.newScreen(
                        glGraphics,
                        fileIO,
                        billingService,
                        this,
                        input,
                        screenType));
    }

    @Override
    public void setGameScreen(int wave) {

        changeScreen(
                ScreenFactory.newGameScreen(
                        glGraphics,
                        fileIO,
                        billingService,
                        this,
                        input,
                        wave));
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
    public void setReturningScreen(ScreenType gameScreen) {

        // set returning screen to current screen
        this.returningScreen = this.screen;

        // call normal set screen method to change screens
        setScreen(gameScreen);
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

        // check if screen exists as resume is also called onCreate before
        // screen exists
        if (screen != null) {
            screen.resume();
        }

        // glView.onResume();

        // what state do we use?
        // maybe separate pause/resume from other states
        // model.setState(State.PAUSED);
    }

    @Override
    public void pause() {

        Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": Pause Game");

        screen.pause();

        // glView.onPause();

        // what state do we use?
        // maybe separate pause/resume from other states

        // model.setState(State.PAUSED);
    }

    @Override
    public void dispose() {
        Log.i(GameConstants.LOG_TAG, LOCAL_TAG + ": Dispose Game");

        screen.dispose();
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
}
