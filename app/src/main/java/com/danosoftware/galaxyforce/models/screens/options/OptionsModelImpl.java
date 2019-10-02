package com.danosoftware.galaxyforce.models.screens.options;

import android.util.Log;

import com.danosoftware.galaxyforce.buttons.sprite_text_button.OptionButton;
import com.danosoftware.galaxyforce.buttons.toggle_group.ToggleButtonGroup;
import com.danosoftware.galaxyforce.buttons.toggle_group.ToggleOption;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.touch.DetectButtonTouch;
import com.danosoftware.galaxyforce.games.Game;
import com.danosoftware.galaxyforce.models.buttons.ButtonModel;
import com.danosoftware.galaxyforce.models.buttons.ButtonType;
import com.danosoftware.galaxyforce.models.screens.ModelState;
import com.danosoftware.galaxyforce.options.Option;
import com.danosoftware.galaxyforce.options.OptionMusic;
import com.danosoftware.galaxyforce.options.OptionSound;
import com.danosoftware.galaxyforce.options.OptionVibration;
import com.danosoftware.galaxyforce.services.configurations.ConfigurationService;
import com.danosoftware.galaxyforce.services.googleplay.ConnectionState;
import com.danosoftware.galaxyforce.services.googleplay.GooglePlayObserver;
import com.danosoftware.galaxyforce.services.googleplay.GooglePlayServices;
import com.danosoftware.galaxyforce.services.music.MusicPlayerService;
import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrateTime;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.common.ISprite;
import com.danosoftware.galaxyforce.sprites.game.starfield.StarAnimationType;
import com.danosoftware.galaxyforce.sprites.game.starfield.StarField;
import com.danosoftware.galaxyforce.sprites.game.starfield.StarFieldTemplate;
import com.danosoftware.galaxyforce.sprites.mainmenu.MenuButton;
import com.danosoftware.galaxyforce.sprites.properties.MenuSpriteIdentifier;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.text.TextPositionX;

import java.util.ArrayList;
import java.util.List;

public class OptionsModelImpl implements OptionsModel, ButtonModel, GooglePlayObserver {

    /* logger tag */
    private static final String TAG = "OptionsModelImpl";

    private final Game game;
    private final Controller controller;

    private final ConfigurationService configurationService;
    private final SoundPlayerService sounds;
    private final MusicPlayerService music;
    private final VibrationService vibrator;
    private final GooglePlayServices playService;

    // references to stars
    private final StarField starField;

    // reference to all sprites in model
    private final List<ISprite> allSprites;

    private ModelState modelState;

    // reference to all text objects in model
    private final List<Text> allText;

    private boolean reBuildAssets;
    private ConnectionState connectionState;

    public OptionsModelImpl(
            Game game,
            Controller controller,
            ConfigurationService configurationService,
            SoundPlayerService sounds,
            MusicPlayerService music,
            VibrationService vibrator,
            GooglePlayServices playService,
            StarFieldTemplate starFieldTemplate) {
        this.game = game;
        this.controller = controller;
        this.configurationService = configurationService;
        this.sounds = sounds;
        this.music = music;
        this.vibrator = vibrator;
        this.playService = playService;
        this.allSprites = new ArrayList<>();
        this.allText = new ArrayList<>();
        this.starField = new StarField(starFieldTemplate, StarAnimationType.MENU);
        this.reBuildAssets = false;
        this.connectionState = playService.connectedState();

        // build screen assets
        buildAssets();

        // register for connection changes from the google play service
        playService.registerConnectionObserver(this);
    }

    private void buildAssets() {

        // clear any current touch controllers prior to adding buttons
        controller.clearTouchControllers();

        // clear current sprites prior to rebuilding
        allSprites.clear();
        allText.clear();

        allSprites.addAll(starField.getSprites());
//        allSprites.add(new SplashSprite(GameConstants.SCREEN_MID_X, 817, MenuSpriteIdentifier.GALAXY_FORCE));

        allText.add(Text.newTextRelativePositionX(
                "GOOGLE PLAY",
                TextPositionX.CENTRE,
                175 + (4 * 170)));

//        ConnectionState state = playService.connectedState();

        if (connectionState == ConnectionState.DISCONNECTED) {
            addNewMenuButton(controller, 4, "CONNECT", ButtonType.PLAY_CONNECT);
        }
        else if (connectionState == ConnectionState.CONNECTED) {
            addNewMenuButton(controller, 4, "CONNECTED", ButtonType.PLAY_DISCONNECT);
        }

        allText.add(Text.newTextRelativePositionX(
                "SOUND EFFECTS",
                TextPositionX.CENTRE,
                175 + (3 * 170)));

        ToggleButtonGroup soundToggleGroup = new ToggleOption(
                this,
                configurationService.getSoundOption());
        addOptionsButton(
                controller,
                3,
                0,
                OptionSound.ON,
                soundToggleGroup,
                90);
        addOptionsButton(
                controller,
                3,
                1,
                OptionSound.OFF,
                soundToggleGroup,
                90);

        allText.add(Text.newTextRelativePositionX(
                "MUSIC",
                TextPositionX.CENTRE,
                175 + (2 * 170)));

        ToggleButtonGroup musicToggleGroup = new ToggleOption(
                this,
                configurationService.getMusicOption());
        addOptionsButton(
                controller,
                2,
                0,
                OptionMusic.ON,
                musicToggleGroup,
                90);
        addOptionsButton(
                controller,
                2,
                1,
                OptionMusic.OFF,
                musicToggleGroup,
                90);

        allText.add(Text.newTextRelativePositionX(
                "VIBRATION",
                TextPositionX.CENTRE,
                175 + (1 * 170)));

        ToggleButtonGroup vibrationToggleGroup = new ToggleOption(
                this,
                configurationService.getVibrationOption());
        addOptionsButton(
                controller,
                1,
                0,
                OptionVibration.ON,
                vibrationToggleGroup,
                90);
        addOptionsButton(
                controller,
                1,
                1,
                OptionVibration.OFF,
                vibrationToggleGroup,
                90);

        addNewMenuButton(controller, 0, "BACK", ButtonType.EXIT);
    }

