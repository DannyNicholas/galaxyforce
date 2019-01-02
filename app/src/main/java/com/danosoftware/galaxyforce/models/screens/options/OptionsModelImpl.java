package com.danosoftware.galaxyforce.models.screens.options;

import android.util.Log;

import com.danosoftware.galaxyforce.buttons.sprite_text_button.OptionButton;
import com.danosoftware.galaxyforce.buttons.toggle_group.ToggleButtonGroup;
import com.danosoftware.galaxyforce.buttons.toggle_group.ToggleOption;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.touch.DetectButtonTouch;
import com.danosoftware.galaxyforce.enumerations.ModelState;
import com.danosoftware.galaxyforce.enumerations.TextPositionX;
import com.danosoftware.galaxyforce.games.Game;
import com.danosoftware.galaxyforce.options.Option;
import com.danosoftware.galaxyforce.options.OptionController;
import com.danosoftware.galaxyforce.options.OptionMusic;
import com.danosoftware.galaxyforce.options.OptionSound;
import com.danosoftware.galaxyforce.options.OptionVibration;
import com.danosoftware.galaxyforce.services.configurations.ConfigurationService;
import com.danosoftware.galaxyforce.sound.SoundEffect;
import com.danosoftware.galaxyforce.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SplashSprite;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Star;
import com.danosoftware.galaxyforce.sprites.properties.MenuSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.vibration.VibrateTime;
import com.danosoftware.galaxyforce.vibration.Vibration;
import com.danosoftware.galaxyforce.vibration.VibrationSingleton;

import java.util.ArrayList;
import java.util.List;

public class OptionsModelImpl implements OptionsModel {

    /* logger tag */
    private static final String TAG = "OptionsModelImpl";

    private final Game game;

    private final ConfigurationService configurationService;
    private final SoundPlayerService sounds;

    // references to stars
    private final List<Star> stars;

    // reference to all sprites in model
    private final List<ISprite> allSprites;

    private ModelState modelState;

    // reference to all text objects in model
    private final List<Text> allText;

    public OptionsModelImpl(
            Game game,
            Controller controller,
            ConfigurationService configurationService,
            SoundPlayerService sounds) {
        this.game = game;
        this.configurationService = configurationService;
        this.sounds = sounds;
        this.allSprites = new ArrayList<>();
        this.allText = new ArrayList<>();
        this.stars = Star.setupStars(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, MenuSpriteIdentifier.STAR_ANIMATIONS);
        buildAssets(controller);
    }

    private void buildAssets(Controller controller) {

        allSprites.addAll(stars);
        allSprites.add(new SplashSprite(GameConstants.SCREEN_MID_X, 817, MenuSpriteIdentifier.GALAXY_FORCE));

        allText.add(Text.newTextRelativePositionX("MOVEMENT", TextPositionX.CENTRE, 175 + (3 * 170)));

        ToggleButtonGroup controllerToggleGroup = new ToggleOption(
                this,
                configurationService.getControllerType());
        addOptionsButton(
                controller,
                3,
                0,
                OptionController.DRAG,
                controllerToggleGroup,
                0);
        addOptionsButton(
                controller,
                3,
                1,
                OptionController.JOYSTICK,
                controllerToggleGroup,
                0);
        addOptionsButton(
                controller,
                3,
                2,
                OptionController.ACCELEROMETER,
                controllerToggleGroup,
                0);

        allText.add(Text.newTextRelativePositionX(
                "SOUND EFFECTS",
                TextPositionX.CENTRE,
                175 + (2 * 170)));

        ToggleButtonGroup soundToggleGroup = new ToggleOption(
                this,
                configurationService.getSoundOption());
        addOptionsButton(
                controller,
                2,
                0,
                OptionSound.ON,
                soundToggleGroup,
                90);
        addOptionsButton(
                controller,
                2,
                1,
                OptionSound.OFF,
                soundToggleGroup,
                90);

        allText.add(Text.newTextRelativePositionX(
                "MUSIC",
                TextPositionX.CENTRE,
                175 + (1 * 170)));

        ToggleButtonGroup musicToggleGroup = new ToggleOption(
                this,
                configurationService.getMusicOption());
        addOptionsButton(
                controller,
                1,
                0,
                OptionMusic.ON,
                musicToggleGroup,
                90);
        addOptionsButton(
                controller,
                1,
                1,
                OptionMusic.OFF,
                musicToggleGroup,
                90);

        allText.add(Text.newTextRelativePositionX(
                "VIBRATION",
                TextPositionX.CENTRE,
                175 + (0 * 170)));

        ToggleButtonGroup vibrationToggleGroup = new ToggleOption(
                this,
                configurationService.getVibrationOption());
        addOptionsButton(
                controller,
                0,
                0,
                OptionVibration.ON,
                vibrationToggleGroup,
                90);
        addOptionsButton(
                controller,
                0,
                1,
                OptionVibration.OFF,
                vibrationToggleGroup,
                90);
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

        // move stars
        for (Star eachStar : stars) {
            eachStar.animate(deltaTime);
        }
    }

    @Override
    public void dispose() {
        // no action

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

    @Override
    public void optionSelected(Option optionSelected) {

        if (optionSelected instanceof OptionController) {
            OptionController controllerType = (OptionController) optionSelected;
            Log.d(TAG, "Controller Option Selected: " + controllerType.getText());
            configurationService.newControllerType(controllerType);
        }

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
        }

        if (optionSelected instanceof OptionVibration) {
            OptionVibration vibrationType = (OptionVibration) optionSelected;
            Log.d(TAG, "Vibration Option Selected: " + vibrationType.getText());
            configurationService.setVibrationOption(vibrationType);

            // update vibration service
            Vibration vibrator = VibrationSingleton.getInstance();
            vibrator.setVibrationEnabled(vibrationType);

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
}