    @Override
    public List<ISprite> getSprites() {
        return allSprites;
    }

    @Override
    public List<Text> getText() {
        return allText;
    }

    @Override
    public void update(float deltaTime) {
        if (modelState == ModelState.GO_BACK) {
            // return back to previous screen
            game.screenReturn();
        }

        if (reBuildAssets) {
            buildAssets();
            reBuildAssets = false;
        }

        // move stars
        starField.animate(deltaTime);
    }

    @Override
    public void dispose() {
        // unregister for connection changes from the google play service
        playService.unregisterConnectionObserver(this);
    }

    private void addOptionsButton(
            Controller controller,
            int row,
            int column,
            Option optionType,
            ToggleButtonGroup toggleGroup,
            int offset) {

        OptionButton button = new OptionButton(
                90 + (column * 180) + offset,
                100 + (row * 170),
                optionType,
                MenuSpriteIdentifier.OPTION_UNSELECTED,
                MenuSpriteIdentifier.OPTION_SELECTED,
                toggleGroup);

        // add a new button to controller's list of touch controllers
        controller.addTouchController(new DetectButtonTouch(button));

        // add new button's sprite to list of sprites
        allSprites.add(button.getSprite());

        // add new button's text to list of text objects
        allText.add(button.getText());

        // add button to option group
        toggleGroup.addOption(button, optionType);
    }

    /**
     * add wanted menu button using the supplied row, label and type.
     */
    private void addNewMenuButton(
            Controller controller,
            int row,
            String label,
            ButtonType buttonType) {

        // create button
        MenuButton button = new MenuButton(
                this,
                GameConstants.GAME_WIDTH / 2,
                100 + (row * 170),
                label,
                buttonType,
                MenuSpriteIdentifier.MAIN_MENU,
                MenuSpriteIdentifier.MAIN_MENU_PRESSED);

        // add a new menu button to controller's list of touch controllers
        controller.addTouchController(new DetectButtonTouch(button));

        /// add new button
        allSprites.add(button.getSprite());
        allText.add(button.getText());
    }

    @Override
    public void optionSelected(Option optionSelected) {

        if (optionSelected instanceof OptionSound) {
            OptionSound soundType = (OptionSound) optionSelected;
            Log.d(TAG, "Sound Option Selected: " + soundType.getText());
            configurationService.setSoundOption(soundType);

            // update sound service
            sounds.setSoundEnabled(soundType == OptionSound.ON);

            // play sound (if enabled) to prove sound is on
            sounds.play(SoundEffect.ALIEN_FIRE);
        }

        if (optionSelected instanceof OptionMusic) {
            OptionMusic musicType = (OptionMusic) optionSelected;
            Log.d(TAG, "Music Option Selected: " + musicType.getText());
            configurationService.setMusicOption(musicType);

            // update music service
            boolean musicEnabled = (musicType == OptionMusic.ON);
            music.setMusicEnabled(musicEnabled);

            // play music
            if (musicEnabled) {
                music.play();
            } else {
                music.pause();
            }
        }

        if (optionSelected instanceof OptionVibration) {
            OptionVibration vibrationType = (OptionVibration) optionSelected;
            Log.d(TAG, "Vibration Option Selected: " + vibrationType.getText());
            configurationService.setVibrationOption(vibrationType);

            // update vibration service
            vibrator.setVibrationEnabled(vibrationType == OptionVibration.ON);

            // vibrate (if enabled) to prove vibrator is on
            vibrator.vibrate(VibrateTime.MEDIUM);
        }
    }

    @Override
    public void goBack() {
        Log.d(TAG, "Persist Configurations.");
        configurationService.persistConfigurations();

        this.modelState = ModelState.GO_BACK;
    }

    @Override
    public void resume() {
        // no action for this model
    }

    @Override
    public void pause() {
        // no action for this model
    }

    @Override
    public void processButton(ButtonType buttonType) {
        switch (buttonType) {
            case EXIT:
                Log.d(GameConstants.LOG_TAG, "Exit Options.");
                goBack();
                break;
            case PLAY_CONNECT:
                Log.d(GameConstants.LOG_TAG, "Connect to Google Play.");
                playService.startSignInIntent();
                break;
            case PLAY_DISCONNECT:
                Log.d(GameConstants.LOG_TAG, "Disconnect from Google Play.");
                playService.signOut();
                break;
            default:
                Log.e(GameConstants.LOG_TAG, "Unsupported button: '" + buttonType + "'.");
                break;
        }
    }

    /**
     * Receives notifications whenever the Google Play service connection state change
     * (e.g. following a successful login attempt)
     */
    @Override
    public void onConnectionStateChange(ConnectionState connectionState) {
        this.connectionState = connectionState;
        this.reBuildAssets = true;
    }
}
